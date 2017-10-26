package com.BattleShipsWebApp.game.servlets;

import BattleShipsEngine.engine.Game;
import BattleShipsEngine.engine.Player;
import com.BattleShipsWebApp.exceptions.GameRecordSizeException;
import com.BattleShipsWebApp.exceptions.RecordDoesNotExistsException;
import com.BattleShipsWebApp.mainGamesRoom.gameRecordsManager.GameRecord;
import com.BattleShipsWebApp.mainGamesRoom.gameRecordsManager.GameRecordsManager;
import com.BattleShipsWebApp.registration.users.User;
import com.BattleShipsWebApp.utils.ServletUtils;
import com.BattleShipsWebApp.utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "RetireFromGameServlet", urlPatterns = {"/game/retireFromGame"})
public class RetireFromGameServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        // needed params
        final String gameNameParameter = SessionUtils.getSessionGameName(request);
        final String userType = SessionUtils.getSessionUserType(request);
        final GameRecordsManager gameRecordsManager = ServletUtils.getGameRecordsManager(getServletContext());


        // get game
        final GameRecord gameRecord = gameRecordsManager.getGameByName(gameNameParameter);
        final Game game = gameRecord.getGame();

        handleRetirement(userType, game);
        User participantUser = new User(SessionUtils.getSessionUsername(request));
        try {
            gameRecord.removeParticipant(participantUser);
        } catch (RecordDoesNotExistsException | GameRecordSizeException e) {
            //TODO: handle
        }


    }

    private void handleRetirement(String userType, Game game) {
        Player player = game.getPlayerByType(Player.Type.valueOf(userType));

        Player enemy = game.getOtherPlayer(player);

        enemy.setHasWon(true);
    }


    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
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
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
