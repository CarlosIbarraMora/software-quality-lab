package mx.edu.cetys.Software_Quality_Lab.users;
import mx.edu.cetys.Software_Quality_Lab.common.*;

import mx.edu.cetys.Software_Quality_Lab.users.exceptions.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    ApiResponse<UserController.UserWrapper> saveUser(UserController.UserRequest userRequest) {
        UserInfoValidator.isValid(userRequest);

        var savedUser = userRepository.save(
                new User(userRequest.username(),
                        passwordEncoder.encode(userRequest.password()),
                        userRequest.email(),
                        userRequest.firstName(),
                        userRequest.lastName(),
                        true));

        var responseUser = mapToResponse(savedUser);
        return new ApiResponse<>("User saved successfully", new UserController.UserWrapper(responseUser), null);
    }

    ApiResponse<UserController.UserWrapper> getUserById(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + id + " was not found"));
        var response = mapToResponse(user);

        return new ApiResponse<>("User info", new UserController.UserWrapper(response), null);
    }

    ApiResponse<Page<UserController.UserWrapper>> getAllUsers(Pageable pageable) {
        Page<User> usersPage = userRepository.findAll(pageable);

        Page<UserController.UserWrapper> wrappedResponse = usersPage.map(user ->
                new UserController.UserWrapper(mapToResponse(user))
        );

        return new ApiResponse<>("All users", wrappedResponse, null);
    }
    //ApiResponse<UserController.UserWrapper> updateUser(UserController.UserRequest userRequest) {}
    ApiResponse<UserController.UserWrapper> getByUsername(String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with name: " + username + " was not found"));
        var wrappedResponse = new UserController.UserWrapper(mapToResponse(user));
        return new ApiResponse<>("User info", wrappedResponse, null);
    }

    ApiResponse<UserController.UserWrapper> updateUser(
            Long id,
            UserController.UpdateUserRequest request
    ) {
        var user = findUserOrThrow(id);

        if (request.username() != null) {
            user.setUsername(request.username());
        }

        if (request.email() != null) {
            user.setEmail(request.email());
        }

        if (request.firstName() != null) {
            user.setFirstName(request.firstName());
        }

        if (request.lastName() != null) {
            user.setLastName(request.lastName());
        }

        if (request.active() != null) {
            user.setActive(request.active());
        }

        var updatedUser = userRepository.save(user);

        return new ApiResponse<>(
                "User updated successfully",
                new UserController.UserWrapper(mapToResponse(updatedUser)),
                null
        );
    }

    ApiResponse<Void> deleteUser(Long id) {
        var user = findUserOrThrow(id);

        userRepository.delete(user);

        return new ApiResponse<>("User deleted successfully", null, null);
    }

    ApiResponse<Void> updatePassword(
            Long id,
            UserController.PasswordRequest request
    ) {
        var user = findUserOrThrow(id);

        if (!passwordEncoder.matches(request.currentPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);

        return new ApiResponse<>("Password updated successfully", null, null);
    }

    ApiResponse<UserController.UserWrapper> login(UserController.LoginRequest request) {
        var user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new UserNotFoundException("Invalid username or password"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new UserNotFoundException("Invalid username or password");
        }

        return new ApiResponse<>(
                "Login successful",
                new UserController.UserWrapper(mapToResponse(user)),
                null
        );
    }

    private User findUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(
                        "User with id: " + id + " was not found"
                ));
    }

    private UserController.UserResponse mapToResponse(User user) {
        return new UserController.UserResponse(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getActive()
        );
    }
}
