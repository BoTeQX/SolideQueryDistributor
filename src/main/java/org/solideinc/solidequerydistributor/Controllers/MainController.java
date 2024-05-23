package org.solideinc.solidequerydistributor.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.solideinc.solidequerydistributor.Util.PageLoader;

public class MainController {
    @FXML
    private Button logoutButton;

    @FXML
    private void initialize() {
        logoutButton.setOnAction(event -> logout());
    }


    private void logout(){
        PageLoader.loadLoginPage();
    }
}
