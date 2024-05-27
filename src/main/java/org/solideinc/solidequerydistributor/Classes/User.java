package org.solideinc.solidequerydistributor.Classes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLanguagePreference() {
        return languagePreference;
    }

    public void setLanguagePreference(String languagePreference) {
        this.languagePreference = languagePreference;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User(String email, String username, String password, String languagePreference) {
        setId(UUID.randomUUID());
        setEmail(email);
        setUsername(username);
        setPassword(password);
        setLanguagePreference(languagePreference);
    }

    @JsonCreator
    public User(@JsonProperty("id") String id,
                @JsonProperty("email") String email,
                @JsonProperty("username") String username,
                @JsonProperty("password") String password,
                @JsonProperty("languagePreference") String languagePreference) {
        setId(UUID.fromString(id));
        setEmail(email);
        setUsername(username);
        setPassword(password);
        setLanguagePreference(languagePreference);
    }



}
