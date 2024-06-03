package org.solideinc.solidequerydistributor.Classes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.solideinc.solidequerydistributor.Util.JsonHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Conversation {
    private UUID id;
    private String conversationName;
    private Date lastUpdate;
    private final List<Message> conversation;

    public Conversation(String conversationName) {
        this.id = UUID.randomUUID();
        this.conversationName = conversationName;
        this.lastUpdate = new Date();
        this.conversation = new ArrayList<>();
    }

    @JsonCreator
    public Conversation(
            @JsonProperty("id") String id,
            @JsonProperty("conversationName") String conversationName,
            @JsonProperty("lastUpdate") Date lastUpdate,
            @JsonProperty("conversation") List<Message> conversation) {
        this.id = UUID.fromString(id);
        this.conversationName = conversationName;
        this.lastUpdate = lastUpdate;
        this.conversation = conversation;
    }



    public void updateConversation() throws IOException {
        JsonHandler.writeJson(this, "conversations/" + this.id.toString() + ".json");
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getDate() {
        return lastUpdate;
    }

    public void setDate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getConversationName() {
        return conversationName;
    }

    public void setConversationName(String conversationName) {
        this.conversationName = conversationName;
    }

    public List<Message> getConversation() {
        return new ArrayList<>(conversation);
    }

    public void addMessage(String message, Boolean isAnswer) throws IOException {
        conversation.add(new Message(message, isAnswer));
        this.lastUpdate = new Date();
        updateConversation();
    }
}
