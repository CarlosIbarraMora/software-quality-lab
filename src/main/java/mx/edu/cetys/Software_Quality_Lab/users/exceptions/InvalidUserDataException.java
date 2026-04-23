package mx.edu.cetys.Software_Quality_Lab.users.exceptions;

public class InvalidUserDataException extends RuntimeException {
    public InvalidUserDataException(String message) {
        super(message);
    }
}
