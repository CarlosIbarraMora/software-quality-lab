package mx.edu.cetys.Software_Quality_Lab.users;
import mx.edu.cetys.Software_Quality_Lab.users.exceptions.*;
import mx.edu.cetys.Software_Quality_Lab.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserControllerAdvice {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ApiResponse<Void> handleInvalidPetException(invalidUserDataException e) {
        return new ApiResponse<>("Invalid user info", null, e.getMessage());
    }
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ApiResponse<Void> handlePetNotFoundException(userNotFoundException e) {
        return new ApiResponse<>("User not found", null, e.getMessage());
    }
}
