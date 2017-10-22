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
        //System.out.println(gameName);
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }


    public static void clearSession(HttpServletRequest request) {
        request.getSession().invalidate();
    }
}