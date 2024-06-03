package org.solideinc.solidequerydistributor.Classes;

import com.fasterxml.jackson.core.type.TypeReference;
import org.solideinc.solidequerydistributor.Util.JsonHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ConversationList {
    private static ConversationList instance;
    public ArrayList<Conversation> conversationList;

    public ConversationList() {
        if (conversationListExists()) {
            loadConversations();
        } else {
            conversationList = new ArrayList<>();
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
        conversationList = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get("data/conversations"))) {
            List<String> files = paths
                    .filter(Files::isRegularFile)
                    .map(Path::toString)
                    .toList();
            for (String file : files) {
                file = file.replace("data\\", "");
                Conversation conversation = JsonHandler.readJson(new TypeReference<Conversation>() {}, file);
                conversationList.add(conversation);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addConversation(Conversation conversation) {
        getInstance().conversationList.add(conversation);
        try {
            conversation.updateConversation();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeConversation(Conversation conversation) {
        getInstance().conversationList.remove(conversation);
        try {
            Files.deleteIfExists(Paths.get("data/conversations/" + conversation.getId() + ".json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
