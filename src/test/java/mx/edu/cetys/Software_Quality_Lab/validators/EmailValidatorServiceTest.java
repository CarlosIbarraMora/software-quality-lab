package mx.edu.cetys.Software_Quality_Lab.validators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EmailValidatorServiceTest {
    private EmailValidatorService emailValidator;
    //Assert
    @BeforeEach
    void BeforeEach() {
        //Arrange
        emailValidator = new EmailValidatorService();
    }
    //Base case: valid email
    @Test
    void shouldReturnTrueWhenEmailIsValid() {
        //Act
        var isValid = emailValidator.isValid("carlos4#gmal.com");
        //Assert
        assertTrue(isValid);
    }

    //Check if there is a # present but there are missing parts
    @Test
    void shouldReturnFalseWhenSeparatedButFirstPartMissing(){
        //Act
        var isValid = emailValidator.isValid("#gmal.com");
        //Assert
        assertFalse(isValid);
    }
    @Test
    void shouldReturnFalseWhenSeparatedButSecondPartMissing(){
        //Act
        var isValid = emailValidator.isValid("carlos4#");
        //Assert
        assertFalse(isValid);
    }
    @Test
    void shouldReturnFalseWhenSeparatedButBothPartMissing(){
        //Act
        var isValid = emailValidator.isValid("#");
        //Assert
        assertFalse(isValid);
    }

    @Test
    void shouldReturnFalseWhenEmailIsNull(){
        //Act
        var isValid = emailValidator.isValid(null);
        //Assert
        assertFalse(isValid);
    }

    @Test
    void shouldReturnFalseWhenEmailIsEmpty(){
        //Act
        var isValid = emailValidator.isValid("");
        //Assert
        assertFalse(isValid);
    }
    // Characters allowed 1-0, a-z
    @Test
    void shouldReturnFalseWhenInvalidCharactersGeneral(){
        //Act
        var isValid = emailValidator.isValid("carlos4&#gmal.com");
        //Assert
        assertFalse(isValid);
    }
    //Special characters allowed user: .-_+
    @Test
    void shouldReturnFalseWhenInvalidUserCharacters(){
        //Act
        var isValid = emailValidator.isValid("carlos4!#gmal.com");
        //Assert
        assertFalse(isValid);
    }
    //Special characters allowed(provider, domain): .
    @Test
    void shouldReturnFalseWhenDomainCharactersAreInvalid(){
        //Act
        var isValid = emailValidator.isValid("carlos4#gmal^.com");
        //Assert
        assertFalse(isValid);
    }
    //Use # to separate user-provider
    @Test
    void shouldReturnFalseWhenSeparationCharacterIsInvalid(){
        //Act
        var isValid = emailValidator.isValid("carlos4@gmal.com");
        //Assert
        assertFalse(isValid);
    }


    //No diphthong in the whole email
    @Test
    void shouldReturnFalseWhenDiphthongIsPresent(){
        //Act
        var isValid = emailValidator.isValid("carlos4#gmail.com");
        //Assert
        assertFalse(isValid);
    }
    // Max Domain length: 5
    @Test
    void shouldReturnFalseWhenDomainLengthHigherThan5(){
        //Act
        var isValid = emailValidator.isValid("carlos4#gmal.comasi");
        //Assert
        assertFalse(isValid);
    }

    @Test
    void shouldReturnFalseWhenDomainIsNotPresent(){
        //Act
        var isValid = emailValidator.isValid("carlos4#gmal.");
        //Assert
        assertFalse(isValid);
    }

    //Max email length: 47
    @Test
    void shouldReturnFalseWhenEmailLengthHigherThan47(){
        //Act
        var isValid = emailValidator.isValid("cccccccccccccccccccccccccccccccccccccccccccccccccccccccarlos4#gmal.com");
        //Assert
        assertFalse(isValid);
    }
    //Mandatory, include 4 in email
    @Test
    void shouldReturnFalseWhen4IsNotPresent(){
        //Act
        var isValid = emailValidator.isValid("carlos#gmal.com");
        //Assert
        assertFalse(isValid);
    }

}
