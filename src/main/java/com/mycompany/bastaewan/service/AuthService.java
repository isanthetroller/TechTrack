package com.mycompany.bastaewan.service;

import com.mycompany.bastaewan.model.User;
import java.util.ArrayList;

// Encapsulation: authentication logic is encapsulated in this service
public class AuthService {
    private ArrayList<User> users;

    public AuthService() {
        users = new ArrayList<>();
        users.add(new User(1, "admin", "password", "Admin"));
    }

    public User authenticate(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }

    public boolean changePassword(String username, String oldPassword, String newPassword) {
        for (User u : users) {
            if (u.getUsername().equals(username) && u.getPassword().equals(oldPassword)) {
                u.setPassword(newPassword);
                return true;
            }
        }
        return false;
    }

    public ArrayList<User> getAllUsers() {
        return users;
    }
}
