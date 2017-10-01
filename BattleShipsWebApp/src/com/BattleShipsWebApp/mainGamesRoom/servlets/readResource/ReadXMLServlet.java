package com.BattleShipsWebApp.mainGamesRoom.servlets.readResource;

import com.BattleShipsWebApp.mainGamesRoom.gameFilesManager.GameFilesManager;
import com.BattleShipsWebApp.utils.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

@WebServlet(name = "ReadXMLServlet", urlPatterns = {"/readxml"})
public class ReadXMLServlet extends HttpServlet {
    private static final String FILE_PATH_ATTRIBUTE_NAME = "filePath";
    private static final String FILE_NAME_ATTRIBUTE_NAME = "fileName";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        final String name = request.getParameter(FILE_NAME_ATTRIBUTE_NAME);
        final String filePath = request.getParameter(FILE_PATH_ATTRIBUTE_NAME);


        String absoluteFilename = getAbsolutePathOfResource(filePath);
        String fileContent = getResourceContent(absoluteFilename);

        GameFilesManager gameFilesManager = ServletUtils.getGameFilesManager(getServletContext());

        gameFilesManager.addFile(name, fileContent);

        response.setStatus(4);
    }

    private String getAbsolutePathOfResource(String resource) {
        URL url = this.getClass().getResource(resource);
        return url != null ? url.getPath() : "?";
    }

    private String getResourceContent(String resource) {
        StringBuilder result = new StringBuilder();
        try (InputStream stream = this.getClass().getResourceAsStream(resource)) {
            Scanner scanner = new Scanner(stream, "UTF-8");
            while (scanner.hasNextLine()) {
                result.append(scanner.nextLine()).append("\n\r");
            }
        } catch (Exception exception) {
            return "Error: Failed to read file!";
        }
        return result.toString();
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
