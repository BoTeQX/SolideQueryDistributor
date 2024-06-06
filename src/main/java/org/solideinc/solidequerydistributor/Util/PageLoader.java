package org.solideinc.solidequerydistributor.Util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.solideinc.solidequerydistributor.Interfaces.PageLoaderInterface;
import org.solideinc.solidequerydistributor.Main;

import java.io.IOException;
import java.util.Objects;

public class PageLoader implements PageLoaderInterface {

    public final String STYLESHEET_PATH = getStyleSheet();
    private Pane rootLayout;
    private Stage stage;

    @Override
    public void setRootLayout(Pane rootLayout) {
        this.rootLayout = rootLayout;
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void loadPage(String fxmlFile, String title) {
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

    @Override
    public void loadFirstPage() {
        loadLoginPage();
    }

    public void loadLoginPage() {
        loadPage("Login.fxml", "Solide™ Query Distributor - Login");
    }

    public void loadMainPage() {
        loadPage("Main.fxml", "Solide™ Query Distributor - Main");
    }

    public void loadAccountPage() {
        loadPage("Account.fxml", "Solide™ Query Distributor - Account");
    }
}