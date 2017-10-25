package com.BattleShipsWebApp.game.servlets;

import BattleShipsEngine.engine.Game;
import BattleShipsEngine.engine.Player;
import BattleShipsEngine.engine.Point;
import com.BattleShipsWebApp.constants.Constants;
import com.BattleShipsWebApp.mainGamesRoom.gameRecordsManager.GameRecord;
import com.BattleShipsWebApp.mainGamesRoom.gameRecordsManager.GameRecordsManager;
import com.BattleShipsWebApp.mainGamesRoom.gameRecordsManager.GameStatus;
import com.BattleShipsWebApp.utils.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Array;


@WebServlet(name = "PlaceMineServlet", urlPatterns = {"/gamesRoom/placeMine"})
public class PlaceMineServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // needed params
        final String gameNameParameter = request.getParameter(Constants.GAME_NAME_ATTRIBUTE_NAME);
        final String buttonIDParameter = String.valueOf(request.getParameter(Constants.BUTTON_ID_ATTRIBUTE_NAME));
        final GameRecordsManager gameRecordsManager = ServletUtils.getGameRecordsManager(getServletContext());

        GameRecord gameRecord = gameRecordsManager.getGameByName(gameNameParameter);

        if (gameRecord != null) {
            String[] valuesArr = buttonIDParameter.split(" ");
            gameRecord.placeMine(new Point(Integer.parseInt(valuesArr[0]), Integer.parseInt(valuesArr[1])));
            gameRecord.swapPlayerTurn();
        }
        else {
            throw new ServletException("Game not found!");
        }

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

