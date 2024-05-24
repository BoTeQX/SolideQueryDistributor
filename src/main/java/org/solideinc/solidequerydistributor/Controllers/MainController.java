package org.solideinc.solidequerydistributor.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import org.solideinc.solidequerydistributor.Util.PageLoader;

public class MainController {
    @FXML
    private Button logoutButton;

    @FXML
    private Pane sidebar;

    @FXML
    private Pane mainContent;

    @FXML
    private Button toggleButton;

    @FXML
    private TextArea text;

    @FXML
    private Circle sendCircle;

    @FXML
    private Button sendButton;

    private boolean isSidebarVisible = true;


    @FXML
    private void initialize() {
        logoutButton.setOnAction(event -> logout());
        toggleButton.setOnAction(this::handleToggleAction);
    }


    private void logout(){
        PageLoader.loadLoginPage();
    }

    private void handleToggleAction(ActionEvent event) {
        if (isSidebarVisible) {
            hideSidebar();
        } else {
            showSidebar();
        }
        isSidebarVisible = !isSidebarVisible;
    }

    private void hideSidebar(){
        sidebar.setPrefWidth(15);
        mainContent.setLayoutX(15);
        mainContent.setPrefWidth(900);
        text.setPrefWidth(770);
        sendButton.setLayoutX(820);
        sendCircle.setLayoutX(850);
        toggleButton.setText(">");
        toggleButton.setLayoutX(0);
    }

    private void showSidebar(){
        sidebar.setPrefWidth(260);
        mainContent.setPrefWidth(640);
        mainContent.setLayoutX(260);
        text.setPrefWidth(518);
        sendButton.setLayoutX(562);
        sendCircle.setLayoutX(591);
        toggleButton.setText("<");
        toggleButton.setLayoutX(205);
    }
}
