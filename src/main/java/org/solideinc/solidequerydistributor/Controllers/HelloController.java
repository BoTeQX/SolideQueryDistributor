package org.solideinc.solidequerydistributor.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.solideinc.solidequerydistributor.Utils.LamaAPI;

import java.util.concurrent.CompletableFuture;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        CompletableFuture.supplyAsync(() -> LamaAPI.sendPrompt("test")).thenAccept(response -> {
            Platform.runLater(() -> welcomeText.setText(response));
        });
    }
}
