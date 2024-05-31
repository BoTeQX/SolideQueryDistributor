package org.solideinc.solidequerydistributor.Controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.solideinc.solidequerydistributor.Classes.User;
import org.solideinc.solidequerydistributor.Util.PageLoader;
import org.solideinc.solidequerydistributor.Util.SolideAPI;

import javax.imageio.IIOException;
import java.awt.*;
import java.io.IOException;

public class AccountController {
    @FXML
    private ComboBox<String> updateLanguageComboBox;
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

    private User user = LoginController.loggedInUser;
    @FXML
    private void initialize() {
        updateAccountButton.setOnAction(event -> saveAccountInformation());
        changePasswordButton.setOnAction(event -> changePassword());
        exitAccountPageButton.setOnAction(event -> mainPage());

        updateLanguageComboBox.setOnAction(event -> updateLanguageSetting());

        updateUsernameTextField.setText(user.getUsername());
        updateEmailTextField.setText(user.getEmail());
        ObservableList<String>  options = updateLanguageComboBox.getItems();
        options.add("nl");
        options.add("en");
        updateLanguageComboBox.setValue(user.getLanguagePreference());
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
                throw new RuntimeException(e);
            }
        }
        createConfirmDialog("Account informatie geupdate");
    }

    private void updateLanguageSetting(){
        user.setLanguagePreference(updateLanguageComboBox.getValue());
        try {
            UserController.updateUsers();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    private void changePassword(){
        if (updatePasswordPasswordField.getText().isEmpty() || updateConfirmPasswordPasswordField.getText().isEmpty()){
            createAlertDialog("Vul alstublieft alle velden in");
            return ;
        }
        if (!updatePasswordPasswordField.getText().equals(updateConfirmPasswordPasswordField.getText())){
            createAlertDialog("De wachtwoorden komen niet over een");
            return ;
        }
        if(!updatePasswordPasswordField.getText().isEmpty() && !updateConfirmPasswordPasswordField.getText().isEmpty() && updatePasswordPasswordField.getText().equals(updateConfirmPasswordPasswordField.getText())) {
            user.updatePassword(updatePasswordPasswordField.getText());
            try {
                UserController.updateUsers();
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        }
        createConfirmDialog("Wachtwoord geupdate");
        updatePasswordPasswordField.setText("");
        updateConfirmPasswordPasswordField.setText("");
    }
    private void mainPage(){
        PageLoader.loadMainPage();
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
