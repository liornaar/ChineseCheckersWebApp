/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import javax.servlet.ServletContext;
import manager.GameManager;
import webLogic.WebManager;

/**
 *
 * @author oroze
 */
public class ServletUtils {
    
    private static final String GAME_MANAGER_ATTRIBUTE_NAME = "GameManager";   
    private static final String WEB_MANAGER_ATTRIBUTE_NAME = "WebManager";

    public static GameManager getGameManager(ServletContext servletContext) {
        if (servletContext.getAttribute(GAME_MANAGER_ATTRIBUTE_NAME) == null) {
	    servletContext.setAttribute(GAME_MANAGER_ATTRIBUTE_NAME, new GameManager());
	}
	return (GameManager) servletContext.getAttribute(GAME_MANAGER_ATTRIBUTE_NAME);
    }
    
    public static WebManager getWebManager(ServletContext servletContext) {
        if (servletContext.getAttribute(WEB_MANAGER_ATTRIBUTE_NAME) == null) {
	    servletContext.setAttribute(WEB_MANAGER_ATTRIBUTE_NAME, new WebManager());
	}
	return (WebManager) servletContext.getAttribute(WEB_MANAGER_ATTRIBUTE_NAME);
    }
    
}
