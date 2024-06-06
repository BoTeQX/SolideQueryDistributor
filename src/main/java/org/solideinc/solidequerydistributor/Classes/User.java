package org.solideinc.solidequerydistributor.Classes;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class User {
    private UUID id;
    private String email;
    private String username;
    private String password;
    private String languagePreference;
    private List<UUID> conversationIds;

    public User(String email, String username, String password, String languagePreference) {
        this.id = UUID.randomUUID();
        this.email = email;
        this.username = username;
        this.password = password;
        this.languagePreference = languagePreference;
        this.conversationIds = new ArrayList<>();
    }

    @JsonCreator
    public User(@JsonProperty("id") String id,
                @JsonProperty("email") String email,
                @JsonProperty("username") String username,
                @JsonProperty("password") String password,
                @JsonProperty("languagePreference") String languagePreference,
                @JsonProperty("conversationIds") String[] conversationIds) {
        this.id = UUID.fromString(id);
        this.email = email;
        this.username = username;
        this.password = password;
        this.languagePreference = languagePreference;
        this.conversationIds = Stream.of(conversationIds)
                .map(UUID::fromString)
                .collect(Collectors.toList());
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public void updatePassword(String password) {
        this.password = BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    public String getLanguagePreference() {
        return languagePreference;
    }

    public void setLanguagePreference(String languagePreference) {
        this.languagePreference = languagePreference;
    }

    public List<UUID> getConversationIds() {
        return conversationIds;
    }

    public void setConversationIds(List<UUID> conversationIds) {
        this.conversationIds = conversationIds;
    }
}
