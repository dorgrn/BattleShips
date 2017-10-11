package com.BattleShipsWebApp.mainGamesRoom.servlets.gamesManagement;

import com.BattleShipsWebApp.constants.Constants;
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
import java.io.PrintWriter;


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

        User user = userManager.getUser(usernameFromParameter);
        GameRecord gameRecord = gameRecordsManager.getGameByName(gameName);

        PrintWriter out = response.getWriter();
        try {

            if (userRole.equals(Constants.USER_PARTICIPANT)) {
                gameRecordsManager.addParticipantToGame(user, gameRecord);
            } else if (userRole.equals(Constants.USER_WATCHER)) {
                gameRecordsManager.addWatcherToGame(user, gameRecord);
            }
            //response.sendRedirect(Constants.GAME_URI);
            response.addHeader(Constants.REDIRECT_ATTRIBUTE_NAME, Constants.GAME_URI);

        } catch (RecordAlreadyExistsException e) {
            System.err.println(e.getMessage());
            response.setStatus(400);
            response.setHeader(Constants.ERROR_ATTRIBUTE_NAME,"You are already signed up for this game!");
        } finally {
            if (out != null) {
                out.close();
            }
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
