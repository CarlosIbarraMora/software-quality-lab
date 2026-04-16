package mx.edu.cetys.Software_Quality_Lab.pets;

import mx.edu.cetys.Software_Quality_Lab.pets.exceptions.invalidPetDataException;
import mx.edu.cetys.Software_Quality_Lab.pets.exceptions.petNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class PetControllerAdvice {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    PetController.ApiResponse<Void> handleInvalidPetException(invalidPetDataException e) {
        return new PetController.ApiResponse<>("Invalid pet info", null, e.getMessage());
    }
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    PetController.ApiResponse<Void> handlePetNotFoundException(petNotFoundException e) {
        return new PetController.ApiResponse<>("Pet not found", null, e.getMessage());
    }
}
