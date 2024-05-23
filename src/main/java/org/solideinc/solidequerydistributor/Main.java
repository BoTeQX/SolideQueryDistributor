package org.solideinc.solidequerydistributor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.solideinc.solidequerydistributor.Controllers.LoginController;
import org.solideinc.solidequerydistributor.Util.PageLoader;
import org.solideinc.solidequerydistributor.Utils.LamaAPI;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Pane rootLayout = new Pane();
        PageLoader.setRootLayout(rootLayout);
        PageLoader.startApplication();
    }

    public static void main(String[] args) {
        launch();
        LamaAPI.connectToHost();
    }
}