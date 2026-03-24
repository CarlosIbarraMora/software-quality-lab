package mx.edu.cetys.Software_Quality_Lab.pets;


import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class PetServiceTest {
//
//    //Una clase repository es la encargada de hacer las llamadas a BD,
//
//    // se marca con el aspecto @Repository definido por spring
//    @Mock
//    PetRepository petRepository;
//
//    //Service Class: Es la clase donde se ejecuta el negocio
//    @InjectMocks
//    PetService petService;
//
//    //Controller: No hay lógica exepto validaciones de valores de entrada
//
//    @Test
//    void savePet(){
//        // Recibir la petición desde el controller(pet)
//        // Verificar los valores del pet:
//        /*
//        -edad no negativa
//        -Nombre más de 2 letras
//        -salvar a la base de datos, y la base de datos nos regresa el mismo pet pero con ID
//         */
//        //Arrange
//        //var pet = new PetController.Pet("Milaneso", "Negro", "chihuahua", 1);
//        //when(PetRepository.savePet(pet))
//                //.thenReturn(1L, "Milaneso", "Negro", "Chihuahua", 1);
//        //Act
//        //PetDTO petResponse = petService.savePet(pet);
//        //Assert
//        Mockito.verify(
//                petRepository.savePet(any(PetController.Pet.class)
//                ), times(1));
//        assertEquals(1L, pet.id());
//
//    }
//
//    @Test
//    void getAllPets(){
//        //Recibir la peticion desde controller GetAll
//        //Query a BD
//        //Meter valores del DTO a la respuesta
//        //Si la BD no tiene valores, regresar empty array
//    }
//


}
