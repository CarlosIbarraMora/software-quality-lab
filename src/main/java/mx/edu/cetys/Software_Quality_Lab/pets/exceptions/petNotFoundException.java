package mx.edu.cetys.Software_Quality_Lab.pets.exceptions;

public class petNotFoundException extends RuntimeException {
    public petNotFoundException(String message) {
        super(message);
    }
}
