package org.solideinc.solidequerydistributor.Interfaces;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public interface PageLoaderInterface {

    String STYLE_SHEET = "/org/solideinc/solidequerydistributor/style.css";

    void setRootLayout(Pane rootLayout);

    void setStage(Stage stage);

    default void startApplication() {
        Stage primaryStage = new Stage();
        setStage(primaryStage);
        setRootLayout(new Pane());
        loadFirstPage();
    }

    void loadPage(String fxmlFile, String title);

    void loadFirstPage();

    default String getStyleSheet() {
        return STYLE_SHEET;
    }
}
