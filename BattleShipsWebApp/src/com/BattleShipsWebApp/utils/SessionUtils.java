package com.BattleShipsWebApp.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.BattleShipsWebApp.constants.Constants.USERNAME;

public class SessionUtils {

    public static String getUsername (HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(USERNAME) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static void clearSession (HttpServletRequest request) {
        request.getSession().invalidate();
    }
}