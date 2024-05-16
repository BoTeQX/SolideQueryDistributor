package org.solideinc.solidequerydistributor.Util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

public class FxmlLoader {
    public static void load(VBox rootLayout, String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(FxmlLoader.class.getResource(fxml));
            Parent newContent = loader.load();
            rootLayout.getChildren().setAll(newContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
