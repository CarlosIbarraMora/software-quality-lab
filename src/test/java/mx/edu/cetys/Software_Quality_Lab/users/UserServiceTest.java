package mx.edu.cetys.Software_Quality_Lab.users;

import mx.edu.cetys.Software_Quality_Lab.users.exceptions.InvalidUserDataException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)

public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    UserController.UserRequest userRequest;

    //Happy path
    @Test
    void shouldCreateUserWithCorrectValues() {
        var request = new UserController.UserRequest(
                "AndyPro",
                "Roblox123",
                "roblox@gmail.com",
                "Andres",
                "Silva");

        var savedUser = new User(
                1L,
                request.username(),
                request.password(),
                request.email(),
                request.firstName(),
                request.lastName(),
                true
        );


        // Assert
        assertEquals(1L, savedUser.getId());
        assertEquals("AndyPro", savedUser.getUsername());
        assertEquals("Roblox123", savedUser.getPasswordHash());
        assertEquals("roblox@gmail.com", savedUser.getEmail());
        assertEquals("Andres", savedUser.getFirstName());
        assertEquals("Silva", savedUser.getLastName());
        assertTrue(savedUser.getActive());
    }

    //Invalid name exception
    @Test
    void saveUser_InvalidName_ExceptionExpected(){
        //Arrange
        var userRequest = new UserController.UserRequest(
                "An",
                "Roblox123",
                "roblox@gmail.com",
                "Andres",
                "Silva"
        );

        //Act
        assertThrows(InvalidUserDataException.class, () -> userService.saveUser(userRequest));
    }
    //Invalid password because empty
    @Test
    void saveUser_InvalidPassword_ExceptionExpected() {
        //Arrange
        var userRequest = new UserController.UserRequest(
                "AndyPro",
                "",
                "roblox@gmail.com",
                "Andres",
                "Silva"
        );
        //Act
        assertThrows(InvalidUserDataException.class, () -> userService.saveUser(userRequest));
    }

    //Invalid email format
    @Test
    void saveUser_InvalidEmailFormat_ExceptionExpected() {
        //Arrange
        var userRequest = new UserController.UserRequest(
                "AndyPro",
                "Roblox67",
                "robloxgmailcom",
                "Andres",
                "Silva"
        );
        //Act
        assertThrows(InvalidUserDataException.class, () -> userService.saveUser(userRequest));
    }
    //Invalid email
    @Test
    void saveUser_BlankEmail_ExceptionExpected() {
        //Arrange
        var userRequest = new UserController.UserRequest(
                "AndyPro",
                "Roblox67",
                "",
                "Andres",
                "Silva"
        );
        //Act
        assertThrows(InvalidUserDataException.class, () -> userService.saveUser(userRequest));
    }
    //Invalid email
    @Test
    void saveUser_NullEmail_ExceptionExpected() {
        //Arrange
        var userRequest = new UserController.UserRequest(
                "AndyPro",
                "Roblox67",
                null,
                "Andres",
                "Silva"
        );
        //Act
        assertThrows(InvalidUserDataException.class, () -> userService.saveUser(userRequest));
    }

}
