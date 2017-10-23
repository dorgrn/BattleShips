package com.BattleShipsWebApp.mainGamesRoom.servlets.readResource;

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


@WebServlet(name = "ErrorInFileInputServlet", urlPatterns = {"/gamesRoom/errorInFileInput"})
public class ErrorInFileInputServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // needed params
        final String gameNameParameter = request.getParameter(Constants.GAME_NAME_ATTRIBUTE_NAME);
        final GameStatus gameStatusParameter =
                GameStatus.valueOf(request.getParameter(Constants.GAME_STATUS_ATTRIBUTE_NAME));

        final GameRecordsManager gameRecordsManager = ServletUtils.getGameRecordsManager(getServletContext());

        GameRecord gameRecord = gameRecordsManager.getGameByName(gameNameParameter);

        if (gameRecord != null) {
            gameRecord.setGameStatus(gameStatusParameter);
        } else {
            throw new ServletException("Game not found!");
        }

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

