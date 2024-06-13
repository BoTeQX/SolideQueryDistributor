package login;

import org.junit.jupiter.api.Test;
import org.solideinc.solidequerydistributor.Controllers.UserController;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {

    @Test
    public void testCheckUser() throws IOException {
        // Arrange
        String username = "testUser";
        String password = "testPassword";
        UserController.createUser("testEmail@test.com", username, password, "en");

        // Act
        boolean result = UserController.checkUser(username, password);

        // Assert
        assertTrue(result, "The user should be able to login with correct credentials");
    }

    @Test
    public void testCheckUserWithWrongPassword() throws IOException {
        // Arrange
        String username = "testUser";
        String password = "testPassword";
        UserController.createUser("testEmail@test.com", username, password, "en");

        // Act
        boolean result = UserController.checkUser(username, "wrongPassword");

        // Assert
        assertFalse(result, "The user should not be able to login with incorrect password");
    }

    @Test
    public void testCheckUserWithNonExistentUser() throws IOException {
        // Arrange
        String username = "nonExistentUser";
        String password = "testPassword";

        // Act
        boolean result = UserController.checkUser(username, password);

        // Assert
        assertFalse(result, "Non-existent user should not be able to login");
    }
}