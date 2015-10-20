/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.System.out;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import manager.GameManager;
import utils.ServletUtils;
import webLogic.WebManager;

/**
 *
 * @author oroze
 */

//TODO: maybe change name of urlpattern
@WebServlet(name = "NewGameOptionServlet", urlPatterns = {"/NewGameOptionServlet"})
public class NewGameOptionServlet extends HttpServlet {

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
      
        int numOfPlayers = Integer.parseInt(request.getParameter("numOfPlayers"));
        int numOfColorsForEach = Integer.parseInt(request.getParameter("numOfColorsForEach"));
        int numOfHumanPlayers = Integer.parseInt(request.getParameter("numOfHumanPlayers"));
        
        
        
        GameManager gameManager = ServletUtils.getGameManager(getServletContext());
        WebManager webManager = ServletUtils.getWebManager(getServletContext());

        if (webManager.isWaitingForPlayers() || webManager.isAllPlayersEnterd() || webManager.isInGame()) {
            //error, game is already created and waiting for players.
            out.println("error, game is already created.");
            response.getWriter().write("error");
        } else {

            gameManager.setNumberOfPlayers(numOfPlayers);
            gameManager.setNumberOfColorsForEachPlayer(numOfColorsForEach);
            gameManager.setNumberOfHumanPlayers(numOfHumanPlayers);

            webManager.setWaitingForPlayers(true);
            webManager.setNumberOfPlayersToJoin(numOfHumanPlayers);
            webManager.setNumberOfTotalPlayers(numOfPlayers);
            webManager.setNumberOfColorsForEachPlayer(numOfColorsForEach);
            System.out.println("going to player input");
            response.getWriter().write("playerNameInput.html");
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
