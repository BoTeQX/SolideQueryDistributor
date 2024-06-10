package org.solideinc.solidequerydistributor.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.solideinc.solidequerydistributor.Classes.User;
import org.solideinc.solidequerydistributor.Main;
import java.io.IOException;

public class LoginController {

    @FXML
    private Button loginButton;

    @FXML
    private TextField loginUsernameTextField;

    @FXML
    private TextField loginPasswordPasswordField;

    public static final String TEXT_FIELD_ERROR_CSS_CLASS = "text-field-error";

    @FXML
    private void initialize() {
        // UserController.createUser("admin@admin.nl", "adminx", "adminx", "nl");
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

    private boolean checkUsernameField(String username) {
        if (username.isEmpty()) {
            if (!loginUsernameTextField.getStyleClass().contains(TEXT_FIELD_ERROR_CSS_CLASS)) {
                loginUsernameTextField.getStyleClass().add(TEXT_FIELD_ERROR_CSS_CLASS);
            }
            return true;
        } else {
            loginUsernameTextField.getStyleClass().remove(TEXT_FIELD_ERROR_CSS_CLASS);
            return false;
        }
    }

    private boolean checkPasswordField(String password) {
        if (password.isEmpty()) {
            if (!loginPasswordPasswordField.getStyleClass().contains(TEXT_FIELD_ERROR_CSS_CLASS)) {
                loginPasswordPasswordField.getStyleClass().add(TEXT_FIELD_ERROR_CSS_CLASS);
            }
            return true;
        } else {
            loginPasswordPasswordField.getStyleClass().remove(TEXT_FIELD_ERROR_CSS_CLASS);
            return false;
        }
    }

    private boolean checkIfEmptyFields(String username, String password) {
        boolean isUsernameEmpty = checkUsernameField(username);
        boolean isPasswordEmpty = checkPasswordField(password);

        if (isUsernameEmpty || isPasswordEmpty) {
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
        Main.PAGE_LOADER.loadMainPage();
    }

    private void login() throws IOException {
        String username = getUsername();
        String password = getPassword();

        if(!checkIfEmptyFields(username, password)) {
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
