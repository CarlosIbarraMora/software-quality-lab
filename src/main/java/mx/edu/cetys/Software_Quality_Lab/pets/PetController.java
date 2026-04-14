package mx.edu.cetys.Software_Quality_Lab.pets;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pets")  //localhost:8080/pets
public class PetController {
    //GET localhost:8080/pets --Todos los pets TODO: pagination

    //GET localhost:8080/pets/{id} --ver pet por id

    //POST  localhost:8080/pets --nuevo post - Request {json body}

    //PUT  localhost:8080/pets/{id} --nuevo post - Actualizar pet por ID

    //PATCH

    //DELETE localhost:8080/{id}  //Flag available:y/n

    public final PetService petService;

    public PetController(PetService petService, PetService petService1) {
        this.petService = petService1;
    }

    //DTOs (Data Transfer Object) for request and Response
    record PetRequest(String name, String color, String race, Integer age){}

    record PetResponse(Long id, String name, String color, String race, Integer age){}
    record PetWrapper(PetResponse pet){}

    // Response generic wrapper to include standard info in all our API
    record ApiResponse<T>(String info, T response, String error){}

    @GetMapping("/health")
    ApiResponse<PetResponse> health() {
        return new ApiResponse<>("This is a health check", null, null );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ApiResponse<PetWrapper> createPet(@RequestBody PetController.PetRequest requestPet){
        return new ApiResponse<>("pet saved",
                new PetWrapper(petService.savePet(requestPet).response()),
                null );
    }

       @GetMapping
       @ResponseStatus(HttpStatus.OK)
       ApiResponse<List<PetWrapper>> getAllPets(){
           return petService.getAllPets();
       }

//    @GetMapping("/{id}")
//    ApiResponse<PetResponse> getPetById(@PathVariable Long id){
//        return petService.getPetById(id);
//    }
}
