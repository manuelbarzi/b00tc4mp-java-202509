package com.b00tc4mp.app.logic;

import com.b00tc4mp.app.data.Data;
import com.b00tc4mp.app.data.UserData;

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
}
