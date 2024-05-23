package org.solideinc.solidequerydistributor.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.solideinc.solidequerydistributor.Main;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class MainController {
    @FXML
    private Pane RootLayout;

    @FXML
    private Button logoutButton;

    @FXML
    private void initialize() {
        logoutButton.setOnAction(event -> logout());
    }


    private void logout(){
        try {
            Stage stage = (Stage) RootLayout.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Login.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            URL styleSheetUrl = getClass().getResource("/org/solideinc/solidequerydistributor/style.css");
            if (styleSheetUrl != null) {
                scene.getStylesheets().add(styleSheetUrl.toExternalForm());
            }

            stage.setScene(scene);
            stage.show();
            stage.setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
}
}
