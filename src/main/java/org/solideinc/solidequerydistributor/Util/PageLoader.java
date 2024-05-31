package org.solideinc.solidequerydistributor.Util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.solideinc.solidequerydistributor.Main;

import java.io.IOException;
import java.util.Objects;

public class PageLoader {

    public static final String STYLESHEET_PATH = "/org/solideinc/solidequerydistributor/style.css";
    private static Pane rootLayout;
    private static Stage stage;

    public static void setRootLayout(Pane rootLayout) {
        PageLoader.rootLayout = rootLayout;
    }

    public static void setStage(Stage stage) {
        PageLoader.stage = stage;
    }

    public static void startApplication() {
            Stage primaryStage = new Stage();
            setStage(primaryStage);
            setRootLayout(new Pane());
            loadLoginPage();
    }

    private static void loadPage(String fxmlFile, String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxmlFile));
            rootLayout = fxmlLoader.load();
            Scene scene = new Scene(rootLayout);
            scene.getStylesheets().add(Objects.requireNonNull(Main.class.getResource(STYLESHEET_PATH)).toExternalForm());
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
            stage.setResizable(false);
            stage.getIcons().add(new Image("/logo.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadLoginPage() {
        loadPage("Login.fxml", "Solide™ Query Distributor - Login");
    }

    public static void loadMainPage() {
        loadPage("Main.fxml", "Solide™ Query Distributor - Main");
    }

    public static void loadAccountPage() {
        loadPage("Account.fxml", "Solide™ Query Distributor - Account");
    }
}