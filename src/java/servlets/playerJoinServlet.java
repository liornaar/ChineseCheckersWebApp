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
import java.util.ArrayList;
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
 * @author user
 */
@WebServlet(name = "playerJoinServlet", urlPatterns = {"/playerJoinServlet"})
public class playerJoinServlet extends HttpServlet {

    public static final String DUPLICATE_NAME = "duplicate_name";
    public static final String USER_FAIL = "user_fail";
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

        ArrayList<String> namesList = null;
        String playerName = request.getParameter("playerName");
        
        GameManager gameManager = ServletUtils.getGameManager(getServletContext());
        WebManager webManager = ServletUtils.getWebManager(getServletContext());

        if (webManager.isGameWasLoaded()) {
            namesList = gameManager.getHumanNames();
            if (playerName != null) {
                request.getSession(true).setAttribute("playerName", playerName);
                if (namesList.contains(playerName)) {
                    namesList.remove(playerName);
                }
            }

            try (PrintWriter out = response.getWriter()) {
                Gson gson = new Gson();
                String jsonResponse = gson.toJson(namesList);
                out.print(jsonResponse);
                out.flush();
            }
        } else {
            PrintWriter writer = response.getWriter();
            boolean duplicateName = gameManager.isDuplicateName(playerName);
            //TODO: do a duplicate name error
            if (duplicateName) {
            writer.print(DUPLICATE_NAME);
            response.setStatus(500);
            }
            else if(webManager.gameIsReady()) {
                writer.print(USER_FAIL);
            response.setStatus(500);
            }
            else {
                gameManager.addPlayerToList(playerName);
                request.getSession(true).setAttribute("playerName", playerName);
                response.getWriter().write("WaitingRoom.html");
            }
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
