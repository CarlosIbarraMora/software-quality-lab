package mx.edu.cetys.Software_Quality_Lab.pets;

import mx.edu.cetys.Software_Quality_Lab.pets.exceptions.InvalidPetDataException;

public class PetInfoValidator {
    public static void isValid(PetController.PetRequest requestPet) {
        //Age > 0
        if(requestPet.age() == null ||requestPet.age() <= 0){
            throw new InvalidPetDataException("Invalid age");
        }
        //Name length > 2
        if(requestPet.name() == null
                || requestPet.name().isBlank()
                || requestPet.name().length() <= 2){
            throw new InvalidPetDataException("Name must be greater than or equal to 2");
        }
        //color not null
        if(requestPet.color() == null || requestPet.color().isBlank()){
            throw new InvalidPetDataException("Invalid color");
        }
        if(requestPet.race() == null || requestPet.race().isBlank()){
            throw new InvalidPetDataException("Invalid race");
        }
    }

    public static void validateUpdate(PetController.PetUpdateRequest requestUpdate) {

        PetController.PetRequest petRequest = new PetController.
                PetRequest(requestUpdate.name(),
                            requestUpdate.color(),
                            requestUpdate.race(),
                            requestUpdate.age());

        isValid(petRequest);

        if (requestUpdate.available() == null) {
            throw new InvalidPetDataException("Available is required");
        }
    }
    public static void validateAvailability(PetController.PetAvailabilityRequest request) {
        if (request.available() == null) {
            throw new InvalidPetDataException("Available is required");
        }
    }
}
