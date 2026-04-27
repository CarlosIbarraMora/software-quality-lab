package mx.edu.cetys.Software_Quality_Lab.users;

import mx.edu.cetys.Software_Quality_Lab.users.exceptions.InvalidUserDataException;
import mx.edu.cetys.Software_Quality_Lab.users.exceptions.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @InjectMocks
    UserService userService;

    @Test
    void saveUser_ShouldSaveUserCorrectly() {
        var request = new UserController.UserRequest(
                "ana123",
                "password123",
                "ana@test.com",
                "Ana",
                "Lopez"
        );

        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        var response = userService.saveUser(request);

        assertEquals("User saved successfully", response.info());
        assertEquals(1L, response.response().user().id());
        assertEquals("ana123", response.response().user().username());
        assertEquals("Ana", response.response().user().firstName());
        assertEquals("Lopez", response.response().user().lastName());
        assertEquals("ana@test.com", response.response().user().email());
        assertTrue(response.response().user().active());

        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void saveUser_WhenUsernameIsInvalid_ShouldThrowException() {
        var request = new UserController.UserRequest(
                "",
                "password123",
                "ana@test.com",
                "Ana",
                "Lopez"
        );

        assertThrows(InvalidUserDataException.class, () ->
                userService.saveUser(request)
        );

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getUserById_ShouldReturnUser() {
        User user = new User(
                1L,
                "ana123",
                "hash",
                "ana@test.com",
                "Ana",
                "Lopez",
                true
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        var response = userService.getUserById(1L);

        assertEquals("User info", response.info());
        assertEquals(1L, response.response().user().id());
        assertEquals("ana123", response.response().user().username());

        verify(userRepository).findById(1L);
    }

    @Test
    void getUserById_WhenUserDoesNotExist_ShouldThrowException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                userService.getUserById(99L)
        );

        verify(userRepository).findById(99L);
    }

    @Test
    void getAllUsers_ShouldReturnPagedUsers() {
        User user = new User(
                1L,
                "ana123",
                "hash",
                "ana@test.com",
                "Ana",
                "Lopez",
                true
        );

        Pageable pageable = PageRequest.of(0, 10);
        Page<User> page = new PageImpl<>(List.of(user), pageable, 1);

        when(userRepository.findAll(pageable)).thenReturn(page);

        var response = userService.getAllUsers(pageable);

        assertEquals("All users", response.info());
        assertEquals(1, response.response().getTotalElements());
        assertEquals("ana123", response.response().getContent().getFirst().user().username());

        verify(userRepository).findAll(pageable);
    }

    @Test
    void getByUsername_ShouldReturnUser() {
        User user = new User(
                1L,
                "ana123",
                "hash",
                "ana@test.com",
                "Ana",
                "Lopez",
                true
        );

        when(userRepository.findByUsername("ana123")).thenReturn(Optional.of(user));

        var response = userService.getByUsername("ana123");

        assertEquals("User info", response.info());
        assertEquals("ana123", response.response().user().username());

        verify(userRepository).findByUsername("ana123");
    }

    @Test
    void getByUsername_WhenUserDoesNotExist_ShouldThrowException() {
        when(userRepository.findByUsername("noexiste")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                userService.getByUsername("noexiste")
        );

        verify(userRepository).findByUsername("noexiste");
    }

    @Test
    void updateUser_ShouldUpdateOnlyProvidedFields() {
        User user = new User(
                1L,
                "ana123",
                "hash",
                "ana@test.com",
                "Ana",
                "Lopez",
                true
        );

        var request = new UserController.UpdateUserRequest(
                "anaUpdated",
                "updated@test.com",
                "Anita",
                "Perez",
                false
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var response = userService.updateUser(1L, request);

        assertEquals("User updated successfully", response.info());
        assertEquals("anaUpdated", response.response().user().username());
        assertEquals("Anita", response.response().user().firstName());
        assertEquals("Perez", response.response().user().lastName());
        assertEquals("updated@test.com", response.response().user().email());
        assertFalse(response.response().user().active());

        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void deleteUser_ShouldDeleteUser() {
        User user = new User(
                1L,
                "ana123",
                "hash",
                "ana@test.com",
                "Ana",
                "Lopez",
                true
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        var response = userService.deleteUser(1L);

        assertEquals("User deleted successfully", response.info());
        assertNull(response.response());
        assertNull(response.error());

        verify(userRepository).delete(user);
    }

    @Test
    void updatePassword_ShouldUpdatePasswordCorrectly() {
        User user = new User(
                1L,
                "ana123",
                "oldHash",
                "ana@test.com",
                "Ana",
                "Lopez",
                true
        );

        var request = new UserController.PasswordRequest(
                "oldPassword",
                "newPassword"
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("oldPassword", "oldHash")).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("newHash");

        var response = userService.updatePassword(1L, request);

        assertEquals("Password updated successfully", response.info());
        assertEquals("newHash", user.getPasswordHash());

        verify(userRepository).save(user);
    }

    @Test
    void updatePassword_WhenCurrentPasswordIsIncorrect_ShouldThrowException() {
        User user = new User(
                1L,
                "ana123",
                "oldHash",
                "ana@test.com",
                "Ana",
                "Lopez",
                true
        );

        var request = new UserController.PasswordRequest(
                "wrongPassword",
                "newPassword"
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "oldHash")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () ->
                userService.updatePassword(1L, request)
        );

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void login_ShouldReturnUserWhenCredentialsAreValid() {
        User user = new User(
                1L,
                "ana123",
                "hash",
                "ana@test.com",
                "Ana",
                "Lopez",
                true
        );

        var request = new UserController.LoginRequest("ana123", "password123");

        when(userRepository.findByUsername("ana123")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "hash")).thenReturn(true);

        var response = userService.login(request);

        assertEquals("Login successful", response.info());
        assertEquals("ana123", response.response().user().username());

        verify(userRepository).findByUsername("ana123");
    }

    @Test
    void login_WhenPasswordIsIncorrect_ShouldThrowException() {
        User user = new User(
                1L,
                "ana123",
                "hash",
                "ana@test.com",
                "Ana",
                "Lopez",
                true
        );

        var request = new UserController.LoginRequest("ana123", "wrongPassword");

        when(userRepository.findByUsername("ana123")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "hash")).thenReturn(false);

        assertThrows(UserNotFoundException.class, () ->
                userService.login(request)
        );
    }
    @Test
    void updateUser_WhenFieldsAreNull_ShouldKeepOriginalValues() {
        User user = new User(
                1L,
                "ana123",
                "hash",
                "ana@test.com",
                "Ana",
                "Lopez",
                true
        );

        var request = new UserController.UpdateUserRequest(
                null,
                null,
                null,
                null,
                null
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var response = userService.updateUser(1L, request);

        assertEquals("User updated successfully", response.info());
        assertEquals("ana123", response.response().user().username());
        assertEquals("Ana", response.response().user().firstName());
        assertEquals("Lopez", response.response().user().lastName());
        assertEquals("ana@test.com", response.response().user().email());
        assertTrue(response.response().user().active());

        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
    }
    @Test
    void updateUser_WhenUserDoesNotExist_ShouldThrowException() {
        var request = new UserController.UpdateUserRequest(
                "anaUpdated",
                "updated@test.com",
                "Anita",
                "Perez",
                false
        );

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                userService.updateUser(99L, request)
        );

        verify(userRepository).findById(99L);
        verify(userRepository, never()).save(any(User.class));
    }
    @Test
    void deleteUser_WhenUserDoesNotExist_ShouldThrowException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                userService.deleteUser(99L)
        );

        verify(userRepository).findById(99L);
        verify(userRepository, never()).delete(any(User.class));
    }
    @Test
    void updatePassword_WhenUserDoesNotExist_ShouldThrowException() {
        var request = new UserController.PasswordRequest(
                "oldPassword",
                "newPassword"
        );

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                userService.updatePassword(99L, request)
        );

        verify(userRepository).findById(99L);
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(userRepository, never()).save(any(User.class));
    }
}