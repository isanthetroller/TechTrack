package com.mycompany.bastaewan;

import com.mycompany.bastaewan.service.DataStore;

/**
 * Encapsulation: session state is encapsulated - only accessible through methods.
 * Delegates authentication to AuthService via DataStore.
 */
public class SessionManager {
    private static String username;
    private static String role;

    public static void setUser(String user, String r) {
        username = user;
        role = r;
    }

    public static String getUsername() {
        return username;
    }

    public static String getRole() {
        return role;
    }

    public static boolean changePassword(String user, String oldPass, String newPass) {
        if (user == null || oldPass == null || newPass == null) {
            return false;
        }
        return DataStore.getInstance().getAuthService().changePassword(user, oldPass, newPass);
    }
}
