package mx.edu.cetys.Software_Quality_Lab.pets;

import mx.edu.cetys.Software_Quality_Lab.pets.exceptions.invalidPetDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //@AutoWired
    private final PetRepository petRepository;

    PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    PetController.ApiResponse<PetController.PetResponse> savePet(PetController.PetRequest requestPet){
        logger.info("Saving Pet Request requestPet={}", requestPet);
        //Age > 0
        if(requestPet.age() == null ||requestPet.age() <= 0){
            throw new  invalidPetDataException("Invalid age");
        }
        //Name length > 2
        if(requestPet.name() == null
                || requestPet.name().isBlank()
                || requestPet.name().length() <= 2){
            throw new invalidPetDataException("Name must be greater than or equal to 2");
        }
        //color not null
        if(requestPet.color() == null || requestPet.color().isBlank()){
            throw new invalidPetDataException("Invalid color");
        }
        if(requestPet.race() == null || requestPet.race().isBlank()){
            throw new invalidPetDataException("Invalid race");
        }

        var savedPet = petRepository.save(
                new Pet(requestPet.name(),
                        requestPet.color(),
                        requestPet.race(),
                        requestPet.age()));

        var responsePet = new PetController.PetResponse(
                savedPet.getId(),
                savedPet.getName(),
                savedPet.getColor(),
                savedPet.getRace(),
                savedPet.getAge());

        return new PetController.ApiResponse<>(
                "Pet saved",
                responsePet,
                null);
    }
    PetController.ApiResponse<List<PetController.PetWrapper>> getAllPets(){
            var pets = petRepository.findAll();

            var response = pets.stream()
                    .map(p -> new PetController.PetResponse(
                            p.getId(),
                            p.getName(),
                            p.getColor(),
                            p.getRace(),
                            p.getAge()
                    ))
                    .toList();

            var wrappedResponse = response.stream()
                    .map(PetController.PetWrapper::new).toList();

            return new PetController.ApiResponse<>("Pet list", wrappedResponse, null);
    }
}
