package mx.edu.cetys.Software_Quality_Lab.pets;

import mx.edu.cetys.Software_Quality_Lab.common.ApiResponse;
import mx.edu.cetys.Software_Quality_Lab.pets.exceptions.InvalidPetDataException;
import mx.edu.cetys.Software_Quality_Lab.pets.exceptions.PetNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

        // Arrange
        var petRequest = new PetController.PetRequest("Andy", "Negro", "Cat", 67);

        Pet savedPet = new Pet();
        savedPet.setId(1L);
        savedPet.setName("Andy");
        savedPet.setColor("Negro");
        savedPet.setRace("Cat");
        savedPet.setAge(67);
        savedPet.setAvailable(true);

        when(petRepository.save(any(Pet.class))).thenReturn(savedPet);

        // Act
        ApiResponse<PetController.PetWrapper> petApiDTO = petService.savePet(petRequest);
        PetController.PetResponse petResponse = petApiDTO.response().pet();

        // Assert
        verify(petRepository, times(1)).save(any(Pet.class));
        assertEquals(1L, petResponse.id());
        assertEquals("Andy", petResponse.name());
        assertEquals("Negro", petResponse.color());
        assertEquals("Cat", petResponse.race());
        assertEquals(67, petResponse.age());
    }


    //Age
    @Test
    void savePet_NullAge_ExceptionExpected(){
        //Arrange
        var petRequest = new PetController.PetRequest("Frijol", "Negro", "Perro", null);

        //Act
        assertThrows(InvalidPetDataException.class, () -> petService.savePet(petRequest));
    }
    @Test
    void savePet_InvalidAge_ExceptionExpected(){
        //Arrange
        var petRequest = new PetController.PetRequest("Frijol", "Negro", "Perro", -2);

        //Act
        assertThrows(InvalidPetDataException.class, () -> petService.savePet(petRequest));
    }
    //Name
    @Test
    void savePet_NullName_ExceptionExpected(){
        //Arrange
        var petRequest = new PetController.PetRequest(null, "Negro", "Perro", 2);

        //Act
        assertThrows(InvalidPetDataException.class, () -> petService.savePet(petRequest));
    }
    @Test
    void savePet_EmptyName_ExceptionExpected(){
        //Arrange
        var petRequest = new PetController.PetRequest("", "Negro", "Perro", 2);

        //Act
        assertThrows(InvalidPetDataException.class, () -> petService.savePet(petRequest));
    }
    @Test
    void savePet_InvalidNameLength_ExceptionExpected(){
        //Arrange
        var petRequest = new PetController.PetRequest("Le", "Negro", "Perro", 2);

        //Act
        assertThrows(InvalidPetDataException.class, () -> petService.savePet(petRequest));
    }

    //Color
    @Test
    void savePet_NullColor_ExceptionExpected(){
        //Arrange
        var petRequest = new PetController.PetRequest("Frijol", null, "Perro", 2);

        //Act
        assertThrows(InvalidPetDataException.class, () -> petService.savePet(petRequest));
    }
    @Test
    void savePet_BlankColor_ExceptionExpected(){
        //Arrange
        var petRequest = new PetController.PetRequest("Frijol", "", "Perro", 2);

        //Act
        assertThrows(InvalidPetDataException.class, () -> petService.savePet(petRequest));
    }

    //Race
    @Test
    void savePet_NullRace_ExceptionExpected(){
        //Arrange
        var petRequest = new PetController.PetRequest("Frijol", "Negro" , null, 2);

        //Act
        assertThrows(InvalidPetDataException.class, () -> petService.savePet(petRequest));
    }

    @Test
    void savePet_BlankRace_ExceptionExpected(){
        //Arrange
        var petRequest = new PetController.PetRequest("Frijol", "Negro" , "", 2);

        //Act
        assertThrows(InvalidPetDataException.class, () -> petService.savePet(petRequest));
    }

    @Test
    void getPetById(){
        // Arrange
        var request = new PetController.PetRequest("Frijol", "brown", "chihuahua", 2);

        Pet savedPet = new Pet(
                request.name(),
                request.color(),
                request.race(),
                request.age());
        savedPet.setId(1L);

        when(petRepository.findById(1L)).thenReturn(Optional.of(savedPet));

        // Act
        var requestedPet = petService.getPetById(1L);

        // Assert
        assertEquals(1L, requestedPet.response().pet().id());
        assertEquals("Frijol", requestedPet.response().pet().name());

    }
    //TODO get by id, -get of id 1 but is not in DB
    //404 not found


}
