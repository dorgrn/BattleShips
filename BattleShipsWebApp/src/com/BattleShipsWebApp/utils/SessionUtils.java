package com.BattleShipsWebApp.utils;

import com.BattleShipsWebApp.constants.Constants;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtils {

    public static String getSessionUsername (HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(Constants.USERNAME) : null;
        System.out.println(sessionAttribute);
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static String getSessionGameName (HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute("SessionSavedGame") : null;
        String gameName = sessionAttribute.toString().substring(13);
        int finishIndex = gameName.indexOf('"');
        gameName = gameName.substring(0, finishIndex);
        //System.out.println(gameName);
        return gameName;
    }

    public static void clearSession (HttpServletRequest request) {
        request.getSession().invalidate();
    }
}