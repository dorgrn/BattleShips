package com.BattleShipsWebApp.utils;

import com.BattleShipsWebApp.constants.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtils {
    public static String getSessionUsername(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(Constants.USERNAME) : null;
        System.out.println(sessionAttribute);
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static String getSessionUserType(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(Constants.PLAYER_TYPE_ATTRIBUTE) : null;
        System.out.println(sessionAttribute);
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }


    public static String getSessionGameName(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        Object sessionAttribute = session != null ? session.getAttribute(Constants.GAME_NAME_ATTRIBUTE_NAME) : null;
        //DEBUG
        System.out.println("Session id is: " + session.getId());
        //System.out.println(gameName);
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

//    public static GameRecordsManager getSessionGameRecordsManager(HttpServletRequest request) {
//        HttpSession session = request.getSession(false);
//
//        if (session == null) {
//            return null;
//        }
//
//        Object sessionAttribute = session.getAttribute(Constants.SESSION_GAME_RECORDS_ATTRIBUTE_NAME);
//
//        if (sessionAttribute == null) {
//            request.setAttribute(Constants.SESSION_GAME_RECORDS_ATTRIBUTE_NAME, new GameRecordsManager());
//        }
//
//        return (GameRecordsManager) request.getAttribute(Constants.SESSION_GAME_RECORDS_ATTRIBUTE_NAME);
//    }


    public static void clearSession(HttpServletRequest request) {
        request.getSession().invalidate();
    }
}