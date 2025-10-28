package com.b00tc4mp.logic;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

import org.json.JSONArray;
import org.json.JSONObject;

import com.b00tc4mp.data.Data;
import com.b00tc4mp.data.UserData;

public class Logic {

    private static Logic instance;

    protected  String userId;

    private Data data;

    private Logic() {
        data = Data.get();
    }

    public static Logic get() {
        if (instance == null) {
            instance = new Logic();
        }

        return instance;
    }

    public void registerUser(String name, String username, String password, String confirmPassword) throws Exception {
        if (name == null || name.isEmpty()) {
            throw new Exception("Name cannot be empty");
        }

        if (username == null || username.isEmpty()) {
            throw new Exception("Username cannot be empty");
        }

        if (password == null || password.isEmpty()) {
            throw new Exception("Password cannot be empty");
        }

        if (confirmPassword == null || confirmPassword.isEmpty()) {
            throw new Exception("Confirm Password cannot be empty");
        }

        if (!password.equals(confirmPassword)) {
            throw new Exception("Passwords do not match");
        }

        UserData user = data.findUserByUsername(username);

        if (user != null) {
            throw new Exception("User already exists");
        }

        data.addUser(new UserData(name, username, password));
    }

    public void loginUser(String username, String password) throws Exception{
        if (username == null || username.isEmpty()) {
            throw new Exception("Username cannot be empty");
        }

        if (password == null || password.isEmpty()) {
            throw new Exception("Password cannot be empty");
        }

        UserData user = data.findUserByUsername(username);

        if (user == null) {
            throw new Exception("User not found");
        }

        if (!user.getPassword().equals(password)) {
            throw new Exception("Invalid password");
        }

        this.userId = user.getId();
    }

    public void logoutUser() {
        this.userId = null;
    }

    public boolean isUserLoggedIn() {
        return this.userId != null;
    }

    public User getCurrentUser() throws Exception {
        if (this.userId == null) {
            throw new Exception("No user is currently logged in");
        }

        UserData user = data.findUserById(this.userId);

        return new User(user.getId(), user.getName(), user.getUsername());
    }

    public ZenQuote getZenQuoteOfDay() throws Exception {
        try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI("https://zenquotes.io/api/today"))
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                int status = response.statusCode();

                if (status != 200) {
                    throw new Exception("Failed to fetch quote, status code: " + status);
                }

                // Parse JSON response
                JSONArray jsonArray = new JSONArray(response.body());
                JSONObject quoteObject = jsonArray.getJSONObject(0);

                String quote = quoteObject.getString("q");
                String author = quoteObject.getString("a");

                return new ZenQuote(quote, author);
            } catch (Exception e) {
                throw new Exception("Failed to fetch quote: " + e.getMessage());
            }
    }
}
