package mx.edu.cetys.Software_Quality_Lab.pets;

import mx.edu.cetys.Software_Quality_Lab.pets.exceptions.petNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

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

        PetInfoValidator.isValid(requestPet);

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
                savedPet.getAge(),
                true
                );

        return new PetController.ApiResponse<>(
                "Pet saved",
                responsePet,
                null);
    }
    PetController.ApiResponse<List<PetController.PetWrapper>> getAllPets(){
            var pets = petRepository.findAll();

            var response = pets.stream()
                    .map(this::mapToResponse
                    ).toList();

            var wrappedResponse = response.stream()
                    .map(PetController.PetWrapper::new).toList();

            return new PetController.ApiResponse<>("Pet list", wrappedResponse, null);
    }

    PetController.ApiResponse<PetController.PetWrapper> getPetById(@PathVariable Long id){
        var pet = petRepository.findById(id)
                               .orElseThrow(() -> new petNotFoundException("Pet was not found"));

        return new PetController.ApiResponse<>("Pet info: ", new PetController.PetWrapper(mapToResponse(pet)), null);
    }

    PetController.ApiResponse<PetController.PetWrapper> updatePetById(@PathVariable Long id, PetController.PetUpdateRequest requestPet){
        var pet = petRepository.findById(id)
                .orElseThrow(() -> new petNotFoundException("Pet was not found"));

        PetInfoValidator.validateUpdate(requestPet);

        pet.setName(requestPet.name());
        pet.setColor(requestPet.color());
        pet.setRace(requestPet.race());
        pet.setAge(requestPet.age());
        pet.setAvailable(requestPet.available());

        var savedPet = petRepository.save(pet);

        return new PetController.ApiResponse<>(
                "Pet updated",
                new PetController.PetWrapper(mapToResponse(savedPet)),
                null
        );
    }
    PetController.ApiResponse<PetController.PetWrapper> patchAvailability(Long id, PetController.PetAvailabilityRequest request) {
        var pet = petRepository.findById(id)
                .orElseThrow(() -> new petNotFoundException("Pet with id=" + id + " was not found"));

        PetInfoValidator.validateAvailability(request);

        pet.setAvailable(request.available());

        var savedPet = petRepository.save(pet);

        return new PetController.ApiResponse<>(
                "Pet availability updated",
                new PetController.PetWrapper(mapToResponse(savedPet)),
                null
        );
    }

    // to simplify the response process
    private PetController.PetResponse mapToResponse(Pet pet) {
        return new PetController.PetResponse(
                pet.getId(),
                pet.getName(),
                pet.getColor(),
                pet.getRace(),
                pet.getAge(),
                pet.isAvailable()
        );
    }

}
