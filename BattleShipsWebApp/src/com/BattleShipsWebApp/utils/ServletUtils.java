package com.BattleShipsWebApp.utils;


import com.BattleShipsWebApp.mainGamesRoom.gameFilesManager.GameFilesManager;
import com.BattleShipsWebApp.registration.users.UserManager;

import javax.servlet.ServletContext;

public class ServletUtils {

    private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
    private static final String CHAT_MANAGER_ATTRIBUTE_NAME = "chatManager";
    private static final String GAME_FILES_ATTRIBUTE_NAME = "gameFilesManager";
    private ServletContext servletContext;

    public static UserManager getUserManager(ServletContext servletContext) {
        if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
            servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
        }
        return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
    }

    public static GameFilesManager getGameFilesManager(ServletContext servletContext){
        if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
            servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
        }
        return (GameFilesManager) servletContext.getAttribute(GAME_FILES_ATTRIBUTE_NAME);
    }
}

//
//    public static int getIntParameter(HttpServletRequest request, String name) {
//	String value = request.getParameter(name);
//	if (value != null) {
//	    try {
//		return Integer.parseInt(value);
//	    } catch (NumberFormatException numberFormatException) {
//	    }
//	}
//	return INT_PARAMETER_ERROR;
//    }
