package com.BattleShipsWebApp.mainGamesRoom.servlets.readResource;

import BattleShipsEngine.engine.ConfigException;
import BattleShipsEngine.engine.GameConfig;
import com.BattleShipsWebApp.constants.Constants;
import com.BattleShipsWebApp.exceptions.RecordAlreadyExistsException;
import com.BattleShipsWebApp.mainGamesRoom.gameRecordsManager.GameRecord;
import com.BattleShipsWebApp.mainGamesRoom.gameRecordsManager.GameRecordsManager;
import com.BattleShipsWebApp.utils.InputFileUtils;
import com.BattleShipsWebApp.utils.ServletUtils;
import com.BattleShipsWebApp.utils.SessionUtils;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Scanner;

@WebServlet(name = "ReadXMLServlet", urlPatterns = {"/readResource/readxml"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class ReadXMLServlet extends HttpServlet {
    private static final String gamesRoomURI = "/pages/gamesRoom/gamesRoom.html";
    private int tempSaveCounter = 0;


    private static String getSubmittedFileName(Part part) {
        for ( String cd : part.getHeader("content-disposition").split(";") ) {
            if (cd.trim().startsWith("filename")) {
                String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return fileName.substring(fileName.lastIndexOf('/') + 1).substring(fileName.lastIndexOf('\\') + 1); // MSIE fix.
            }
        }
        return null;
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
        // only POST should be used
        doPost(request, response);
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
        response.setContentType("text/html;charset=UTF-8");

        final String gameName = request.getParameter(Constants.GAME_NAME_ATTRIBUTE_NAME);
        final String creatorName = SessionUtils.getUsername(request);
        final Part filePart = request.getPart("file"); // Retrieves <input type="file" name="file">
        final String fileName = getSubmittedFileName(filePart);
        boolean success = false;
        InputStream fileContent = filePart.getInputStream();
        File outputTempFile = InputFileUtils.inputStreamToFile(fileContent, "temp", "TEMP_" + tempSaveCounter++, "xml");

        GameConfig gameConfig = new GameConfig();
        try {
            gameConfig.load(outputTempFile);
            GameRecordsManager gameRecordsManager = ServletUtils.getGameRecordsManager(getServletContext());
            GameRecord gameRecord = new GameRecord(gameName, creatorName, gameConfig);
            gameRecordsManager.addGameRecord(gameRecord);

            // add game record to session
            request.getSession().setAttribute(Constants.SESSION_SAVED_GAMES, new Gson().toJson(gameRecord));
            System.out.println("Config inserted successfully");

            /* TODO: 07-Oct-17 redirect after upload...
            success = true;
            response.sendRedirect(Constants.GAME_URI);
            */
        } catch (RecordAlreadyExistsException e) {
            System.err.println(e.getMessage());
            response.setHeader("RecordAlreadyExistsException", gameName);
        } catch (ConfigException e) {
            System.err.println(e.getMessage());
            response.setHeader("ConfigException", gameName);
        } catch (JAXBException e) {
            e.printStackTrace();
            response.setHeader("JAXBException", gameName);
        }

        if (!success) {
            response.sendRedirect(gamesRoomURI);
        }
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


    private String getAbsolutePathOfResource(String resource) {
        URL url = this.getClass().getResource(resource);
        return url != null ? url.getPath() : "?";
    }

    private String getSubmittedFile(HttpServletRequest request) throws IOException, ServletException {
        StringBuilder fileContent = new StringBuilder();

        Collection<Part> parts = request.getParts();

        for ( Part part : parts ) {
            //to write the content of the file to an actual file in the system (will be created at c:\samplefile)
            part.write("samplefile");

            //to write the content of the file to a string
            fileContent.append(readFromInputStream(part.getInputStream()));
        }

        return fileContent.toString();
    }

    private String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
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


}
