package mx.edu.cetys.Software_Quality_Lab.users;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    private record UserRequest(String username, String password, String firstName, String lastName) {}
    private record UserResponse(Long id, String username, String password, String firstName, String lastName, Boolean active) {}

    //POST user

    //GET user by id

    //GET all useres TODO pagination

    //UPDATE user info

    //DELETE user

    //PATCH password

    // "LOGIN"
}
