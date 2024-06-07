package org.solideinc.solidequerydistributor;

import javafx.application.Application;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.solideinc.solidequerydistributor.Classes.ConversationList;
import org.solideinc.solidequerydistributor.Util.PageLoader;
import org.solideinc.solidequerydistributor.Util.LamaAPI;
public class Main extends Application {
    @Override
    public void start(Stage stage) {
        Pane rootLayout = new Pane();
        PageLoader.setRootLayout(rootLayout);
        PageLoader.startApplication();
    }

    public static void main(String[] args) {
        new ConversationList();
        launch();
        LamaAPI.connectToHost();
    }
}