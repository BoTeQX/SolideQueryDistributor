package org.solideinc.solidequerydistributor.Abstract;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.solideinc.solidequerydistributor.Interfaces.PageLoaderInterface;
import org.solideinc.solidequerydistributor.Main;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractPageLoader implements PageLoaderInterface {

    public final String STYLESHEET_PATH = getStyleSheet();
    protected Pane rootLayout;
    protected Stage stage;
    private static final Logger LOGGER = Logger.getLogger(AbstractPageLoader.class.getName());

    @Override
    public void setRootLayout(Pane rootLayout) {
        this.rootLayout = rootLayout;
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public final void loadPage(String fxmlFile, String title) {
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
            LOGGER.log(Level.SEVERE, "Failed to load page template", e);
        }
    }

    public abstract void loadLoginPage();

    public abstract void loadMainPage();

    public abstract void loadAccountPage();
}
