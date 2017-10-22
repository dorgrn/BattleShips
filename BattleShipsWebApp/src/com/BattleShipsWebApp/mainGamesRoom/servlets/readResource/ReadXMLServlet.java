package com.BattleShipsWebApp.mainGamesRoom.servlets.readResource;

import BattleShipsEngine.engine.ConfigException;
import BattleShipsEngine.engine.Game;
import BattleShipsEngine.engine.GameConfig;
import BattleShipsEngine.engine.Player;
import com.BattleShipsWebApp.constants.Constants;
import com.BattleShipsWebApp.exceptions.RecordAlreadyExistsException;
import com.BattleShipsWebApp.mainGamesRoom.gameRecordsManager.GameRecord;
import com.BattleShipsWebApp.mainGamesRoom.gameRecordsManager.GameRecordsManager;
import com.BattleShipsWebApp.mainGamesRoom.gameRecordsManager.GameStatus;
import com.BattleShipsWebApp.utils.InputFileUtils;
import com.BattleShipsWebApp.utils.ServletUtils;
import com.BattleShipsWebApp.utils.SessionUtils;
import com.google.gson.Gson;
import sun.security.krb5.Config;

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
    private int tempSaveCounter = 0;
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
        final String creatorName = SessionUtils.getSessionUsername(request);
        final Part filePart = request.getPart("file"); // Retrieves <input type="file" name="file">
        final String fileName = getSubmittedFileName(filePart);

        InputStream fileContent = filePart.getInputStream();
        File outputTempFile = InputFileUtils.inputStreamToFile(fileContent, "temp", "TEMP_" + tempSaveCounter++, "xml");

        GameConfig gameConfig = new GameConfig();
        try {
            gameConfig.load(outputTempFile);
            Game game = gameConfig.initiateGameFromGenerated();


            GameRecordsManager gameRecordsManager = ServletUtils.getGameRecordsManager(getServletContext());
            // is creator
            GameRecord gameRecord = createGameRecord(gameName, creatorName, Player.Type.PLAYER_ONE, game);
            //gameRecord.addParticipant(gameRecord.getCreator());
            gameRecordsManager.addGameRecord(gameRecord);

            // add game record to session
            String gson = new Gson().toJson(gameRecord);
            //System.out.println(gson);

            request.getSession().setAttribute(Constants.SESSION_SAVED_GAME, gson);
            request.getSession().setAttribute(Constants.PLAYER_TYPE_ATTRIBUTE, Player.Type.PLAYER_ONE);
            //DEBUG: System.out.println("Config inserted successfully");

            response.sendRedirect(Constants.GAME_URI);
            //DEBUG: System.out.println("The game " + gameName + " was saved on session " + request.getSession().toString());

        } catch (RecordAlreadyExistsException | ConfigException e) {
            System.err.println(e.getMessage());
            response.setHeader(e.getClass().getSimpleName(), gameName);
        } catch (JAXBException e) {
            e.printStackTrace();
            response.setHeader("JAXBException", gameName);
        }
    }

    private GameRecord createGameRecord(String gameName, String creatorName, Player.Type playerType, Game game) throws RecordAlreadyExistsException {
        GameRecord gameRecord = new GameRecord(gameName, creatorName, game);
        gameRecord.setGameStatus(GameStatus.ONE_PLAYER);

        return gameRecord;
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


    private static String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
    }

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


}
