package mx.edu.cetys.Software_Quality_Lab.users;

import mx.edu.cetys.Software_Quality_Lab.pets.Pet;
import mx.edu.cetys.Software_Quality_Lab.pets.PetController;
import mx.edu.cetys.Software_Quality_Lab.pets.exceptions.invalidPetDataException;
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
        //var petRequest = new PetController.PetRequest("L", "Negro", "Perro", 2);

        //Act
        //assertThrows(invalidPetDataException.class, () -> petService.savePet(petRequest));
    }
}
