package mx.edu.cetys.Software_Quality_Lab.users.exceptions;

public class invalidUserDataException extends RuntimeException {
    public invalidUserDataException(String message) {
        super(message);
    }
}
