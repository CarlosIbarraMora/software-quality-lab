package mx.edu.cetys.validators;


import Validators.EmailValidatorService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EmailValidatorServiceTest {
    @Test
    void shouldReturnFalseWhenEmailIsNull(){
        //Arrange
        EmailValidatorService emailValidator = new EmailValidatorService();
        //Act
        var isValid = emailValidator.isValid(null);

        //Assert
        assertFalse(isValid);
    }

    @Test
    void shouldReturnFalseWhenEmailIsEmpty(){
        //Arrange
        EmailValidatorService emailValidator = new EmailValidatorService();
        //Act
        var isValid = emailValidator.isValid("");
        //Assert
        assertFalse(isValid);
    }
}
