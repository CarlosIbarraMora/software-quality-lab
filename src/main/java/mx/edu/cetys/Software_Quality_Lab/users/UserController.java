package mx.edu.cetys.Software_Quality_Lab.users;
import mx.edu.cetys.Software_Quality_Lab.common.*;

import mx.edu.cetys.Software_Quality_Lab.pets.PetController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    record UserRequest(String username, String password, String email, String firstName, String lastName) {}
    record UserResponse(Long id, String username, String firstName, String lastName, String email, Boolean active) {}
    record UserWrapper(UserResponse user) {}

    //POST user
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ApiResponse<UserWrapper> createUser(@RequestBody UserRequest userRequest) {
        return userService.saveUser(userRequest);
    }

    //GET user by id

    //GET all useres TODO pagination

    //GET by username

    //UPDATE user info

    //DELETE user

    //PATCH password

    // "LOGIN"
}
