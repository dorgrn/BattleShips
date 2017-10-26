package com.BattleShipsWebApp.mainGamesRoom.servlets.gamesManagement;

import com.BattleShipsWebApp.constants.Constants;
import com.BattleShipsWebApp.exceptions.GameRecordSizeException;
import com.BattleShipsWebApp.exceptions.RecordAlreadyExistsException;
import com.BattleShipsWebApp.mainGamesRoom.gameRecordsManager.GameRecord;
import com.BattleShipsWebApp.mainGamesRoom.gameRecordsManager.GameRecordsManager;
import com.BattleShipsWebApp.registration.users.User;
import com.BattleShipsWebApp.registration.users.UserManager;
import com.BattleShipsWebApp.utils.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "AddUserServlet", urlPatterns = {"/gamesRoom/gameRecords/addUser"})
public class AddUserServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        UserManager userManager = ServletUtils.getUserManager(request.getServletContext());
        GameRecordsManager gameRecordsManager = ServletUtils.getGameRecordsManager(request.getServletContext());

        // needed params
        final String usernameFromParameter = request.getParameter(Constants.USERNAME_ATTRIBUTE);
        final String gameName = request.getParameter(Constants.GAME_NAME_ATTRIBUTE_NAME);
        final String userRole = request.getParameter(Constants.USER_ROLE_ATTRIBUTE);
        final String playerType = request.getParameter(Constants.PLAYER_TYPE_ATTRIBUTE);

        User user = userManager.getUser(usernameFromParameter);
        GameRecord gameRecord = gameRecordsManager.getGameByName(gameName);

        try {

            if (userRole.equals(Constants.USER_PARTICIPANT)) {
                gameRecordsManager.addParticipantToGame(user, gameRecord);
            } else if (userRole.equals(Constants.USER_WATCHER)) {
                gameRecordsManager.addWatcherToGame(user, gameRecord);
            }

            //response.addHeader(Constants.REDIRECT_ATTRIBUTE_NAME, Constants.GAME_URI);
            setAttributes(request, gameName, userRole, playerType);
            response.sendRedirect(Constants.GAME_URI);

            /*
            //TODO DEBUG
            System.out.println("The game " + gameName + " was saved on user " + user.getUserName() + "'s session! " +
                    "He's a " + userRole);*/

        } catch (RecordAlreadyExistsException | GameRecordSizeException e) {
            setAttributes(request, gameName, userRole, playerType);
        }
    }

    private void setAttributes(HttpServletRequest request, String gameName, String userRole, String playerType) throws IOException {
        //set these attributes to current session
        request.getSession(true).setAttribute(Constants.GAME_NAME_ATTRIBUTE_NAME, gameName);
        // player type and user role are the same as the player
        request.getSession(true).setAttribute(Constants.PLAYER_TYPE_ATTRIBUTE, playerType);
        request.getSession(true).setAttribute(Constants.USER_ROLE_ATTRIBUTE, userRole);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlets request
     * @param response servlets response
     * @throws ServletException if a servlets-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);


    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlets request
     * @param response servlets response
     * @throws ServletException if a servlets-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlets.
     *
     * @return a String containing servlets description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
