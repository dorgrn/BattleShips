package com.BattleShipsWebApp.utils;


import com.BattleShipsWebApp.constants.Constants;
import com.BattleShipsWebApp.game.chat.chatManagement.ChatManager;
import com.BattleShipsWebApp.mainGamesRoom.gameRecordsManager.GameRecordsManager;
import com.BattleShipsWebApp.registration.users.UserManager;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

public class ServletUtils {

    private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
    private static final String CHAT_MANAGER_ATTRIBUTE_NAME = "chatManager";
    private static final String XML_MANAGER_ATTRIBUTE_NAME = "XMLFilesManager";
    private static final String GAME_RECORDS_ATTRIBUTE_NAME = "gameRecordsManager";

    private ServletContext servletContext;

    public static UserManager getUserManager(ServletContext servletContext) {
        //System.out.println("servlets context is: " + servletContext.toString());

        if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
            servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
        }
        return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
    }

    public static GameRecordsManager getGameRecordsManager(ServletContext servletContext){
        if (servletContext.getAttribute(GAME_RECORDS_ATTRIBUTE_NAME) == null) {
            servletContext.setAttribute(GAME_RECORDS_ATTRIBUTE_NAME, new GameRecordsManager());
        }
        return (GameRecordsManager) servletContext.getAttribute(GAME_RECORDS_ATTRIBUTE_NAME);
    }

    public static ChatManager getChatManager(ServletContext servletContext) {
        if (servletContext.getAttribute(CHAT_MANAGER_ATTRIBUTE_NAME) == null) {
            servletContext.setAttribute(CHAT_MANAGER_ATTRIBUTE_NAME, new GameRecordsManager());
        }
        return (ChatManager) servletContext.getAttribute(CHAT_MANAGER_ATTRIBUTE_NAME);
    }

    public static int getIntParameter(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException ignored) {
            }
        }
        return Constants.INT_PARAMETER_ERROR;
    }
}