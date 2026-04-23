package mx.edu.cetys.Software_Quality_Lab.pets;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import mx.edu.cetys.Software_Quality_Lab.common.*;

import java.util.List;

@RestController
@RequestMapping("/pets")  //localhost:8080/pets
public class PetController {
    //GET localhost:8080/pets --Todos los pets TODO: pagination

    public final PetService petService;

    public PetController(PetService petService1) {
        this.petService = petService1;
    }

    //DTOs (Data Transfer Object) for request and Response
    record PetRequest(String name, String color, String race, Integer age){}
    record PetUpdateRequest(String name, String color, String race, Integer age, Boolean available){}
    record PetAvailabilityRequest(Boolean available) {}
    record PetResponse(Long id, String name, String color, String race, Integer age, Boolean available){}
    record PetWrapper(PetResponse pet){}

    @GetMapping("/health")
    ApiResponse<PetResponse> health() {
        return new ApiResponse<>("This is a health check", null, null );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ApiResponse<PetWrapper> createPet(@RequestBody PetController.PetRequest requestPet){
        return petService.savePet(requestPet);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    ApiResponse<Page<PetWrapper>> getPets(Pageable pageable) {
           return petService.getPets(pageable);
       }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ApiResponse<PetWrapper> getPetById(@PathVariable Long id){
        return petService.getPetById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ApiResponse<PetWrapper> updatePetById(@PathVariable Long id, @RequestBody PetUpdateRequest requestPet){
        return petService.updatePetById(id, requestPet);
    }

    // This is to delete, but we only change the availability to keep the record
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ApiResponse<PetWrapper> patchPetAvailability(@PathVariable Long id, @RequestBody PetAvailabilityRequest requestPet) {
        return petService.patchAvailability(id, requestPet);
    }


}
