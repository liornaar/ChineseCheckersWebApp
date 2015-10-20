/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import static java.lang.System.out;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import logic.Player;
import logic.XMLGameData;
import logic.XMLHandling;
import manager.GameManager;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import utils.ServletUtils;
import webLogic.WebManager;

/**
 *
 * @author user
 */
@WebServlet(name = "loadGameServlet", urlPatterns = {"/loadGame"})
@MultipartConfig
public class loadGameServlet extends HttpServlet {

    private XMLGameData xmlData;
    
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
        
        if(webManager.isInGameSettings()) {
            response.sendRedirect("index.html");
            return;
        }
        
        String humanPlayer = null;
        
        webManager.setWaitingForPlayers(true);
        
    Part file = request.getPart("loadFile"); 
    InputStream xmlStream = file.getInputStream();
    InputStream xsdChecker = file.getInputStream();
    
    
    try {
        XMLHandling handler = new XMLHandling();
        xmlData = handler.getGameDataFromXML(xmlStream);
        gameManager.initGameFromXML(xmlData);
        webManager.setGameWasLoaded(true);
        webManager.setNumberOfPlayersToJoin(xmlData.getNumberOfHumanPlayers());
        if(xmlData.getNumberOfHumanPlayers() == 1) {
            for(Player player : xmlData.getPlayers()) {
                if(player.isHuman()) {
                   humanPlayer  = player.getName();
                }
            }
            request.getSession(true).setAttribute("playerName", humanPlayer);
            webManager.setInGame(true);
            response.sendRedirect("game.html");
            
        }
        else {
            webManager.setWaitingForPlayers(true);
            response.sendRedirect("loadGamePlayersList.html");
            
        }
        
    } catch(Exception e) {
        out.println(e.getMessage());
        response.sendRedirect("htmlErrorXML.html");
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
