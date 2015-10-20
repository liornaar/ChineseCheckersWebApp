/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.System.out;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import manager.GameManager;
import utils.ServletUtils;
import webLogic.WebManager;

/**
 *
 * @author user
 */
@WebServlet(name = "waitingRoomServlet", urlPatterns = {"/waiting"})
public class waitingRoomServlet extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");

        GameManager gameManager = ServletUtils.getGameManager(getServletContext());
        WebManager webManager = ServletUtils.getWebManager(getServletContext());

        boolean gameCanStart = false;

        HttpSession session = request.getSession(true);
        String playerName = (String) session.getAttribute("playerName");

        if (!webManager.isPlayerInGame(playerName)) {
            int numberOfPlayersInside = webManager.getPlayersJoined();
            numberOfPlayersInside++;
            webManager.setPlayersJoined(numberOfPlayersInside);

            if (numberOfPlayersInside == webManager.getNumberOfPlayersToJoin()) {
                if (!webManager.isGameWasLoaded()) {
                    gameManager.initGame();
                }
                gameCanStart = true;
            }
        }

        if (webManager.gameIsReady()) {
            //gameManager.initGame();
            gameCanStart = true;
        }

        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(gameCanStart);
            out.print(jsonResponse);
            out.flush();
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

}
