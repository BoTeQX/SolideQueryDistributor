package org.solideinc.solidequerydistributor.Controllers;


import javafx.event.ActionEvent;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.solideinc.solidequerydistributor.Main;
import org.solideinc.solidequerydistributor.Util.LamaAPI;
import org.solideinc.solidequerydistributor.Util.SolideAPI;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
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
    private Circle sendCircle;
    private boolean waitingForResponse = false;


    private boolean isSidebarVisible = true;


    @FXML
    private void initialize() {
        logoutButton.setOnAction(event -> logout());
        toggleButton.setOnAction(this::handleToggleAction);
        sendButton.setOnAction(event -> confirmPrompt());
    }

    private void confirmPrompt() {
        LamaAPI.connectToHost();
        String text = chatField.getText();
        if (text == null || text.length() == 0 || waitingForResponse)
            return;

        addMessage(text, false);
        String fakeText = SolideAPI.sendPrompt(text);
        if (fakeText != null) {
            addMessage(fakeText, true);
            return;
        }

        chatField.setText("Waiting for Response");
        chatField.setDisable(true);
        CompletableFuture.supplyAsync(() -> LamaAPI.sendPrompt(text)).thenAccept(response -> {
            Platform.runLater(() -> addMessage(response, true));
        });
        waitingForResponse = true;
    }

    private void addMessage(String text, boolean answer) {
        Label messageLabel = new Label(text);
        HBox messageBox = new HBox();

        messageLabel.setWrapText(true);
        Text textNode = new Text(text);
        textNode.setFont(messageLabel.getFont());
        textNode.setWrappingWidth(300);
        textNode.setTextOrigin(VPos.BASELINE);
        textNode.setBoundsType(TextBoundsType.LOGICAL_VERTICAL_CENTER);

        double textHeight = textNode.getLayoutBounds().getHeight();
        System.out.println(textHeight);
        messageLabel.setPrefHeight(textHeight + 20);
        messageLabel.setMinHeight(textHeight + 20);
        messageLabel.setMaxHeight(textHeight + 20);

        if (answer) {
            messageBox.setAlignment(Pos.CENTER_LEFT);
            messageLabel.setStyle("-fx-background-color: lightblue; -fx-padding: 10px; -fx-border-radius: 10; -fx-background-radius: 10;");
            waitingForResponse = false;
            chatField.setText("");
            chatField.setDisable(false);
        } else {
            messageBox.setAlignment(Pos.CENTER_RIGHT);
            messageLabel.setStyle("-fx-background-color: lightgreen; -fx-padding: 10px; -fx-border-radius: 10; -fx-background-radius: 10;");
        }

        messageBox.getChildren().add(messageLabel);
        messageBox.setPadding(new Insets(5, 5, 5, 5));
        chatBox.getChildren().add(messageBox);

        // EEE, JavaFX don't have inverted ScrollPane so you have to move it everytime :/
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
        toggleButton.setLayoutX(0);
    }

    private void showSidebar(){
        sidebar.setPrefWidth(260);
        mainContent.setPrefWidth(640);
        mainContent.setLayoutX(260);
        chatField.setPrefWidth(518);
        sendButton.setLayoutX(562);
        sendCircle.setLayoutX(591);
        toggleButton.setText("<");
        toggleButton.setLayoutX(205);
    }
}
