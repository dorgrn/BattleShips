package com.BattleShipsWebApp.utils;

import com.BattleShipsWebApp.constants.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.BattleShipsWebApp.constants.Constants.USERNAME;

public class SessionUtils {

    public static String getSessionUsername (HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(Constants.USERNAME) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }


    public static String getSessionGameName (HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(Constants.CURRENT_GAME_ATTRIBUTE_NAME) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static void clearSession (HttpServletRequest request) {
        request.getSession().invalidate();
    }
}