package org.solideinc.solidequerydistributor.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.solideinc.solidequerydistributor.Classes.User;
import org.solideinc.solidequerydistributor.Main;
import org.solideinc.solidequerydistributor.Util.Observer;

import java.io.IOException;

public class LoginController {

    @FXML
    public Button loginButton;

    @FXML
    public TextField loginUsernameTextField;

    @FXML
    public TextField loginPasswordPasswordField;

    private Observer usernameObserver;
    private Observer passwordObserver;

    @FXML
    private void initialize() {
        // UserController.createUser("admin@admin.nl", "adminx", "adminx", "nl");
        setObservers();

        loginButton.setOnAction(event -> {
            try {
                login();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        });
    }

    private static User loggedInUser = null;

    private String getUsername() {
        return loginUsernameTextField.getText();
    }

    private String getPassword() {
        return loginPasswordPasswordField.getText();
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(User user) {
        loggedInUser = user;
    }

    private void clearFields() {
        loginUsernameTextField.clear();
        loginPasswordPasswordField.clear();
    }

    private void setObservers() {
        usernameObserver = new Observer(loginUsernameTextField);
        passwordObserver = new Observer(loginPasswordPasswordField);
    }

    private boolean checkIfEmptyFields() {
        String username = loginUsernameTextField.getText();
        String password = loginPasswordPasswordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            createAlertDialog("Vul alstublieft alle velden in");
            return true;
        }

        return false;
    }

    private void validateLoginCredentials (String username, String password) throws IOException {
        if (UserController.checkUser(username, password)) {
            setLoggedInUser(UserController.getUser(username));
            redirectUser();
        } else {
            createAlertDialog("ongeldige gebruikersnaam of wachtwoord");
            clearFields();
        }
    }

    private void redirectUser() {
        Main.pageLoader.loadMainPage();
    }

    public void login() throws IOException {
        // Check if the fields are empty when the user clicks the login button
        usernameObserver.checkAndApplyStyle();
        passwordObserver.checkAndApplyStyle();

        String username = getUsername();
        String password = getPassword();

        if(!checkIfEmptyFields()) {
            validateLoginCredentials(username, password);
        }
    }

    private void createAlertDialog(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login fout");
        alert.setHeaderText(content);
        alert.setContentText(null);
        alert.showAndWait();
    }
}
