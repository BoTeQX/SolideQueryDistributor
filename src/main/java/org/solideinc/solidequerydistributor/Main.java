package org.solideinc.solidequerydistributor;

import javafx.application.Application;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.solideinc.solidequerydistributor.Classes.ConversationList;
import org.solideinc.solidequerydistributor.Util.PageLoader;
import org.solideinc.solidequerydistributor.Util.LamaAPI;
public class Main extends Application {

    public static PageLoader pageLoader = PageLoader.getInstance();

    @Override
    public void start(Stage stage) {
        Pane rootLayout = new Pane();
        pageLoader.setRootLayout(rootLayout);
        pageLoader.startApplication();
    }

    public static void main(String[] args) {
        new ConversationList();
        launch();
        LamaAPI.connectToHost();
    }
}