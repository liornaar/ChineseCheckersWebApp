/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logic.CheckersBoard;
import manager.GameManager;
import utils.ServletUtils;
import com.google.gson.Gson;
import java.awt.Point;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpSession;
import logic.Color;
import utils.sessionUtils;
import webLogic.WebManager;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author oroze
 */
@WebServlet(name = "GameServlet", urlPatterns = {"/GameServlet"})
public class GameServlet extends HttpServlet {
        private final Timer AITimer = new Timer();
        private final Timer kickTimer = new Timer();
        public TimerTask task;
        private kickManager KM;
        private boolean isBeginingOfGame = true;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String gameOverOrSkipped;
        GameManager gameManager = ServletUtils.getGameManager(getServletContext());
        WebManager webManager = ServletUtils.getWebManager(getServletContext());
   
        if (isBeginingOfGame) {
            resetKickTimer(gameManager, request, webManager);
            isBeginingOfGame = false;
        }
        

        webManager.setInGame(true);

        switch (request.getParameter("action")) {
            case "updateButtonsFromBoard":
                String colorsArrayJSON = updateButtonsFromBoard(gameManager);
                response.getWriter().write(colorsArrayJSON);
                break;
            case "pickMarble":
                pickMarble(gameManager, request);
                break;
            case "moveMarble":
                gameOverOrSkipped = moveMarble(gameManager, request, webManager);
                response.getWriter().write(gameOverOrSkipped);
                break;
            case "updatePointsFromBoard":
                String pointsArrayJSON = updatePointsFromBoard(gameManager);
                response.getWriter().write(pointsArrayJSON);
                break;
            case "finishTurn":
                finishTurn(gameManager, request);
                break;
            case "quit":
                gameOverOrSkipped = quit(gameManager, request, webManager, false);
                response.getWriter().write(gameOverOrSkipped);
                break;
            case "doAIMove":
                gameOverOrSkipped = doAIMove(gameManager, webManager);
                response.getWriter().write(gameOverOrSkipped);
                break;
            case "updateCurrentPlayerName":
                String name = updateCurrentPlayerName(gameManager);
                response.getWriter().write(name);
                break;
            case "checkIfGameEnded":
                gameOverOrSkipped = checkIfGameEnded(gameManager, webManager);
                response.getWriter().write(gameOverOrSkipped);
                break;
            default:
                throw new AssertionError();
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private String updateButtonsFromBoard(GameManager gameManager) {
        ArrayList<String> boardByColors = new ArrayList<>();

        for (int i = 0; i < CheckersBoard.ROWS; i++) {
            for (int j = 0; j < CheckersBoard.COLS; j++) {
                if (!gameManager.getBoard().isWall(i, j)) {
                    boardByColors.add(gameManager.getBoard().getColorByCord(i, j).toString());
                }
            }
        }
        Gson gson = new Gson();

        return gson.toJson(boardByColors);

    }

    private void pickMarble(GameManager gameManager, HttpServletRequest request) {       
        int row = Integer.parseInt(request.getParameter("row"));
        int col = Integer.parseInt(request.getParameter("col"));
        Point pickedMarble = new Point(row, col);

        String playerName = sessionUtils.getUsePlayerName(request);

        if (playerName.equals(gameManager.getCurrentPlayer().getName())) {
            if (!(gameManager.isOnSkipRoll()) && (gameManager.isCurrentUserMarble(pickedMarble))) {
                gameManager.calculatePossibleMoves(pickedMarble);
            }
        }
    }

    private String moveMarble(GameManager gameManager, HttpServletRequest request, WebManager webManager) {
        int row = Integer.parseInt(request.getParameter("row"));
        int col = Integer.parseInt(request.getParameter("col"));
        String gameOverOrSkipped = "none";
        String playerName = sessionUtils.getUsePlayerName(request);
        
        if (playerName.equals(gameManager.getCurrentPlayer().getName())) {

            gameManager.clearMovesFromBoard();
            gameManager.clearPossibleMoves();
            Point movingTo = new Point(row, col);
            gameManager.moveMarble(movingTo);
            if (gameManager.checkForWinner() != null) {
                gameOverOrSkipped = "gameOver";
//                task.cancel();
//                isBeginingOfGame = true;
//                gameManager.getPlayerNames().clear();
//                webManager.resetWebManager();
                resetSettingsAfterGameOver(gameManager, webManager);
            } else {
                if (gameManager.isSkipMove(movingTo)) {//keep turn alive, for skipping
                    gameOverOrSkipped = "skipped";
                    gameManager.calculateSkipMovesOnly(movingTo);
                    gameManager.setIsOnSkipRoll(true);
                } else {//finish Turn
                    task.cancel();
                    resetKickTimer(gameManager, request, webManager);
                    gameManager.setIsOnSkipRoll(false);
                    gameManager.switchTurns();
                }
            }
        }
        return gameOverOrSkipped;
    }

    private String updatePointsFromBoard(GameManager gameManager) {
        ArrayList<Point> points = new ArrayList<>();

        for (int i = 0; i < CheckersBoard.ROWS; i++) {
            for (int j = 0; j < CheckersBoard.COLS; j++) {
                if (!gameManager.getBoard().isWall(i, j)) {
                    points.add(new Point(i, j));
                }
            }
        }
        Gson gson = new Gson();

        return gson.toJson(points);

    }

    private void finishTurn(GameManager gameManager, HttpServletRequest request) {
        String playerName = sessionUtils.getUsePlayerName(request);

        if (playerName.equals(gameManager.getCurrentPlayer().getName())) {
            task.cancel();
            resetKickTimer(gameManager, request, null);
            gameManager.setIsOnSkipRoll(false);
            gameManager.switchTurns();
        }
    }

    private String quit(GameManager gameManager, HttpServletRequest request, WebManager webManager, boolean toRemoveCurrent) {
        String isGameOverOSwitched = "none";
        String playerName;
        
        if (toRemoveCurrent) {
            playerName = gameManager.getCurrentPlayer().getName();
        } else {
            playerName = sessionUtils.getUsePlayerName(request);
        }
        
        gameManager.removePlayerFromGame(playerName);
        
        if(playerName.equals(gameManager.getCurrentPlayer().getName())){
            gameManager.switchTurns();
            if (!gameManager.isHumanTurn()) {
                doAIMove(gameManager, webManager);
            }
            isGameOverOSwitched = "switched";
        }
        
        if (gameManager.isOnly1PlayerRemaining()|| gameManager.isOnlyComputersLeft()) {
            isGameOverOSwitched = "gameOver";
//            task.cancel();
//            isBeginingOfGame = true;
            gameManager.setLastPlayerAsCurrent();
//            gameManager.getPlayerNames().clear();
//            webManager.resetWebManager();
            resetSettingsAfterGameOver(gameManager, webManager);
        }
        
        return isGameOverOSwitched;
    }

    private String updateCurrentPlayerName(GameManager gameManager) {
        String playerName = gameManager.getCurrentPlayer().getName();
        playerName += " (Colors: ";
        for (Color color : gameManager.getCurrentPlayer().getColors()) {
            String toAdd = color.toString().toLowerCase();
            toAdd = toAdd.substring(0, 1).toUpperCase() + toAdd.substring(1);
            playerName += toAdd + ", ";
        }
        
        playerName = playerName.substring(0, playerName.length() - 2);
        playerName += ")";

        return playerName;
    }

    private String checkIfGameEnded(GameManager gameManager, WebManager webManager) {
        String isGameOver = "none";
        if (gameManager.checkForWinner() != null) {
            isGameOver = "gameOver";
//            task.cancel();
//            isBeginingOfGame = true;
            resetSettingsAfterGameOver(gameManager, webManager);
        } else if (gameManager.isOnly1PlayerRemaining() || gameManager.isOnlyComputersLeft()) {
            isGameOver = "gameOver";
//            task.cancel();
//            isBeginingOfGame = true;
            gameManager.setLastPlayerAsCurrent();
            resetSettingsAfterGameOver(gameManager, webManager);
        }
        return isGameOver;
    }
    
    private String doAIMove(GameManager gameManager, WebManager webManager) {
        ComputerManager CM = new ComputerManager(gameManager, webManager);
        AITimer.schedule(CM, 0);
        task.cancel();
        resetKickTimer(gameManager, null, webManager);
        return CM.isGameOverOrAnotherAIMove;
    }

    private void resetKickTimer(GameManager gameManager, HttpServletRequest request, WebManager webManager) {
        kickManager KM = new kickManager(gameManager, request, webManager);
        task = KM;
        kickTimer.schedule(KM, 25000); //25 sec
    }

    private class ComputerManager extends TimerTask {

        public String isGameOverOrAnotherAIMove = "none";
        public GameManager gameManager;
        public WebManager webManager;

        public ComputerManager(GameManager gameM, WebManager webM) {
            gameManager = gameM;
            webManager = webM;
        }

        @Override
        public void run() {
            if (!gameManager.isHumanTurn()) {
                ArrayList<Point> AIMove = gameManager.getAiMove();
                gameManager.setCurrentPickedMarble(AIMove.get(0));
                for (int i = 1; i < AIMove.size(); i++) {
                    gameManager.moveMarble(AIMove.get(i));
                    gameManager.setCurrentPickedMarble(AIMove.get(i));
                }

                try {
                    Thread.sleep(750);
                } catch (InterruptedException ex) {
                    // Logger.getLogger(GameServlet.class.getName()).log(Level.SEVERE, null, ex);
                }

                gameManager.clearPossibleMoves();
                if (gameManager.checkForWinner() != null) {
                    isGameOverOrAnotherAIMove = "gameOver";
//                    task.cancel();
//                    isBeginingOfGame = true;
                    resetSettingsAfterGameOver(gameManager, webManager);
                } else if (gameManager.isOnlyComputersLeft() || gameManager.isOnly1PlayerRemaining()) {
                    isGameOverOrAnotherAIMove = "gameOver";
//                    task.cancel();
//                    isBeginingOfGame = true;
                    gameManager.setLastPlayerAsCurrent();
                    resetSettingsAfterGameOver(gameManager, webManager);
                } else {
                   // resetKickTimer(gameManager, null, webManager);
                    gameManager.setIsOnSkipRoll(false);
                    gameManager.switchTurns();
                    if (!gameManager.isHumanTurn()) {
                    doAIMove(gameManager, webManager);
                     //   isGameOverOrAnotherAIMove = "anotherAIMove";
                    }

                    try {
                        Thread.sleep(750);
                    } catch (InterruptedException ex) {
                        // Logger.getLogger(GameServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

        }
    }
    
    private class kickManager extends TimerTask {

        public GameManager gameManager;
        public WebManager webManager;
        public HttpServletRequest request;

        public kickManager(GameManager gameM, HttpServletRequest req, WebManager webM) {
            gameManager = gameM;
            webManager = webM;
            request = req;
        }

        @Override
        public void run() {
            quit(gameManager, request, webManager, true);
            if (gameManager.isHumanTurn()) {
                task.cancel();
                resetKickTimer(gameManager, null, webManager);
            }
        }
    }

    private void resetSettingsAfterGameOver(GameManager gameManager, WebManager webManager) {
        task.cancel();
        isBeginingOfGame = true;
        
        gameManager.getPlayerNames().clear();
        gameManager.getHumanNames().clear();
        webManager.resetWebManager();
    }

}
