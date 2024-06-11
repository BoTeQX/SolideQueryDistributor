package org.solideinc.solidequerydistributor.Controllers;

import at.favre.lib.crypto.bcrypt.BCrypt;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.solideinc.solidequerydistributor.Classes.User;
import org.solideinc.solidequerydistributor.Main;
import java.io.IOException;
public class AccountController {
    @FXML
    private PasswordField oldPasswordPasswordField;
    @FXML
    private Button changePasswordButton;
    @FXML
    private TextField updateUsernameTextField;
    @FXML
    private PasswordField updatePasswordPasswordField;
    @FXML
    private PasswordField updateConfirmPasswordPasswordField;
    @FXML
    private TextField updateEmailTextField;
    @FXML
    private Button updateAccountButton;
    @FXML
    private Button exitAccountPageButton;

    private User user = LoginController.getLoggedInUser();
    @FXML
    private void initialize() {
        updateAccountButton.setOnAction(event -> saveAccountInformation());
        changePasswordButton.setOnAction(event -> changePassword());
        exitAccountPageButton.setOnAction(event -> mainPage());

        updateUsernameTextField.setText(user.getUsername());
        updateEmailTextField.setText(user.getEmail());
    }
    private void saveAccountInformation(){
        if (updateUsernameTextField.getText().equals(user.getUsername()) && updateEmailTextField.getText().equals(user.getEmail())){
            createAlertDialog("Er zijn geen veranderingen");
            return ;
        }
        if (updateUsernameTextField.getText().isEmpty() || updateEmailTextField.getText().isEmpty()){
            createAlertDialog("Vul alstublieft alle velden in");
            return ;
        }
        if (!updateUsernameTextField.getText().isEmpty() && !updateEmailTextField.getText().isEmpty()){
            user.setUsername(updateUsernameTextField.getText());
            user.setEmail(updateEmailTextField.getText());
            try {
                UserController.updateUsers();
            }catch (IOException e){
                throw new IllegalArgumentException(e);
            }
        }
        createConfirmDialog("Account informatie geupdate");
    }

    private void changePassword(){
        BCrypt.Result result = BCrypt.verifyer().verify(oldPasswordPasswordField.getText().toCharArray(), user.getPassword());
        if (!result.verified){
            createAlertDialog("Wachtwoord is incorrect.");
            return ;
        }
        if (updatePasswordPasswordField.getText().isEmpty() || updateConfirmPasswordPasswordField.getText().isEmpty() || oldPasswordPasswordField.getText().isEmpty()){
            createAlertDialog("Vul alstublieft alle velden in");
            return ;
        }
        if (!updatePasswordPasswordField.getText().equals(updateConfirmPasswordPasswordField.getText())){
            createAlertDialog("De wachtwoorden komen niet overeen");
            return ;
        }
        if(!updatePasswordPasswordField.getText().isEmpty() && !updateConfirmPasswordPasswordField.getText().isEmpty() && !oldPasswordPasswordField.getText().isEmpty() && updatePasswordPasswordField.getText().equals(updateConfirmPasswordPasswordField.getText())) {
            user.updatePassword(updatePasswordPasswordField.getText());
            try {
                UserController.updateUsers();
                createConfirmDialog("Wachtwoord geupdate");
            }catch (IOException e){
                throw new IllegalArgumentException(e);
            }
        }
        oldPasswordPasswordField.setText("");
        updatePasswordPasswordField.setText("");
        updateConfirmPasswordPasswordField.setText("");
    }
    private void mainPage(){
        Main.PAGE_LOADER.loadMainPage();
    }

    private void createAlertDialog(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Update fout");
        alert.setHeaderText(content);
        alert.setContentText(null);
        alert.showAndWait();
    }
    private void createConfirmDialog(String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Gegevens succesvol geupdate");
        alert.setHeaderText(content);
        alert.setContentText(null);
        alert.showAndWait();
    }
}
