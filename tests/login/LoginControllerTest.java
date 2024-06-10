package login;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.solideinc.solidequerydistributor.Controllers.LoginController;
import org.solideinc.solidequerydistributor.Controllers.UserController;
import org.solideinc.solidequerydistributor.Main;
import org.solideinc.solidequerydistributor.Util.PageLoader;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoginControllerTest extends ApplicationTest {

    private LoginController loginController;
    private TextField usernameField;
    private TextField passwordField;
    private Button loginButton;
    private PageLoader originalPageLoader;

    @BeforeEach
    public void setUp() {
        loginController = new LoginController();
        usernameField = Mockito.mock(TextField.class);
        passwordField = Mockito.mock(TextField.class);
        loginButton = Mockito.mock(Button.class);

        loginController.loginUsernameTextField = usernameField;
        loginController.loginPasswordPasswordField = passwordField;
        loginController.loginButton = loginButton;

        // Save the original PAGE_LOADER
        originalPageLoader = Main.PAGE_LOADER;

        // Set up the mocked TextField to return a non-null ObservableList for getStyleClass()
        ObservableList<String> usernameStyleClass = FXCollections.observableArrayList();
        ObservableList<String> passwordStyleClass = FXCollections.observableArrayList();
        when(usernameField.getStyleClass()).thenReturn(usernameStyleClass);
        when(passwordField.getStyleClass()).thenReturn(passwordStyleClass);

        // Mock the PAGE_LOADER
        PageLoader mockPageLoader = Mockito.mock(PageLoader.class);
        Main.PAGE_LOADER = mockPageLoader;



    }

    @AfterEach
    public void tearDown() {
        // Reset the PAGE_LOADER to its original state
        Main.PAGE_LOADER = originalPageLoader;
    }

    @Test
    public void testLoginWithEmptyFields() {
        when(usernameField.getText()).thenReturn("");
        when(passwordField.getText()).thenReturn("");

        assertThrows(RuntimeException.class, () -> loginController.login());

        // Verify that loadMainPage was not called
        verify(Main.PAGE_LOADER, never()).loadMainPage();
    }

    @Test
    public void testLoginWithValidCredentials() throws Exception {
        when(usernameField.getText()).thenReturn("testUser");
        when(passwordField.getText()).thenReturn("testPassword");

        UserController.createUser("testEmail@test.com", "testUser", "testPassword", "en");

        assertDoesNotThrow(() -> loginController.login());

        // Verify that loadMainPage was called
        verify(Main.PAGE_LOADER).loadMainPage();
    }

    @Test
    public void testLoginWithInvalidCredentials() throws Exception {
        when(usernameField.getText()).thenReturn("testUser");
        when(passwordField.getText()).thenReturn("wrongPassword");

        UserController.createUser("testEmail@test.com", "testUser", "testPassword", "en");

        assertThrows(RuntimeException.class, () -> loginController.login());

        // Verify that loadMainPage was not called
        verify(Main.PAGE_LOADER, never()).loadMainPage();
    }
}