package mx.edu.cetys.Software_Quality_Lab.users;
import mx.edu.cetys.Software_Quality_Lab.common.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

        var responseUser = new UserController.UserResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getActive()
        );
        return new ApiResponse<>("User saved successfully", new UserController.UserWrapper(responseUser), null);
    }
}
