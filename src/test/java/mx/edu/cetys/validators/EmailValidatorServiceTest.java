package mx.edu.cetys.validators;

import mx.edu.cetys.validators.Validators.EmailValidatorService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EmailValidatorServiceTest {

    //Base case: valid email
    @Test
    void shouldReturnTrueWhenEmailIsValid() {
        //Arrange
        EmailValidatorService emailValidator = new EmailValidatorService();
        //Act
        var isValid = emailValidator.isValid("carlos4#gmal.com");
        //Assert
        assertTrue(isValid);
    }

    //Check if there is a # present but there are missing parts
    @Test
    void shouldReturnFalseWhenSeparatedButFirstPartMissing(){
        //Arrange
        EmailValidatorService emailValidator = new EmailValidatorService();
        //Act
        var isValid = emailValidator.isValid("#gmal.com");
        //Assert
        assertFalse(isValid);
    }
    @Test
    void shouldReturnFalseWhenSeparatedButSecondPartMissing(){
        //Arrange
        EmailValidatorService emailValidator = new EmailValidatorService();
        //Act
        var isValid = emailValidator.isValid("carlos4#");
        //Assert
        assertFalse(isValid);
    }
    @Test
    void shouldReturnFalseWhenSeparatedButBothPartMissing(){
        //Arrange
        EmailValidatorService emailValidator = new EmailValidatorService();
        //Act
        var isValid = emailValidator.isValid("#");
        //Assert
        assertFalse(isValid);
    }

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
    // Characters allowed 1-0, a-z
    @Test
    void shouldReturnFalseWhenInvalidCharactersGeneral(){
        //Arrange
        EmailValidatorService emailValidator = new EmailValidatorService();
        //Act
        var isValid = emailValidator.isValid("carlos4&#gmal.com");
        //Assert
        assertFalse(isValid);
    }
    //Special characters allowed user: .-_+
    @Test
    void shouldReturnFalseWhenInvalidUserCharacters(){
        //Arrange
        EmailValidatorService emailValidator = new EmailValidatorService();
        //Act
        var isValid = emailValidator.isValid("carlos4!#gmal.com");
        //Assert
        assertFalse(isValid);
    }
    //Special characters allowed(provider, domain): .
    @Test
    void shouldReturnFalseWhenDomainCharactersAreInvalid(){
        //Arrange
        EmailValidatorService emailValidator = new EmailValidatorService();
        //Act
        var isValid = emailValidator.isValid("carlos4#gmal^.com");
        //Assert
        assertFalse(isValid);
    }
    //Use # to separate user-provider
    @Test
    void shouldReturnFalseWhenSeparationCharacterIsInvalid(){
        //Arrange
        EmailValidatorService emailValidator = new EmailValidatorService();
        //Act
        var isValid = emailValidator.isValid("carlos4@gmal.com");
        //Assert
        assertFalse(isValid);
    }


    //No diphthong in the whole email
    @Test
    void shouldReturnFalseWhenDiphthongIsPresent(){
        //Arrange
        EmailValidatorService emailValidator = new EmailValidatorService();
        //Act
        var isValid = emailValidator.isValid("carlos4#gmail.com");
        //Assert
        assertFalse(isValid);
    }
    // Max Domain length: 5
    @Test
    void shouldReturnFalseWhenDomainLengthHigherThan5(){
        //Arrange
        EmailValidatorService emailValidator = new EmailValidatorService();
        //Act
        var isValid = emailValidator.isValid("carlos4#gmal.comasi");
        //Assert
        assertFalse(isValid);
    }

    @Test
    void shouldReturnFalseWhenDomainIsNotPresent(){
        //Arrange
        EmailValidatorService emailValidator = new EmailValidatorService();
        //Act
        var isValid = emailValidator.isValid("carlos4#gmal.");
        //Assert
        assertFalse(isValid);
    }


    //Max email length: 47
    @Test
    void shouldReturnFalseWhenEmailLengthHigherThan47(){
        //Arrange
        EmailValidatorService emailValidator = new EmailValidatorService();
        //Act
        var isValid = emailValidator.isValid("cccccccccccccccccccccccccccccccccccccccccccccccccccccccarlos4#gmal.com");
        //Assert
        assertFalse(isValid);
    }
    //Mandatory, include 4 in email
    @Test
    void shouldReturnFalseWhen4IsNotPresent(){
        //Arrange
        EmailValidatorService emailValidator = new EmailValidatorService();
        //Act
        var isValid = emailValidator.isValid("carlos#gmal.com");
        //Assert
        assertFalse(isValid);
    }

}
