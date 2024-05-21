package org.solideinc.solidequerydistributor.Controllers;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.solideinc.solidequerydistributor.Classes.User;

import java.util.ArrayList;
import java.util.UUID;

public class UserController {
    public static ArrayList<User> users = new ArrayList<>();

    public static void createUser(String email, String username, String password, String languagePreference) {
        User user = new User(email, username, password, languagePreference);
        users.add(user);
    }

    public static void addUser(User user) {
        users.add(user);
    }

    public static User getUser(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public static boolean checkUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                if (result.verified) {
                    return true;
                }
            }
        }
        return false;
    }

    public static ArrayList<User> getUsers() {
        return users;
    }

    public static void removeUser(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                users.remove(user);
                break;
            }
        }
    }

    public static void clearUsers() {
        users.clear();
    }

    public static UUID getUserById(UUID id) {
        for (User user : users) {
            if (user.getId().equals(id)) {
                return user.getId();
            }
        }
        return null;
    }
}
