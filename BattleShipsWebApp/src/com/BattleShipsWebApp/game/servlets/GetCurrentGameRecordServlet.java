package com.BattleShipsWebApp.game.servlets;

import com.BattleShipsWebApp.mainGamesRoom.gameRecordsManager.GameRecordsManager;
import com.BattleShipsWebApp.utils.ServletUtils;
import com.BattleShipsWebApp.utils.SessionUtils;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "GetCurrentRecordGameServlet", urlPatterns = {"/game/currentGameRecord"})
public class GetCurrentGameRecordServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try (PrintWriter out = response.getWriter()) {
            response.setContentType("application/json");
            final String gameName = SessionUtils.getSessionGameName(request);
            GameRecordsManager gameRecordsManager = ServletUtils.getGameRecordsManager(getServletContext());

            final String gameJson = new Gson().toJson(gameRecordsManager.getGameByName(gameName));

            out.println(gameJson);
            out.flush();

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

