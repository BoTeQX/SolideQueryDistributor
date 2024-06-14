package org.solideinc.solidequerydistributor.Controllers;

import io.github.amithkoujalgi.ollama4j.core.exceptions.OllamaBaseException;
import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.util.Duration;
import org.solideinc.solidequerydistributor.Classes.Conversation;
import org.solideinc.solidequerydistributor.Classes.ConversationList;
import org.solideinc.solidequerydistributor.Classes.Message;
import org.solideinc.solidequerydistributor.Main;
import org.solideinc.solidequerydistributor.Util.LamaAPI;
import org.solideinc.solidequerydistributor.Util.SolideAPI;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import java.util.concurrent.CompletableFuture;
import javafx.scene.shape.Circle;

public class MainController {
    private static MainController instance;
    @FXML
    public Pane rootLayout;
    @FXML
    private ComboBox<String> updateLanguageComboBox;
    @FXML
    private Button logoutButton;
    @FXML
    private Button accountPageButton;
    @FXML
    private VBox chatBox;
    @FXML
    private TextArea chatField;
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
    private VBox chatPages;
    @FXML
    private ToggleButton offlineToggleButton;
    @FXML
    private Circle offlineToggleButtonCircle;
    @FXML
    private ImageView connectionSymbol;
    @FXML
    private Label conversationTitle;

    private Label currentMessageLabel;

   Image connectionImage = new Image("/connectionImage.png");

    Image connectionNotImage = new Image("/connectionNotImage.png");


    private boolean waitingForResponse = false;
    private boolean animatingText = false;

    private boolean isSidebarVisible = true;



    private Conversation currentConversation;

    private static boolean offlineMode;

    private final Tooltip offlineTooltip = new Tooltip("De Solide™ - Assistent is momenteel in de offline modus. Klik om online te gaan.");

    private final Tooltip onlineTooltip = new Tooltip("De Solide™ - Assistent is momenteel in de online modus. Klik om offline te gaan.");

    public static MainController getInstance() {
        return instance;
    }
    @FXML
    private void initialize() {
        updateLanguageComboBox.setOnAction(event -> updateLanguageSetting());
        ObservableList<String> options = updateLanguageComboBox.getItems();
        options.add("nl");
        options.add("en");
        updateLanguageComboBox.setValue(LoginController.getLoggedInUser().getLanguagePreference());

        logoutButton.setOnAction(event -> logout());
        accountPageButton.setOnAction(event -> accountPage());
        toggleButton.setOnAction(this::handleToggleAction);

        addNewButton.setOnAction(event -> addConversation("Nieuw gesprek", null));

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

        SolideAPI.setPromptsBasedOnLanguagePreference();
        setupOfflineToggleButton();

        hideChat();

        instance = this;
        offlineMode = true;
    }

    public void setOnlineTooltip(){
        onlineTooltip.setShowDuration(Duration.INDEFINITE);
        onlineTooltip.setShowDelay(Duration.ZERO);
        onlineTooltip.setHideDelay(Duration.ZERO);
        offlineToggleButton.setTooltip(onlineTooltip);
    }

    public void setOfflineTooltip(){
        offlineTooltip.setShowDuration(Duration.INDEFINITE);
        offlineTooltip.setShowDelay(Duration.seconds(0));
        offlineTooltip.setHideDelay(Duration.seconds(0));
        offlineToggleButton.setTooltip(offlineTooltip);
    }

    private void updateLanguageSetting(){
        LoginController.getLoggedInUser().setLanguagePreference(updateLanguageComboBox.getValue());
        try {
            UserController.updateUsers();
            SolideAPI.setPromptsBasedOnLanguagePreference();
        }catch (IOException e){
            throw new IllegalStateException(e);
        }
    }

    private void setupOfflineToggleButton() {
        connectionSymbol.setImage(connectionNotImage);
        setOfflineTooltip();
        offlineToggleButton.setOnAction(event -> handleOfflineToggleAction());
    }

    private void handleOfflineToggleAction() {
        if (waitingForResponse) return;
        TranslateTransition transition = createTransition();
        boolean offlinemode = offlineToggleButton.isSelected();
        transition.setToX(offlinemode ? 19 : 0);
        setOfflineMode(!offlinemode);
        setOnlineTooltip();
        connectionSymbol.setImage(offlinemode ? connectionImage : connectionNotImage);
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
        if (handleFakeResponse(text)) return;

        if (LamaAPI.isConnected()) {
            handleRealResponse(text);
        } else {
            handleOfflineMode();
        }
    }

    private boolean handleFakeResponse(String text) throws IOException {
        String fakeText = SolideAPI.sendPrompt(text);
        if (fakeText != null) {
            addMessage(fakeText, true, true);
            return true;
        }
        return false;
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
        if (waitingForResponse) return;
        if (id == null) {
            Conversation newConversation = new Conversation(name);
            ConversationList.addConversation(newConversation);
            id = newConversation.getId();
            System.out.println(id);
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
        if (currentConversation != null && currentConversation.getId().equals(id))
            conversationTitle.setText(result);

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
            if (currentConversation.getId().equals(id)) {
                System.out.println("Deleting current conversation");
                hideChat();
                currentConversation = null;
            }
            ConversationList.removeConversation(conversation);
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
        Conversation tempConversation;
        if (id == null) {
            tempConversation = new Conversation(name);
            ConversationList.addConversation(tempConversation);
        } else {
            tempConversation = ConversationList.getConversation(id);
        }
        final UUID tid = tempConversation.getId();

        pageButton.setOnMouseClicked(event -> {
            if (waitingForResponse) return;

            chatBox.getChildren().clear();
            Conversation conversation = ConversationList.getConversation(tid);
            if (conversation == null) {
                System.out.println("Conversation not found");
                return;
            }

            chatPages.getChildren().forEach(node -> {
                node.getStyleClass().remove("selected");
            });

            pageButton.getStyleClass().add("selected");

            currentConversation = conversation;
            List<Message> messages = conversation.getMessages();
            messages.forEach(message -> {
                try {
                    addMessage(message.getMessage(), message.isAnswer(), false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            conversationTitle.setText(conversation.getConversationName());
            showChat();
        });
    }

    public void updateMessage(String message) {
        message = message.trim();
        if (currentMessageLabel == null && !animatingText) {
            animatingText = true;
            currentMessageLabel = createMessageLabel(message);
            HBox messageBox = createMessageBox(true, currentMessageLabel);
            messageBox.getChildren().add(currentMessageLabel);
            chatBox.getChildren().add(messageBox);
        } else {
            currentMessageLabel.setText(message);
            scrollChatPane();
        }
    }

    private void addMessage(String text, boolean answer, boolean save) throws IOException {
        if (currentConversation == null) {
            System.out.println("No conversation selected");
            return;
        }

        text = text.trim();
        if (save) currentConversation.addMessage(text, answer);
        if (currentMessageLabel != null) {
            animatingText = false;
            chatBox.getChildren().remove(chatBox.getChildren().size() - 1);
            currentMessageLabel = null;
        }

        Label messageLabel = createMessageLabel(text);
        HBox messageBox = createMessageBox(answer, messageLabel);

        messageBox.getChildren().add(messageLabel);
        chatBox.getChildren().add(messageBox);

        System.out.println(waitingForResponse);
        if (answer) {
            waitingForResponse = false;
            chatField.setText("");
            chatField.setDisable(false);
        }
        scrollChatPane();
    }

    private Label createMessageLabel(String text) {
        Label messageLabel = new Label(text);
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(450);
        messageLabel.setMinHeight(Region.USE_PREF_SIZE);

        return messageLabel;
    }

    private HBox createMessageBox(boolean answer, Label messageLabel) {
        HBox messageBox = new HBox(5);
        messageBox.setPadding(new Insets(5));

        if (answer) {
            messageBox.setAlignment(Pos.CENTER_LEFT);
            messageLabel.setStyle("-fx-background-color: transparent; -fx-border-width: 1.5; -fx-border-color: white; -fx-padding: 10px; -fx-border-radius: 10; -fx-background-radius: 10; -fx-font-size: 16");
        } else {
            messageBox.setAlignment(Pos.CENTER_RIGHT);
            messageLabel.setStyle("-fx-background-color: white; -fx-padding: 10px; -fx-border-radius: 10; -fx-background-radius: 10; -fx-font-size: 16; -fx-text-fill: black");
        }
        return messageBox;
    }

    private void scrollChatPane() {
        PauseTransition pause = new PauseTransition(Duration.millis(25));
        pause.setOnFinished(event -> chatPane.setVvalue(1.0));
        pause.play();
    }

    private void logout() {
        if (waitingForResponse) return;
        Main.pageLoader.loadLoginPage();
    }
    private void accountPage() {
        if (waitingForResponse) return;
        Main.pageLoader.loadAccountPage();
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
        chatField.setPrefWidth(848);
        toggleButton.setText(">");
        toggleButton.setLayoutY(270);
        toggleButton.setLayoutX(-5);
        chatPane.setPrefWidth(865);
        chatBox.setPrefWidth(858);
        updateLanguageComboBox.setLayoutX(795);
        conversationTitle.setPrefWidth(660);
    }

    private void showSidebar(){
        sidebar.setPrefWidth(260);
        mainContent.setPrefWidth(640);
        mainContent.setLayoutX(260);
        chatField.setPrefWidth(603);
        toggleButton.setText("<");
        toggleButton.setLayoutY(0);
        toggleButton.setLayoutX(210);
        chatPane.setPrefWidth(615);
        chatBox.setPrefWidth(613);
        updateLanguageComboBox.setLayoutX(552);
        conversationTitle.setPrefWidth(417);
    }

    private void hideChat() {
        chatPane.setVisible(false);
        chatField.setVisible(false);
        offlineToggleButton.setVisible(false);
        updateLanguageComboBox.setVisible(false);
        connectionSymbol.setVisible(false);
        conversationTitle.setVisible(false);
    }

    private void showChat() {
        chatPane.setVisible(true);
        chatField.setVisible(true);
        offlineToggleButton.setVisible(true);
        updateLanguageComboBox.setVisible(true);
        connectionSymbol.setVisible(true);
        conversationTitle.setVisible(true);
    }

    public static boolean isOfflineMode() {
        return offlineMode;
    }

    public static void setOfflineMode(boolean offlineMode) {
        MainController.offlineMode = offlineMode;
    }

}
