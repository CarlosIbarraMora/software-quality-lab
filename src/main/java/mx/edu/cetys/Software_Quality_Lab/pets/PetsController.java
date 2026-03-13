package mx.edu.cetys.Software_Quality_Lab.pets;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pets")  //localhost:8080/pets
public class PetsController {
    //GET localhost:8080/pets --Todos los pets TODO: pagination

    //GET localhost:8080/pets/{id} --ver pet por id

    //POST  localhost:8080/pets --nuevo post - Request {json body}

    //PUT  localhost:8080/pets/{id} --nuevo post - Actualizar pet por ID

    //PATCH

    //DELETE localhost:8080/{id}  //Flag available:y/n

    @GetMapping("/health")
    public petResponse health() {
        return new petResponse("This is a health check", null);
    }

    private record petResponse(Object info, Pet pet){}
    record Pet(String name, String color, String race){}
}
