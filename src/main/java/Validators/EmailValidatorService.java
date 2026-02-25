package Validators;

public class EmailValidatorService {
    public boolean isValid(String email) {
        return email != null && !email.isEmpty();
    }
}