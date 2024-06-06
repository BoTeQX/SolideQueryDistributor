package org.solideinc.solidequerydistributor.Controllers;

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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import javafx.scene.shape.Circle;
import org.solideinc.solidequerydistributor.Util.PageLoader;

public class MainController {
    @FXML
    private Button logoutButton;
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

    private boolean waitingForResponse = false;

    private boolean isSidebarVisible = true;

    private Conversation currentConversation;

    @FXML
    private void initialize() {
        logoutButton.setOnAction(event -> logout());
        toggleButton.setOnAction(this::handleToggleAction);
        addNewButton.setOnAction(event -> addConversation("Nieuw gesprek", null));
        sendButton.setOnAction(event -> {
            try {
                if (currentConversation != null)
                    confirmPrompt();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        chatField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER && !event.isShiftDown()) {
                event.consume();
                try {
                    if (currentConversation != null)
                        confirmPrompt();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        List<Conversation> conversationList = ConversationList.getInstance().conversationList;
        for (Conversation conversation : conversationList) {
            addConversation(conversation.getConversationName(), conversation.getId());
        }
    }

    private void confirmPrompt() throws IOException {
        LamaAPI.connectToHost();
        String text = chatField.getText().trim();
        if (text == null || text.length() == 0 || waitingForResponse)
            return;

        addMessage(text, false, true);
        String fakeText = SolideAPI.sendPrompt(text);
        if (fakeText != null) {
            addMessage(fakeText, true, true);
            return;
        }

        if (LamaAPI.isConnected()) {
            chatField.setText("Wachten op reactie...");
            chatField.setDisable(true);
            CompletableFuture.supplyAsync(() -> LamaAPI.sendPrompt(text)).thenAccept(response -> {
                Platform.runLater(() -> {
                    try {
                        addMessage(response, true, true);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            });
            waitingForResponse = true;
        } else {
            if (LoginController.getLoggedInUser().getLanguagePreference().equals("nl"))
                addMessage("De Solide™ Assistent is momenteel buiten gebruik, probeer het later nogmaals", true, true);
            else
                addMessage("The Solide™ Assistant is currently offline, please try again later", true, true);
        }
    }
    public void addConversation(String name, UUID id) {
        Label nameLabel = new Label(name);
        nameLabel.setPrefWidth(300);
        nameLabel.getStyleClass().add("nameLabel");

        Button optionsButton = new Button(". . .");
        optionsButton.getStyleClass().add("optionButton");

        ContextMenu contextMenu = new ContextMenu();
        optionsButton.setOnAction(event -> {
            if (waitingForResponse)
                return;
            contextMenu.show(optionsButton, Side.BOTTOM, 0, 0);
        });
        contextMenu.getStyleClass().add("contextMenu");

        HBox hBox = new HBox(nameLabel, optionsButton);
        hBox.getStyleClass().add("conversation");
        VBox pageButton = new VBox(hBox);
        VBox.setMargin(hBox, new Insets(5, 0, 0, 0));

        if (id == null) {
            currentConversation = new Conversation(name);
            ConversationList.addConversation(currentConversation);
        } else {
            currentConversation = ConversationList.getConversation(id);
        }
        final UUID tid = currentConversation.getId();

        MenuItem renameItem = new MenuItem("Hernoemen");
        renameItem.getStyleClass().add("menu-item");
        renameItem.setOnAction(event -> {
            TextInputDialog dialog = new TextInputDialog(nameLabel.getText());
            dialog.setTitle("Hernoemen");
            dialog.setHeaderText("Hernoem het gesprek");
            dialog.setContentText("Naam:");
            dialog.showAndWait().ifPresent(result -> {
                nameLabel.setText(result);
                final UUID fid = tid;
                Conversation conversation = ConversationList.getConversation(fid);
                if (conversation == null) {
                    System.out.println("Conversation not found");
                    return;
                }

                conversation.setConversationName(result);
                try {
                    conversation.updateConversation();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        });

        MenuItem deleteItem = new MenuItem("Verwijderen");
        deleteItem.getStyleClass().add("menu-item");
        deleteItem.getStyleClass().add("delete");
        deleteItem.setOnAction(event -> {
            final UUID fid = tid;
            Conversation conversation = ConversationList.getConversation(fid);
            if (conversation == null) {
                System.out.println("Conversation not found");
                return;
            }
            ConversationList.removeConversation(conversation);
            chatPages.getChildren().remove(pageButton);
        });

        contextMenu.getItems().addAll(renameItem, deleteItem);

        pageButton.setOnMouseClicked(event -> {
            if (waitingForResponse)
                return;

            chatBox.getChildren().clear();
            final UUID fid = tid;
            Conversation conversation = ConversationList.getConversation(fid);
            if (currentConversation == null) {
                System.out.println("Conversation not found");
                return;
            }

            List<Message> messages = conversation.getConversation();
            for (Message message : messages) {
                try {
                    addMessage(message.getMessage(), message.isAnswer(), false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        chatPages.getChildren().add(pageButton);
    }


    private void addMessage(String text, boolean answer, boolean save) throws IOException {
        if (currentConversation == null) {
            System.out.println("No conversation selected");
            return;
        }

        text = text.trim();
        if (save)
            currentConversation.addMessage(text, answer);

        Label messageLabel = new Label(text);
        HBox messageBox = new HBox();

        messageLabel.setWrapText(true);
        Text textNode = new Text(text);

        Font currentFont = messageLabel.getFont();
        Font newFont = new Font(currentFont.getFamily(), 16);

        textNode.setFont(messageLabel.getFont());
        textNode.setWrappingWidth(300);
        textNode.setFont(newFont);
        textNode.setTextOrigin(VPos.BASELINE);
        textNode.setBoundsType(TextBoundsType.LOGICAL_VERTICAL_CENTER);

        double textHeight = textNode.getLayoutBounds().getHeight();
        messageLabel.setPrefHeight(textHeight + 20);
        messageLabel.setMinHeight(textHeight + 20);
        messageLabel.setMaxHeight(textHeight + 20);

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

        messageBox.getChildren().add(messageLabel);
        messageBox.setPadding(new Insets(5, 5, 5, 5));
        chatBox.getChildren().add(messageBox);

        PauseTransition pause = new PauseTransition(Duration.millis(25));
        pause.setOnFinished(event -> chatPane.setVvalue(1.0));
        pause.play();
    }

    private void logout(){
        PageLoader.loadLoginPage();
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
}
