package com.BattleShipsWebApp.game.chat.servlets;


import com.BattleShipsWebApp.constants.Constants;
import com.BattleShipsWebApp.game.chat.chatManagement.ChatManager;
import com.BattleShipsWebApp.game.chat.chatManagement.SingleChatEntry;
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
import java.util.List;

@WebServlet(name = "GetChatMessagesServlet", urlPatterns = {"/game/chat/getMessages"})
public class GetChatMessagesServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        ChatManager chatManager = ServletUtils.getChatManager(getServletContext());
        String username = SessionUtils.getSessionUsername(request);
        if (username == null) {
            System.out.println("Username error in chat");
            return;
        }

        int chatVersion = ServletUtils.getIntParameter(request, Constants.CHAT_VERSION_PARAMETER);
        logServerMessage("Server Chat version: " + chatManager.getVersion() + ", User '" + username + "' Chat version: " + chatVersion);


        try (PrintWriter out = response.getWriter()) {
            List<SingleChatEntry> chatEntries = chatManager.getChatEntries(chatVersion);
            ChatAndVersion cav = new ChatAndVersion(chatEntries, chatManager.getVersion());
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(cav);
            logServerMessage(jsonResponse);
            out.print(jsonResponse);
            out.flush();

        }
    }

    private void logServerMessage(String message) {
        System.out.println(message);
    }

    class ChatAndVersion {

        final private List<SingleChatEntry> entries;
        final private int version;

        public ChatAndVersion(List<SingleChatEntry> entries, int version) {
            this.entries = entries;
            this.version = version;
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
