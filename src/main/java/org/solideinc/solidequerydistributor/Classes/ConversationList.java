package org.solideinc.solidequerydistributor.Classes;

import com.fasterxml.jackson.core.type.TypeReference;
import org.solideinc.solidequerydistributor.Util.JsonHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class ConversationList {
    private static ConversationList instance;
    private ArrayList<Conversation> conversationsList;

    public ConversationList() {
        if (conversationListExists()) {
            loadConversations();
        } else {
            conversationsList = new ArrayList<>();
        }
    }

    public static ConversationList getInstance() {
        if (instance == null) {
            instance = new ConversationList();
        }
        return instance;
    }

    private boolean conversationListExists() {
        try (Stream<Path> paths = Files.walk(Paths.get("data/conversations"))) {
            return paths.anyMatch(Files::isRegularFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void loadConversations() {
        conversationsList = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get("data/conversations"))) {
            List<String> files = paths
                    .filter(Files::isRegularFile)
                    .map(Path::toString)
                    .toList();
            for (String file : files) {
                file = file.replace("data\\", "");
                Conversation conversation = JsonHandler.readJson(new TypeReference<Conversation>() {}, file);
                conversationsList.add(conversation);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addConversation(Conversation conversation) {
        getInstance().conversationsList.add(conversation);
        try {
            conversation.updateConversation();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeConversation(Conversation conversation) {
        getInstance().conversationsList.remove(conversation);
        try {
            JsonHandler.removeFile("conversations/" + conversation.getId() + ".json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Conversation getConversation(UUID id) {
        return getInstance().conversationsList.stream()
                .filter(conversation -> conversation.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public static List<Conversation> getConversations() {
        return getInstance().conversationsList;
    }
}
