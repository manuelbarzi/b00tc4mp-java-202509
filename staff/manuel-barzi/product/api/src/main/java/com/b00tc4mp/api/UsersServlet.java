package com.b00tc4mp.api;

import com.b00tc4mp.data.Data;
import com.b00tc4mp.logic.Logic;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "UsersServlet", urlPatterns = "/hello")
public class UsersServlet extends HttpServlet {

    private static final Gson gson = new Gson();
    private Data data; // Will be initialized in init()
    private Logic logic;

    @Override
    public void init() throws ServletException {
        super.init();
        this.data = Data.get(); // Get singleton instance (with preloaded "pepito")
        this.logic = Logic.get();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PrintWriter out = response.getWriter();

        // Read JSON body
        String jsonInput = readRequestBody(request);
        if (jsonInput == null || jsonInput.trim().isEmpty()) {
            sendError(response, out, HttpServletResponse.SC_BAD_REQUEST, Exception.class.getSimpleName(), "Empty request body");
            return;
        }

        // Parse JSON
        JsonObject json;
        String name;
        String username;
        String password;
        String confirmPassword;

        try {
            json = gson.fromJson(jsonInput, JsonObject.class);

            name = json.get("name").getAsString().trim();
            username = json.get("username").getAsString().trim();
            password = json.get("password").getAsString();
            confirmPassword = json.get("confirmPassword").getAsString();
        } catch (JsonSyntaxException e) {
            sendError(response, out, HttpServletResponse.SC_BAD_REQUEST, e.getClass().getSimpleName(), "Invalid JSON format");
            return;
        } catch (NullPointerException e) {
            sendError(response, out, HttpServletResponse.SC_BAD_REQUEST, e.getClass().getSimpleName(), "Missing fields in JSON");
            return;
        }

        try {
            logic.registerUser(name, username, password, confirmPassword);

            // Success response
            response.setStatus(HttpServletResponse.SC_CREATED);
            out.flush();
        } catch (Exception e) {
            sendError(response, out, HttpServletResponse.SC_BAD_REQUEST, e.getClass().getSimpleName(), e.getMessage());
        }
    }

    // Helper: Read full request body
    private String readRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();

        try (BufferedReader reader = request.getReader()) {
            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        return sb.toString();
    }

    // Helper: Send JSON error
    private void sendError(HttpServletResponse response, PrintWriter out, int status, String error, String message) {
        response.setStatus(status);
        response.setCharacterEncoding("UTF-8");

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("error", error);
        jsonObject.addProperty("message", message);

        out.print(gson.toJson(jsonObject));
        out.flush();
    }
}
