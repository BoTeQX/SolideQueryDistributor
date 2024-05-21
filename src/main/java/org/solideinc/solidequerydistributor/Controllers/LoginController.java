package org.solideinc.solidequerydistributor.Controllers;


import at.favre.lib.crypto.bcrypt.BCrypt;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import org.solideinc.solidequerydistributor.Classes.User;


public class LoginController {
    @FXML
    private Pane RootLayout;

    @FXML
    private Button loginButton;

    @FXML
    private TextField loginUsernameTextField;

    @FXML
    private TextField loginPasswordPasswordField;

    @FXML
    private void initialize() {
        String password = "test";
        String hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        UserController.createUser("test@test.nl", "test", hashedPassword, "nl");
        loginButton.setOnAction(event -> login());
    }

    private String getUsername() {
        return loginUsernameTextField.getText();
    }

    private String getPassword() {
        return loginPasswordPasswordField.getText();
    }

    private void clearFields() {
        loginUsernameTextField.clear();
        loginPasswordPasswordField.clear();
    }

    private boolean checkUsernameField(String username) {
        if (username.isEmpty()) {
            if (!loginUsernameTextField.getStyleClass().contains("text-field-error")) {
                loginUsernameTextField.getStyleClass().add("text-field-error");
            }
            return true;
        } else {
            loginUsernameTextField.getStyleClass().remove("text-field-error");
            return false;
        }
    }

    private boolean checkPasswordField(String password) {
        if (password.isEmpty()) {
            if (!loginPasswordPasswordField.getStyleClass().contains("text-field-error")) {
                loginPasswordPasswordField.getStyleClass().add("text-field-error");
            }
            return true;
        } else {
            loginPasswordPasswordField.getStyleClass().remove("text-field-error");
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

    private void validateLoginCredentials (String username, String password) {
        if (UserController.checkUser(username, password)) {
            redirectUser();
        } else {
            createAlertDialog("ongeldige gebruikersnaam of wachtwoord");
            clearFields();
        }
    }

    private void redirectUser() {
        // Redirect user to the main screen
        System.out.println("Redirecting user to the main screen");
    }

    private void login() {
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
