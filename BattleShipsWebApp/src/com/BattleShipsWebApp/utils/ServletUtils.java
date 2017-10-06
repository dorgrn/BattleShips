package com.BattleShipsWebApp.utils;


import com.BattleShipsWebApp.mainGamesRoom.gameConfigsManager.GameRecordsManager;
import com.BattleShipsWebApp.registration.users.UserManager;

import javax.servlet.ServletContext;

public class ServletUtils {

    private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
    private static final String CHAT_MANAGER_ATTRIBUTE_NAME = "chatManager";
    private static final String XML_MANAGER_ATTRIBUTE_NAME = "XMLFilesManager";
    private static final String GAME_CONFIG_ATTRIBUTE_NAME = "gameConfigsManager";
    private ServletContext servletContext;

    public static UserManager getUserManager(ServletContext servletContext) {
        if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
            servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
        }
        return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
    }

    public static GameRecordsManager getGameConfigManager(ServletContext servletContext){
        if (servletContext.getAttribute(GAME_CONFIG_ATTRIBUTE_NAME) == null) {
            servletContext.setAttribute(GAME_CONFIG_ATTRIBUTE_NAME, new GameRecordsManager());
        }
        return (GameRecordsManager) servletContext.getAttribute(GAME_CONFIG_ATTRIBUTE_NAME);
    }
}