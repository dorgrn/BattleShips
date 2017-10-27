package com.BattleShipsWebApp.utils;

import BattleShipsEngine.engine.Player;
import com.BattleShipsWebApp.constants.Constants;
import com.BattleShipsWebApp.mainGamesRoom.gameRecordsManager.GameRecord;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static java.lang.Integer.parseInt;

public class SessionUtils {
    public static String getSessionUsername(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(Constants.USERNAME) : null;
        //System.out.println(sessionAttribute);
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static Player.Type getSessionPlayerType(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(Constants.PLAYER_TYPE_ATTRIBUTE) : null;
        //System.out.println(sessionAttribute);
        return sessionAttribute != null ? Player.Type.valueOf(sessionAttribute.toString()) : null;
    }


    public static String getSessionGameName(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        Object sessionAttribute = session != null ? session.getAttribute(Constants.GAME_NAME_ATTRIBUTE_NAME) : null;
        //DEBUG
        //System.out.println("Session id is: " + session.getId());
        //System.out.println(gameName);
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static boolean isGameVersionCurrent(HttpServletRequest request, GameRecord gameRecord){
        HttpSession session = request.getSession(false);

        return getGameVersion(request) == gameRecord.getVersion();
    }

    public static int getGameVersion(HttpServletRequest request){
        HttpSession session = request.getSession(false);

        Object sessionAttribute = session != null ? session.getAttribute(Constants.GAME_VERSION_ATTRIBUTE_NAME) : null;

        return sessionAttribute != null ? Integer.parseInt(sessionAttribute.toString()) : 0;
    }

    public static void increaseGameVersion(HttpServletRequest request){
        HttpSession session = request.getSession(false);

        setSessionAttribute(Constants.GAME_VERSION_ATTRIBUTE_NAME, getGameVersion(request) + 1, request);
    }



    public static void setSessionAttribute(String attribute, Object value,  HttpServletRequest request){
        HttpSession session = request.getSession(false);

        if (session != null){
            session.setAttribute(attribute, value);
        }

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