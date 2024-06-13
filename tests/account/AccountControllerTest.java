package account;

import at.favre.lib.crypto.bcrypt.BCrypt;
import javafx.application.Platform;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.solideinc.solidequerydistributor.Classes.User;
import org.solideinc.solidequerydistributor.Controllers.AccountController;
import org.solideinc.solidequerydistributor.Controllers.LoginController;
import org.solideinc.solidequerydistributor.Controllers.UserController;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountControllerTest extends ApplicationTest {
    private AccountController accountController;
    private User user;

    @BeforeEach
    void setUp() throws IOException {
        // Mock the elements
        TextField updateUsernameTextField = mock(TextField.class);
        TextField updateEmailTextField = mock(TextField.class);
        PasswordField oldPasswordPasswordField = mock(PasswordField.class);
        PasswordField updatePasswordPasswordField = mock(PasswordField.class);
        PasswordField updateConfirmPasswordPasswordField = mock(PasswordField.class);

        // Set up any necessary behavior for the mocks
        when(updateUsernameTextField.getText()).thenReturn("");
        when(updateEmailTextField.getText()).thenReturn("");
        when(oldPasswordPasswordField.getText()).thenReturn("");
        when(updatePasswordPasswordField.getText()).thenReturn("");
        when(updateConfirmPasswordPasswordField.getText()).thenReturn("");

        // Create a test user and set it as logged in
        UserController.createUser("testEmail@test.com", "testUser", "testPassword", "en");
        user = UserController.getUser("testUser");
        LoginController.setLoggedInUser(user);

        // Create a real AccountController instance after setting the logged-in user
        accountController = new AccountController();

        // Inject the mock fields into the controller
        accountController.updateUsernameTextField = updateUsernameTextField;
        accountController.updateEmailTextField = updateEmailTextField;
        accountController.oldPasswordPasswordField = oldPasswordPasswordField;
        accountController.updatePasswordPasswordField = updatePasswordPasswordField;
        accountController.updateConfirmPasswordPasswordField = updateConfirmPasswordPasswordField;
    }

    @Test
    void testUpdatePassword() {
        // Simulate user input
        when(accountController.updatePasswordPasswordField.getText()).thenReturn("newPassword");
        when(accountController.updateConfirmPasswordPasswordField.getText()).thenReturn("newPassword");
        when(accountController.oldPasswordPasswordField.getText()).thenReturn("testPassword");

        // Invoke the method under test
        Platform.runLater(() -> accountController.changePassword());

        // Wait for JavaFX thread to finish
        WaitForAsyncUtils.waitForFxEvents();

        // Verify the result
        assertTrue(BCrypt.verifyer().verify("newPassword".toCharArray(), user.getPassword()).verified);
    }

    @Test
    void testUpdateEmail()  {
        // Simulate user input
        when(accountController.updateEmailTextField.getText()).thenReturn("newEmail@example.com");
        when(accountController.updateUsernameTextField.getText()).thenReturn("testUser");

        // Invoke the method under test
        Platform.runLater(() -> accountController.saveAccountInformation());

        // Wait for JavaFX thread to finish
        WaitForAsyncUtils.waitForFxEvents();

        // Verify the result
        assertEquals("newEmail@example.com", user.getEmail());
    }

    @Test
    void testUpdateUsername() {
        // Simulate user input
        when(accountController.updateUsernameTextField.getText()).thenReturn("newUsername");
        when(accountController.updateEmailTextField.getText()).thenReturn("testEmail@test.com");

        // Invoke the method under test
        Platform.runLater(() -> accountController.saveAccountInformation());

        // Wait for JavaFX thread to finish
        WaitForAsyncUtils.waitForFxEvents();

        // Verify the result
        assertEquals("newUsername", user.getUsername());
    }
}
