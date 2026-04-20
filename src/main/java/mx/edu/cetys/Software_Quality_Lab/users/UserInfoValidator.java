package mx.edu.cetys.Software_Quality_Lab.users;

import mx.edu.cetys.Software_Quality_Lab.users.exceptions.invalidUserDataException;

public class UserInfoValidator {
    public static void isValid(UserController.UserRequest request) {

        if(request.username() == null || request.username().isEmpty()){
            throw new invalidUserDataException("Please enter a valid username.");
        }
        if(request.username().length() < 3){
            throw new invalidUserDataException("Username length must be at least 3.");
        }
        if(request.password() == null || request.password().isEmpty()){
            throw new invalidUserDataException("Please enter a valid password.");
        }
        if(request.firstName() == null || request.firstName().isEmpty()){
            throw new invalidUserDataException("Please enter a valid first name.");
        }
        if(request.lastName() == null || request.lastName().isEmpty()){
            throw new invalidUserDataException("Please enter a valid last name.");
        }
        if (request.email() == null || request.email().isBlank()) {
            throw new invalidUserDataException("Email is required");
        }
        if (!request.email().contains("@") || !request.email().contains(".")) {
            throw new invalidUserDataException("Invalid email format");
        }

    }
}
