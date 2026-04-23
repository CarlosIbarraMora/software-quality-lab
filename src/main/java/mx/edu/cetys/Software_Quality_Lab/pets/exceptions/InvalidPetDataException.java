package mx.edu.cetys.Software_Quality_Lab.pets.exceptions;


public class InvalidPetDataException extends RuntimeException {
    public InvalidPetDataException(String message) {
        super(message);
    }
}
