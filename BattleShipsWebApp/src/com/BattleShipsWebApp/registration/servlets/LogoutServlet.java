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
import java.io.PrintWriter;

@WebServlet(name = "LogOutServlet", urlPatterns = {"/registration/logout"})
public class LogoutServlet extends HttpServlet{

    private final String SIGN_UP_URL = "../pages/welcome/signup.html";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");


        String usernameFromSession = SessionUtils.getUsername(request);
        String callerUri = request.getParameter("CALLER_URI");
        PrintWriter out = response.getWriter();


        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        if (usernameFromSession != null) {
            String usernameFromParameter = request.getParameter(Constants.USERNAME);
            if (usernameFromParameter == null) {
                //no username in session and no username in parameter -
                //redirect back to the index page
                //this return an HTTP code back to the browser telling it to load
                response.sendRedirect(callerUri);
            } else {
                //normalize the username value
                usernameFromParameter = usernameFromParameter.trim();
                if (!userManager.isUserExists(new User(usernameFromParameter))) {
                    String errorMessage = "Username " + usernameFromParameter + " doesn't exist.";
                    request.setAttribute(Constants.USER_NAME_ERROR, errorMessage);
                    // getServletContext().getRequestDispatcher(LOGIN_ERROR_URL).forward(request, response);
                } else {
                    // remove user
                    userManager.removeUser(usernameFromParameter);
                    //set the username in a session so it will be available on each request
                    //the true parameter means that if a session object does not exists yet
                    //create a new one
                    request.getSession(true).setAttribute(Constants.USERNAME, "");

                    //redirect the request to the chat room - in order to actually change the URL
                    System.out.println("On login, request URI is: " + request.getRequestURI());
                    response.sendRedirect(SIGN_UP_URL);
                }
            }
        } else {
            // user already exists - show user's gamesRoom TODO: improve to handle bonus
            out.println("<script type=\"text/javascript\">");
            out.println("alert('Username or password incorrect');");
            out.println("location='" + SIGN_UP_URL + "';");
            out.println("</script>");
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
