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

@WebServlet(name = "LogInServlet", urlPatterns = {"/registration/login"})
public class LoginServlet extends HttpServlet {
    private final String GAMES_ROOM_URL = "../pages/gamesRoom/gamesRoom.html";
    private final String SIGN_UP_URL = "../pages/signup/signup.html";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String usernameFromSession = SessionUtils.getSessionUsername(request);
        String usernameFromParameter = request.getParameter(Constants.USERNAME);

        UserManager userManager = ServletUtils.getUserManager(getServletContext());

        if (usernameFromSession == null) {  //user hasn't signedup yet
            if (usernameFromParameter == null) { //no username in session and no username in parameter
                response.setHeader(Constants.USERNAME_ERROR, "no username in request or session");
                response.sendRedirect(SIGN_UP_URL);
            } else { // signup
                //normalize the username value
                usernameFromParameter = usernameFromParameter.trim();
                if (userManager.isUserExists(new User(usernameFromParameter, null))) {
                    usernameExists(request, response, usernameFromParameter);
                } else {
                    signupUser(request, response, usernameFromParameter, userManager);
                }
            }
        } else {
            // user already signedup
            if (usernameFromParameter.equals(usernameFromSession)) {
                request.setAttribute(Constants.USERNAME_ALERT, "Username already exists. Redirecting to page.");
            } else { // resign user using different name
                resignUser(request, usernameFromSession, usernameFromParameter, userManager);
            }

            response.sendRedirect(GAMES_ROOM_URL);
        }
    }

    private void usernameExists(HttpServletRequest request, HttpServletResponse response, String usernameFromParameter) throws IOException {
        String alertMessage = "Username " + usernameFromParameter + " already exists. Redirecting to page.";
        request.setAttribute(Constants.USERNAME_ALERT, alertMessage);
        response.sendRedirect(GAMES_ROOM_URL);
    }

    private void resignUser(HttpServletRequest request, String usernameFromSession, String usernameFromParameter, UserManager userManager) {
        request.getSession(true).setAttribute(Constants.USERNAME, usernameFromParameter);
        userManager.removeUser(usernameFromSession);
        request.setAttribute(Constants.USERNAME_ALERT, "username already signedup, resigning");
    }

    private void signupUser(HttpServletRequest request, HttpServletResponse response, String usernameFromParameter, UserManager userManager) throws IOException {
        //add the new user to the users list
        userManager.addUser(usernameFromParameter, null);
        request.getSession(true).setAttribute(Constants.USERNAME, usernameFromParameter);
        //redirect the request to the chat room - in order to actually change the URL
        System.out.println("On login, request URI is: " + request.getRequestURI());
        response.sendRedirect(GAMES_ROOM_URL);
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
