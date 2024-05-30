package org.solideinc.solidequerydistributor.Classes;

import org.solideinc.solidequerydistributor.Util.JsonHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Conversation {
    private UUID id;
    private String conversationName;
    private final List<Message> conversation;

    public Conversation(String conversationName) {
        this.id = UUID.randomUUID();
        this.conversationName = conversationName;
        this.conversation = new ArrayList<>();
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
        updateConversation();
    }
}
