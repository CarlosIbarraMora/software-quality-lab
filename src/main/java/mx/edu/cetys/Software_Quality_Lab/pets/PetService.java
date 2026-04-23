package mx.edu.cetys.Software_Quality_Lab.pets;

import mx.edu.cetys.Software_Quality_Lab.pets.exceptions.PetNotFoundException;
import mx.edu.cetys.Software_Quality_Lab.common.*;
import mx.edu.cetys.Software_Quality_Lab.users.User;
import mx.edu.cetys.Software_Quality_Lab.users.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

   ApiResponse<PetController.PetWrapper> savePet(PetController.PetRequest requestPet){
        logger.info("Saving Pet Request requestPet={}", requestPet);

        PetInfoValidator.isValid(requestPet);

        var savedPet = petRepository.save(
                new Pet(requestPet.name(),
                        requestPet.color(),
                        requestPet.race(),
                        requestPet.age()));

        var responsePet = mapToResponse(savedPet);
        var wrappedPet = new PetController.PetWrapper(responsePet);
        return new ApiResponse<>(
                "Pet saved",
                wrappedPet,
                null);
    }
    ApiResponse<Page<PetController.PetWrapper>> getPets(Pageable pageable) {
        Page<Pet> pets = petRepository.findAll(pageable);

        Page<PetController.PetWrapper> wrappedResponse = pets.map(pet ->
                new PetController.PetWrapper(mapToResponse(pet))
        );

        return new ApiResponse<>("Pet list", wrappedResponse, null);
    }

    ApiResponse<PetController.PetWrapper> getPetById(@PathVariable Long id){
        var pet = petRepository.findById(id)
                               .orElseThrow(() -> new PetNotFoundException("Pet was not found"));

        return new ApiResponse<>("Pet info: ", new PetController.PetWrapper(mapToResponse(pet)), null);
    }

    ApiResponse<PetController.PetWrapper> updatePetById(@PathVariable Long id, PetController.PetUpdateRequest requestPet){
        var pet = petRepository.findById(id)
                .orElseThrow(() -> new PetNotFoundException("Pet was not found"));

        PetInfoValidator.validateUpdate(requestPet);

        pet.setName(requestPet.name());
        pet.setColor(requestPet.color());
        pet.setRace(requestPet.race());
        pet.setAge(requestPet.age());
        pet.setAvailable(requestPet.available());

        var savedPet = petRepository.save(pet);

        return new ApiResponse<>(
                "Pet updated",
                new PetController.PetWrapper(mapToResponse(savedPet)),
                null
        );
    }
    ApiResponse<PetController.PetWrapper> patchAvailability(Long id, PetController.PetAvailabilityRequest request) {
        var pet = petRepository.findById(id)
                .orElseThrow(() -> new PetNotFoundException("Pet with id=" + id + " was not found"));

        PetInfoValidator.validateAvailability(request);

        pet.setAvailable(request.available());

        var savedPet = petRepository.save(pet);

        return new ApiResponse<>(
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
