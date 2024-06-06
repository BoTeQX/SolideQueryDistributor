package org.solideinc.solidequerydistributor.Interfaces;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public interface PageLoaderInterface {

    String stylesheet = "/org/solideinc/solidequerydistributor/style.css";

    void setRootLayout(Pane rootLayout);

    void setStage(Stage stage);

    void startApplication();

    void loadPage(String fxmlFile, String title);

    void loadFirstPage();

    default String getStyleSheet() {
        return stylesheet;
    }
}
