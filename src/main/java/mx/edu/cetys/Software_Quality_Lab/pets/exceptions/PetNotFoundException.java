package mx.edu.cetys.Software_Quality_Lab.pets.exceptions;

public class PetNotFoundException extends RuntimeException {
    public PetNotFoundException(String message) {
        super(message);
    }
}
