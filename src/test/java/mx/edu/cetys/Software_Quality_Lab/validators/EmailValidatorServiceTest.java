package mx.edu.cetys.Software_Quality_Lab.validators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Email validator Service Test")
public class EmailValidatorServiceTest {
    private EmailValidatorService emailValidator;
    //Assert
    @BeforeEach
    @DisplayName("Initial setup")
    void BeforeEach() {
        //Arrange
        emailValidator = new EmailValidatorService();
    }
    //Base case: valid email
    @Test
    @DisplayName("Base case email")
    void shouldReturnTrueWhenEmailIsValid() {
        //Act
        var isValid = emailValidator.isValid("carlos4#gmal.com");
        //Assert
        assertTrue(isValid);
    }

    //Check if there is a # present but there are missing parts
    @Test
    @DisplayName("Should return false when user is missing")
    void shouldReturnFalseWhenSeparatedButFirstPartMissing(){
        //Act
        var isValid = emailValidator.isValid("#gmal.com");
        //Assert
        assertFalse(isValid);
    }
    @Test
    @DisplayName("Should return false when provider-domain is missing")
    void shouldReturnFalseWhenSeparatedButSecondPartMissing(){
        //Act
        var isValid = emailValidator.isValid("carlos4#");
        //Assert
        assertFalse(isValid);
    }
    @Test
    @DisplayName("Should return false when everything is missing except #")
    void shouldReturnFalseWhenSeparatedButBothPartMissing(){
        //Act
        var isValid = emailValidator.isValid("#");
        //Assert
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should return false when email is null")
    void shouldReturnFalseWhenEmailIsNull(){
        //Act
        var isValid = emailValidator.isValid(null);
        //Assert
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should return false when email is empty")
    void shouldReturnFalseWhenEmailIsEmpty(){
        //Act
        var isValid = emailValidator.isValid("");
        //Assert
        assertFalse(isValid);
    }
    // Characters allowed 1-0, a-z
    @Test
    @DisplayName("Should return false when invalid characters in general(not 1-0, a-z) ")
    void shouldReturnFalseWhenInvalidCharactersGeneral(){
        //Act
        var isValid = emailValidator.isValid("carlos4&#gmal.com");
        //Assert
        assertFalse(isValid);
    }
    //Special characters allowed user: .-_+
    @Test
    @DisplayName("Should return false when invalid user characters (allowed: .-_+)")
    void shouldReturnFalseWhenInvalidUserCharacters(){
        //Act
        var isValid = emailValidator.isValid("carlos4!#gmal.com");
        //Assert
        assertFalse(isValid);
    }
    //Special characters allowed(provider, domain): .
    @Test
    @DisplayName("Should return false when invalid domain characters (allowed: .)")
    void shouldReturnFalseWhenDomainCharactersAreInvalid(){
        //Act
        var isValid = emailValidator.isValid("carlos4#gmal^.com");
        //Assert
        assertFalse(isValid);
    }
    //Use # to separate user-provider
    @Test
    @DisplayName("Should return false when separation character is not #")
    void shouldReturnFalseWhenSeparationCharacterIsInvalid(){
        //Act
        var isValid = emailValidator.isValid("carlos4@gmal.com");
        //Assert
        assertFalse(isValid);
    }
    //No diphthong in the whole email
    @Test
    @DisplayName("Should return false when diphthong is present")
    void shouldReturnFalseWhenDiphthongIsPresent(){
        //Act
        var isValid = emailValidator.isValid("carlos4#gmail.com");
        //Assert
        assertFalse(isValid);
    }
    // Max Domain length: 5
    @Test
    @DisplayName("Should return false when domain length > than 5")
    void shouldReturnFalseWhenDomainLengthHigherThan5(){
        //Act
        var isValid = emailValidator.isValid("carlos4#gmal.comasi");
        //Assert
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should return false when domain not present")
    void shouldReturnFalseWhenDomainIsNotPresent(){
        //Act
        var isValid = emailValidator.isValid("carlos4#gmal.");
        //Assert
        assertFalse(isValid);
    }

    //Max email length: 47
    @Test
    @DisplayName("Should return false when general character count > 47")
    void shouldReturnFalseWhenEmailLengthHigherThan47(){
        //Act
        var isValid = emailValidator.isValid("cccccccccccccccccccccccccccccccccccccccccccccccccccccccarlos4#gmal.com");
        //Assert
        assertFalse(isValid);
    }
    //Mandatory, include 4 in email
    @Test
    @DisplayName("Should return false when 4 is not present)")
    void shouldReturnFalseWhen4IsNotPresent(){
        //Act
        var isValid = emailValidator.isValid("carlos#gmal.com");
        //Assert
        assertFalse(isValid);
    }

}
