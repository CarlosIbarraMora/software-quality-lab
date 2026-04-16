package mx.edu.cetys.Software_Quality_Lab.users.exceptions;

public class userNotFoundException extends RuntimeException {
    public userNotFoundException(String message) {
        super(message);
    }
}
