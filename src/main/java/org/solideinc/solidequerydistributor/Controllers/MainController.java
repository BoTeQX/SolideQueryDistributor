package org.solideinc.solidequerydistributor.Controllers;

import io.github.amithkoujalgi.ollama4j.core.exceptions.OllamaBaseException;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.util.Duration;
import org.solideinc.solidequerydistributor.Classes.Conversation;
import org.solideinc.solidequerydistributor.Classes.ConversationList;
import org.solideinc.solidequerydistributor.Classes.Message;
import org.solideinc.solidequerydistributor.Util.LamaAPI;
import org.solideinc.solidequerydistributor.Util.SolideAPI;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import java.util.concurrent.CompletableFuture;
import javafx.scene.shape.Circle;
import org.solideinc.solidequerydistributor.Util.PageLoader;

public class MainController {
    @FXML
    private Button logoutButton;
    @FXML
    private Button accountPageButton;
    @FXML
    private VBox chatBox;
    @FXML
    private TextArea chatField;
    @FXML
    private Button sendButton;
    @FXML
    private ScrollPane chatPane;
    @FXML
    private Pane sidebar;
    @FXML
    private Pane mainContent;
    @FXML
    private Button toggleButton;
    @FXML
    private Button addNewButton;
    @FXML
    private Circle sendCircle;
    @FXML
    private VBox chatPages;
    @FXML
    private ToggleButton offlineToggleButton;
    @FXML
    private Circle offlineToggleButtonCircle;


    private boolean waitingForResponse = false;

    private boolean isSidebarVisible = true;

    private Conversation currentConversation;

    private static boolean offlineMode = true;
    private final Tooltip offlineTooltip = new Tooltip("De Solide™ - Assistent is momenteel in de offline modus. Klik om online te gaan.");
    private final Tooltip onlineTooltip = new Tooltip("De Solide™ - Assistent is momenteel in de online modus. Klik om offline te gaan.");
    @FXML
    private void initialize() {
        logoutButton.setOnAction(event -> logout());
        accountPageButton.setOnAction(event -> accountPage());
        toggleButton.setOnAction(this::handleToggleAction);

        addNewButton.setOnAction(event -> addConversation("Nieuw gesprek", null));
        sendButton.setOnAction(event -> {
            try {
                confirmPrompt();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        chatField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER && !event.isShiftDown()) {
                event.consume();
                try {
                    confirmPrompt();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        List<Conversation> conversationsList = ConversationList.getConversations();
        for (Conversation conversation : conversationsList) {
            addConversation(conversation.getConversationName(), conversation.getId());
        }

        sendButton.setOnAction(event -> {
            try {
                confirmPrompt();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        });
        SolideAPI.setPromptsBasedOnLanguagePreference();
        setupOfflineToggleButton();

        hideChat();
    }

    private void setupOfflineToggleButton() {
        offlineToggleButton.setTooltip(offlineTooltip);
        offlineToggleButton.setOnAction(event -> handleOfflineToggleAction());
    }

    private void handleOfflineToggleAction() {
        TranslateTransition transition = createTransition();
        if (offlineToggleButton.isSelected()) {
            transition.setToX(19);
            setOfflineMode(false);
            setTooltip(onlineTooltip);
        } else {
            transition.setToX(0);
            setOfflineMode(true);
            setTooltip(offlineTooltip);
        }
        transition.play();
    }

    private TranslateTransition createTransition() {
        return new TranslateTransition(Duration.seconds(0.25), offlineToggleButtonCircle);
    }

    private void setTooltip(Tooltip tooltip) {
        offlineToggleButton.setTooltip(tooltip);
    }

    private void confirmPrompt() throws IOException {
        LamaAPI.connectToHost();
        String text = chatField.getText().trim();
        if (text.isEmpty() || waitingForResponse) return;

        addMessage(text, false, true);
        handleFakeResponse(text);

        if (LamaAPI.isConnected()) {
            handleRealResponse(text);
        } else {
            handleOfflineMode();
        }
    }

    private void handleFakeResponse(String text) throws IOException {
        String fakeText = SolideAPI.sendPrompt(text);
        if (fakeText != null) {
            addMessage(fakeText, true, true);
        }
    }

    private void handleRealResponse(String text) {
        chatField.setText("Wachten op reactie...");
        chatField.setDisable(true);
        CompletableFuture.supplyAsync(() -> {
            try {
                return LamaAPI.sendPrompt(text);
            } catch (OllamaBaseException | IOException e) {
                throw new IllegalStateException(e);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException(e);
            }
        }).thenAccept(response -> Platform.runLater(() -> {
            try {
                addMessage(response, true, true);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }));
        waitingForResponse = true;
    }

    private void handleOfflineMode() throws IOException {
        String message = LoginController.getLoggedInUser().getLanguagePreference().equals("nl") ?
                "De Solide™ Assistent is momenteel offline. probeer het later nogmaals, of schakel de online modus in." :
                "The Solide™ Assistant is currently offline. please try again later, or enable online mode.";
        addMessage(message, true, true);
    }

    public void addConversation(String name, UUID id) {
        if (id == null) {
            Conversation newConversation = new Conversation(name);
            ConversationList.addConversation(newConversation);
            id = newConversation.getId();
        }
        Label nameLabel = createNameLabel(name);
        Button optionsButton = createOptionsButton();
        ContextMenu contextMenu = createContextMenu(nameLabel, id);

        optionsButton.setOnAction(event -> {
            if (waitingForResponse) return;
            contextMenu.show(optionsButton, Side.BOTTOM, 0, 0);
        });

        VBox pageButton = createPageButton(nameLabel, optionsButton, id.toString());
        setupConversation(name, id, pageButton);

        chatPages.getChildren().add(pageButton);
    }

    private Label createNameLabel(String name) {
        Label nameLabel = new Label(name);
        nameLabel.setPrefWidth(300);
        nameLabel.getStyleClass().add("nameLabel");
        return nameLabel;
    }

    private Button createOptionsButton() {
        Button optionsButton = new Button(". . .");
        optionsButton.getStyleClass().add("optionButton");
        return optionsButton;
    }

    private ContextMenu createContextMenu(Label nameLabel, UUID id) {
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getStyleClass().add("contextMenu");

        MenuItem renameItem = createRenameMenuItem(nameLabel, id);
        MenuItem deleteItem = createDeleteMenuItem(id);

        contextMenu.getItems().addAll(renameItem, deleteItem);
        return contextMenu;
    }

    private MenuItem createRenameMenuItem(Label nameLabel, UUID id) {
        MenuItem renameItem = new MenuItem("Hernoemen");
        renameItem.getStyleClass().add("menu-item");
        renameItem.setOnAction(event -> {
            TextInputDialog dialog = new TextInputDialog(nameLabel.getText());
            dialog.setTitle("Hernoemen");
            dialog.setHeaderText("Hernoem het gesprek");
            dialog.setContentText("Naam:");
            dialog.showAndWait().ifPresent(result -> {
                nameLabel.setText(result);
                updateConversationName(id, result);
            });
        });
        return renameItem;
    }

    private void updateConversationName(UUID id, String result) {
        Conversation conversation = ConversationList.getConversation(id);
        if (conversation == null) {
            System.out.println("[UPDATE] Conversation not found");
            return;
        }

        conversation.setConversationName(result);
        try {
            conversation.updateConversation();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private MenuItem createDeleteMenuItem(UUID id) {
        MenuItem deleteItem = new MenuItem("Verwijderen");
        deleteItem.getStyleClass().addAll("menu-item", "delete");
        deleteItem.setOnAction(event -> {
            Conversation conversation = ConversationList.getConversation(id);
            if (conversation == null) {
                System.out.println("Conversation not found");
                return;
            }
            ConversationList.removeConversation(conversation);
            if (currentConversation.getId().equals(id)) {
                hideChat();
                currentConversation = null;
            }
            chatPages.getChildren().removeIf(node -> node.getId().equals(id.toString()));
        });
        return deleteItem;
    }

    private VBox createPageButton(Label nameLabel, Button optionsButton, String id) {
        HBox hBox = new HBox(nameLabel, optionsButton);
        hBox.getStyleClass().add("conversation");
        VBox pageButton = new VBox(hBox);
        pageButton.setId(id);
        VBox.setMargin(hBox, new Insets(5, 0, 0, 0));
        return pageButton;
    }

    private void setupConversation(String name, UUID id, VBox pageButton) {
        if (id == null) {
            currentConversation = new Conversation(name);
            ConversationList.addConversation(currentConversation);
        } else {
            currentConversation = ConversationList.getConversation(id);
        }
        final UUID tid = currentConversation.getId();

        pageButton.setOnMouseClicked(event -> {
            if (waitingForResponse) return;

            chatBox.getChildren().clear();
            Conversation conversation = ConversationList.getConversation(tid);
            if (conversation == null) {
                System.out.println("Conversation not found");
                return;
            }

            List<Message> messages = conversation.getMessages();
            messages.forEach(message -> {
                try {
                    addMessage(message.getMessage(), message.isAnswer(), false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            showChat();
        });
    }

    private void addMessage(String text, boolean answer, boolean save) throws IOException {
        if (currentConversation == null) {
            System.out.println("No conversation selected");
            return;
        }

        text = text.trim();
        if (save) currentConversation.addMessage(text, answer);

        Label messageLabel = createMessageLabel(text);
        HBox messageBox = createMessageBox(answer, messageLabel);

        messageBox.getChildren().add(messageLabel);
        chatBox.getChildren().add(messageBox);

        scrollChatPane();
    }

    private Label createMessageLabel(String text) {
        Label messageLabel = new Label(text);
        messageLabel.setWrapText(true);

        Text textNode = new Text(text);
        Font newFont = new Font(messageLabel.getFont().getFamily(), 16);
        textNode.setFont(newFont);
        textNode.setWrappingWidth(300);
        textNode.setTextOrigin(VPos.BASELINE);
        textNode.setBoundsType(TextBoundsType.LOGICAL_VERTICAL_CENTER);

        double textHeight = textNode.getLayoutBounds().getHeight();
        messageLabel.setPrefHeight(textHeight + 20);
        messageLabel.setMinHeight(textHeight + 20);
        messageLabel.setMaxHeight(textHeight + 20);
        return messageLabel;
    }

    private HBox createMessageBox(boolean answer, Label messageLabel) {
        HBox messageBox = new HBox();
        messageBox.setPadding(new Insets(5, 5, 5, 5));

        if (answer) {
            messageBox.setAlignment(Pos.CENTER_LEFT);
            messageLabel.setStyle("-fx-background-color: transparent; -fx-padding: 10px; -fx-border-radius: 10; -fx-background-radius: 10; -fx-font-size: 16");
            waitingForResponse = false;
            chatField.setText("");
            chatField.setDisable(false);
        } else {
            messageBox.setAlignment(Pos.CENTER_RIGHT);
            messageLabel.setStyle("-fx-background-color: #41515c; -fx-padding: 10px; -fx-border-radius: 10; -fx-background-radius: 10; -fx-font-size: 16");
        }
        return messageBox;
    }

    private void scrollChatPane() {
        PauseTransition pause = new PauseTransition(Duration.millis(25));
        pause.setOnFinished(event -> chatPane.setVvalue(1.0));
        pause.play();
    }

    private void logout(){
        PageLoader.loadLoginPage();
    }
    private void accountPage(){
        PageLoader.loadAccountPage();
    }

    private void handleToggleAction(ActionEvent event) {
        if (isSidebarVisible) {
            hideSidebar();
        } else {
            showSidebar();
        }
        isSidebarVisible = !isSidebarVisible;
    }

    private void hideSidebar(){
        sidebar.setPrefWidth(15);
        mainContent.setLayoutX(15);
        mainContent.setPrefWidth(900);
        chatField.setPrefWidth(770);
        sendButton.setLayoutX(820);
        sendCircle.setLayoutX(850);
        toggleButton.setText(">");
        toggleButton.setLayoutY(270);
        toggleButton.setLayoutX(-5);
        chatPane.setPrefWidth(830);
        chatBox.setPrefWidth(825);
    }

    private void showSidebar(){
        sidebar.setPrefWidth(260);
        mainContent.setPrefWidth(640);
        mainContent.setLayoutX(260);
        chatField.setPrefWidth(518);
        sendButton.setLayoutX(562);
        sendCircle.setLayoutX(591);
        toggleButton.setText("<");
        toggleButton.setLayoutY(0);
        toggleButton.setLayoutX(210);
        chatPane.setPrefWidth(587);
        chatBox.setPrefWidth(583);
    }

    private void hideChat() {
        chatPane.setVisible(false);
        chatField.setVisible(false);
        offlineToggleButton.setVisible(false);
        sendButton.setVisible(false);
        sendCircle.setVisible(false);
    }

    private void showChat() {
        chatPane.setVisible(true);
        chatField.setVisible(true);
        offlineToggleButton.setVisible(true);
        sendButton.setVisible(true);
        sendCircle.setVisible(true);
    }

    public static boolean isOfflineMode() {
        return offlineMode;
    }

    public static void setOfflineMode(boolean offlineMode) {
        MainController.offlineMode = offlineMode;
    }
}
