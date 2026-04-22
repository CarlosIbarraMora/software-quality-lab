package mx.edu.cetys.Software_Quality_Lab.pets;

import mx.edu.cetys.Software_Quality_Lab.pets.exceptions.invalidPetDataException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PetServiceTest {
//
//    //Una clase repository es la encargada de hacer las llamadas a BD,
//
//    // se marca con el aspecto @Repository definido por spring
    @Mock
    PetRepository petRepository;
//    //Service Class: Es la clase donde se ejecuta el negocio
    @InjectMocks
    PetService petService;

    //Controller: No hay lógica exepto validaciones de valores de entrada
    PetController.PetRequest request;

    //safe pet request check
    @Test
    void shouldCreatePetWithCorrectValues() {
        //Arrange
        var request = new PetController.PetRequest("Frijol", "brown", "chihuahua", 2);
        //Act
        Pet savedPet = new Pet(
                request.name(),
                request.color(),
                request.race(),
                request.age());
        savedPet.setId(1L);

        assertEquals("Frijol", savedPet.getName());
        assertEquals("brown", savedPet.getColor());
        assertEquals("chihuahua", savedPet.getRace());
        assertEquals(2, savedPet.getAge());
        assertEquals(1L, savedPet.getId());
    }

    @Test
    void savePet_InvalidName_ExceptionExpected(){
        //Arrange
        var petRequest = new PetController.PetRequest("L", "Negro", "Perro", 2);

        //Act
        assertThrows(invalidPetDataException.class, () -> petService.savePet(petRequest));
    }

    //TODO save pet invalid data
    //TODO get by id, -get of id 1 but is not in DB
    //404 not found


}
