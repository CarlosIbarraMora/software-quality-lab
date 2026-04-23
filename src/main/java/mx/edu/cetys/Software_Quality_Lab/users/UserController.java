package mx.edu.cetys.Software_Quality_Lab.users;
import mx.edu.cetys.Software_Quality_Lab.common.*;

import mx.edu.cetys.Software_Quality_Lab.pets.PetController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    record UserRequest(String username, String password, String email, String firstName, String lastName) {}
    record UpdateUserRequest(
            String username,
            String email,
            String firstName,
            String lastName,
            Boolean active
    ) {}

    record PasswordRequest(
            String currentPassword,
            String newPassword
    ) {}

    record LoginRequest(
            String username,
            String password
    ) {}
    record UserResponse(Long id, String username, String firstName, String lastName, String email, Boolean active) {}
    record UserWrapper(UserResponse user) {}

    //POST user
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ApiResponse<UserWrapper> createUser(@RequestBody UserRequest userRequest) {
        return userService.saveUser(userRequest);
    }

    //GET user by id
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ApiResponse<UserWrapper> getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    //GET all users
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    ApiResponse<Page<UserWrapper>> getUsers(Pageable pageable) {
        return userService.getAllUsers(pageable);
    }
    //GET by username
    @GetMapping("/username/{username}")
    @ResponseStatus(HttpStatus.OK)
    ApiResponse<UserWrapper> getUserByUsername(@PathVariable String username) {
        return userService.getByUsername(username);
    }
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ApiResponse<UserWrapper> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request
    ) {
        return userService.updateUser(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ApiResponse<Void> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    @PatchMapping("/{id}/password")
    @ResponseStatus(HttpStatus.OK)
    ApiResponse<Void> updatePassword(
            @PathVariable Long id,
            @RequestBody PasswordRequest request
    ) {
        return userService.updatePassword(id, request);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    ApiResponse<UserWrapper> login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }
}
