package mx.edu.cetys.Software_Quality_Lab.pets.exceptions;


public class invalidPetDataException extends RuntimeException {
    public invalidPetDataException(String message) {
        super(message);
    }
}
