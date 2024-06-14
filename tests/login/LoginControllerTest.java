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
import org.solideinc.solidequerydistributor.Util.Observer;
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

        // Save the original pageLoader
        originalPageLoader = Main.pageLoader;

        // Set up the mocked TextField to return a non-null ObservableList for getStyleClass()
        ObservableList<String> usernameStyleClass = FXCollections.observableArrayList();
        ObservableList<String> passwordStyleClass = FXCollections.observableArrayList();
        when(usernameField.getStyleClass()).thenReturn(usernameStyleClass);
        when(passwordField.getStyleClass()).thenReturn(passwordStyleClass);

        // Mock the pageLoader
        PageLoader mockPageLoader = Mockito.mock(PageLoader.class);
        Main.pageLoader = mockPageLoader;


        loginController.usernameObserver = Mockito.mock(Observer.class);
        loginController.passwordObserver = Mockito.mock(Observer.class);



    }

    @AfterEach
    public void tearDown() {
        // Reset the pageLoader to its original state
        Main.pageLoader = originalPageLoader;
    }

    @Test
    public void testLoginWithEmptyFields() {
        when(usernameField.getText()).thenReturn("");
        when(passwordField.getText()).thenReturn("");

        assertThrows(RuntimeException.class, () -> loginController.login());

        // Verify that loadMainPage was not called
        verify(Main.pageLoader, never()).loadMainPage();
    }

    @Test
    public void testLoginWithValidCredentials() throws Exception {
        when(usernameField.getText()).thenReturn("testUser");
        when(passwordField.getText()).thenReturn("testPassword");

        UserController.createUser("testEmail@test.com", "testUser", "testPassword", "en");

        assertDoesNotThrow(() -> loginController.login());

        // Verify that loadMainPage was called
        verify(Main.pageLoader).loadMainPage();
    }

    @Test
    public void testLoginWithInvalidCredentials() throws Exception {
        when(usernameField.getText()).thenReturn("testUser");
        when(passwordField.getText()).thenReturn("wrongPassword");

        UserController.createUser("testEmail@test.com", "testUser", "testPassword", "en");

        assertThrows(RuntimeException.class, () -> loginController.login());

        // Verify that loadMainPage was not called
        verify(Main.pageLoader, never()).loadMainPage();
    }
}