package com.BattleShipsWebApp.mainGamesRoom.servlets.users;

import com.BattleShipsWebApp.utils.SessionUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

    @WebServlet(name = "GetCurrentUserServlet", urlPatterns = {"/gamesRoom/currentUser"})
    public class GetCurrentUserServlet extends HttpServlet {
        protected void processRequest(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {

            response.setContentType("application/json");

            try (PrintWriter out = response.getWriter()){
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("username", SessionUtils.getUsername(request));
                out.println(jsonObject);
                out.flush();
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
