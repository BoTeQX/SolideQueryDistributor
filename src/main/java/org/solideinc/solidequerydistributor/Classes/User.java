package org.solideinc.solidequerydistributor.Classes;

import java.util.UUID;

public class User {
    private UUID id;
    private String email;
    private String username;
    private String password;
    private String languagePreference;

    public String getPassword() {
        return password;
    }

    private void setPassword(String password) {
        this.password = password;
    }

    public String getLanguagePreference() {
        return languagePreference;
    }

    private void setLanguagePreference(String languagePreference) {
        this.languagePreference = languagePreference;
    }

    public String getUsername() {
        return username;
    }

    private void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    private void setEmail(String email) {
        this.email = email;
    }

    public UUID getId() {
        return id;
    }

    private void setId(UUID id) {
        this.id = id;
    }

    public User(String email, String username, String password, String languagePreference) {
        setId(UUID.randomUUID());
        setEmail(email);
        setUsername(username);
        setPassword(password);
        setLanguagePreference(languagePreference);
    }


}
