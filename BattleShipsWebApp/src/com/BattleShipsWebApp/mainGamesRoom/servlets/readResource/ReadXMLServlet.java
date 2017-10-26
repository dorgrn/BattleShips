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

import javax.servlet.RequestDispatcher;
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
import java.io.PrintWriter;
import java.util.Scanner;

@WebServlet(name = "ReadXMLServlet", urlPatterns = {"/readResource/readxml"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class ReadXMLServlet extends HttpServlet {
    private static final String DIRECTORY_NAME = "temp";
    private static final String FILE_NAME_START = "TEMP_";
    private int tempSaveCounter = 0;

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
        response.setContentType("text/html;charset=UTF-8");

        final String gameName = request.getParameter(Constants.GAME_NAME_ATTRIBUTE_NAME);
        final String creatorName = SessionUtils.getSessionUsername(request);
        final Part filePart = request.getPart("file"); // Retrieves <input type="file" name="file">
        final String fileName = getSubmittedFileName(filePart);

        try {
            InputStream fileContent = filePart.getInputStream();
            File outputTempFile = InputFileUtils.inputStreamToFile(fileContent, DIRECTORY_NAME, FILE_NAME_START + tempSaveCounter++, "xml");

            GameConfig gameConfig = new GameConfig();
            gameConfig.load(outputTempFile);
            Game game = gameConfig.initiateGameFromGenerated();

            processGameRecord(gameName, creatorName, game);

            request.getSession().setAttribute(Constants.GAME_NAME_ATTRIBUTE_NAME, gameName);
            request.getSession().setAttribute(Constants.PLAYER_TYPE_ATTRIBUTE, Player.Type.PLAYER_ONE);
            System.out.println("GameRecord inserted successfully");

            response.sendRedirect(Constants.GAME_URI);
            //DEBUG: System.out.println("The game " + gameName + " was saved on session " + request.getSession().toString());
        } catch (RecordAlreadyExistsException e) {
            // player is already in game, transfer to its game
            //request.getSession().setAttribute(Constants.GAME_NAME_ATTRIBUTE_NAME, gameName);
            response.sendRedirect(Constants.DUPLICATE_GAME_URI);

        } catch (JAXBException | ConfigException e) {
            // error in parsing file
            handleErrorInParsingFile(request, response, fileName, e);
        }
    }

    private void handleErrorInParsingFile(HttpServletRequest request, HttpServletResponse response, String fileName, Exception e) throws ServletException, IOException {
        response.setContentType("text/html");
        try (PrintWriter out = response.getWriter()){
            out.format("<script>document.getElementById('errorPlaceHolder').innerHTML='Couldn't parse file %s. %s'</script>", fileName, e.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher(Constants.GAME_ROOM_URI);
            dispatcher.include(request, response);
        }
    }

    private void processGameRecord(String gameName, String creatorName, Game game) throws RecordAlreadyExistsException {
        GameRecordsManager gameRecordsManager = ServletUtils.getGameRecordsManager(getServletContext());

        GameRecord gameRecord = createGameRecord(gameName, creatorName, game);
        gameRecordsManager.addGameRecord(gameRecord);
    }

    private GameRecord createGameRecord(String gameName, String creatorName, Game game) throws RecordAlreadyExistsException {
        GameRecord gameRecord = new GameRecord(gameName, creatorName, game);
        gameRecord.setGameStatus(GameStatus.ONE_PLAYER);  // creator - first player

        return gameRecord;
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
     * @param request  servlets request
     * @param response servlets response
     * @throws ServletException if a servlets-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // only POST should be used
        doPost(request, response);
    }


}
