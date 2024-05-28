package org.solideinc.solidequerydistributor.Controllers;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.fasterxml.jackson.core.type.TypeReference;
import org.solideinc.solidequerydistributor.Classes.User;
import org.solideinc.solidequerydistributor.Util.JsonHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class UserController {
    public static ArrayList<User> users = new ArrayList<>();

    public static void createUser(String email, String username, String password, String languagePreference) throws IOException {
        String hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        User user = new User(email, username, hashedPassword, languagePreference);
        try {
            loadUsers();
            users.add(user);
            updateUsers();
        } catch (IOException e) {
            users.add(user);
            updateUsers();
        }
    }

    public static User getUser(String username) throws IOException {
        loadUsers();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public static boolean checkUser(String username, String password) throws IOException {
        loadUsers();
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

    public static void updateUsers() throws IOException {
        JsonHandler.writeJson(users, "users.json");
    }

    public static void loadUsers() throws IOException {
        ArrayList<User> readUsers = JsonHandler.readJson(new TypeReference<>() {}, "users.json");
        if (readUsers != null) {
            users = readUsers;
        }
    }

    public static ArrayList<User> getUsers() {
        return users;
    }

    public static void removeUser(String username) throws IOException {
        loadUsers();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                users.remove(user);
                updateUsers();
                break;
            }
        }
    }

    public static void clearUsers() throws IOException {
        loadUsers();
        users.clear();
        updateUsers();
    }

    public static UUID getUserById(UUID id) throws IOException {
        loadUsers();
        for (User user : users) {
            if (user.getId().equals(id)) {
                return user.getId();
            }
        }
        return null;
    }
}
