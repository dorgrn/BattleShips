package com.BattleShipsWebApp.registration.servlets;

import com.BattleShipsWebApp.constants.Constants;
import com.BattleShipsWebApp.registration.users.User;
import com.BattleShipsWebApp.registration.users.UserManager;
import com.BattleShipsWebApp.utils.ServletUtils;
import com.BattleShipsWebApp.utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LogOutServlet", urlPatterns = {"/registration/logout"})
public class LogoutServlet extends HttpServlet {

    private final String SIGN_UP_URL = "/pages/signup/signup.html";


    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String usernameFromSession = SessionUtils.getSessionUsername(request);
        String callerUri = request.getParameter("CALLER_URI");
        String usernameFromParameter = request.getParameter(Constants.USERNAME);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());

        if (usernameFromSession == null || usernameFromParameter == null) {
            response.setHeader(Constants.USERNAME_ERROR, "User doesn't exist on logout.");
            response.sendRedirect(callerUri);
        }

        if (!userManager.isUserExists(new User(usernameFromParameter))) { // user already exists - show user's gamesRoom (bonus)
            response.setHeader(Constants.USERNAME_ERROR, "user name isn't in database");
            response.sendRedirect(callerUri);
            // getServletContext().getRequestDispatcher(LOGIN_ERROR_URL).forward(request, response);
        } else { // username is found
            // remove user
            userManager.removeUser(usernameFromParameter);
            System.out.println("On logout, request URI is: " + request.getRequestURI());
            response.sendRedirect(SIGN_UP_URL);
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
