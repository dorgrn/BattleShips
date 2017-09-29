package registration.servlets;

import registration.constants.Constants;
import registration.users.User;
import registration.utils.ServletUtils;
import registration.utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static registration.constants.Constants.USERNAME;

@WebServlet(name = "LogInServlet", urlPatterns = {"/registration/login"})
public class LoginServlet extends HttpServlet {

    // urls that starts with forward slash '/' are considered absolute
    // urls that doesn't start with forward slash '/' are considered relative to the place where this servlet request comes from
    // you can use absolute paths, but then you need to build them from scratch, starting from the context path
    // ( can be fetched from request.getContextPath() ) and then the 'absolute' path from it.
    // Each method with it's pros and cons...
    private final String GAMES_ROOM_URL = "../pages/gamesRoom/gamesRoom.html";
    private final String SIGN_UP_URL = "../pages/welcome/welcome.html";

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
        String usernameFromSession = SessionUtils.getUsername(request);
        PrintWriter out = response.getWriter();

        registration.users.UserManager userManager = ServletUtils.getUserManager(getServletContext());
        if (usernameFromSession == null) {
            //user is not logged in yet
            String usernameFromParameter = request.getParameter(USERNAME);
            if (usernameFromParameter == null) {
                //no username in session and no username in parameter -
                //redirect back to the index page
                //this return an HTTP code back to the browser telling it to load
                response.sendRedirect(SIGN_UP_URL);
            } else {
                //normalize the username value
                usernameFromParameter = usernameFromParameter.trim();
                if (userManager.isUserExists(new User(usernameFromParameter))) {
                    String errorMessage = "Username " + usernameFromParameter + " already exists. Please enter a different username.";
                    // username already exists, forward the request back to index.jsp
                    // with a parameter that indicates that an error should be displayed
                    // the request dispatcher obtained from the servlet context is one that MUST get an absolute path (starting with'/')
                    // and is relative to the web app root
                    // see this link for more details:
                    // http://timjansen.github.io/jarfiller/guide/servlet25/requestdispatcher.xhtml
                    request.setAttribute(Constants.USER_NAME_ERROR, errorMessage);
                    // getServletContext().getRequestDispatcher(LOGIN_ERROR_URL).forward(request, response);
                } else {
                    //add the new user to the users list
                    userManager.addUser(usernameFromParameter);
                    //set the username in a session so it will be available on each request
                    //the true parameter means that if a session object does not exists yet
                    //create a new one
                    request.getSession(true).setAttribute(USERNAME, usernameFromParameter);

                    //redirect the request to the chat room - in order to actually change the URL
                    System.out.println("On login, request URI is: " + request.getRequestURI());
                    response.sendRedirect(GAMES_ROOM_URL);
                }
            }
        } else {
            // user already exists - show user's gamesRoom
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
