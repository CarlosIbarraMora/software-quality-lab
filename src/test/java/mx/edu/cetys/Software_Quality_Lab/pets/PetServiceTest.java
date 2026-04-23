package mx.edu.cetys.Software_Quality_Lab.pets;

import mx.edu.cetys.Software_Quality_Lab.common.ApiResponse;
import mx.edu.cetys.Software_Quality_Lab.pets.exceptions.InvalidPetDataException;
import mx.edu.cetys.Software_Quality_Lab.pets.exceptions.PetNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PetServiceTest {

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
    void getPetById_WhenPetDoesNotExist_ShouldThrowException() {
        when(petRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(PetNotFoundException.class, () -> petService.getPetById(99L));

        verify(petRepository, times(1)).findById(99L);
    }

    @Test
    void getPets_ShouldReturnPagedPets() {
        Pet pet = new Pet("Frijol", "Brown", "Chihuahua", 2);
        pet.setId(1L);
        pet.setAvailable(true);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Pet> page = new PageImpl<>(List.of(pet), pageable, 1);

        when(petRepository.findAll(pageable)).thenReturn(page);

        var response = petService.getPets(pageable);

        assertEquals("Pet list", response.info());
        assertEquals(1, response.response().getTotalElements());
        assertEquals("Frijol", response.response().getContent().getFirst().pet().name());

        verify(petRepository).findAll(pageable);
    }

    @Test
    void updatePetById_ShouldUpdatePetCorrectly() {
        Pet existingPet = new Pet("Frijol", "Brown", "Chihuahua", 2);
        existingPet.setId(1L);
        existingPet.setAvailable(true);

        var updateRequest = new PetController.PetUpdateRequest(
                "Andy",
                "Negro",
                "Cat",
                5,
                false
        );

        Pet updatedPet = new Pet(
                updateRequest.name(),
                updateRequest.color(),
                updateRequest.race(),
                updateRequest.age()
        );
        updatedPet.setId(1L);
        updatedPet.setAvailable(updateRequest.available());

        when(petRepository.findById(1L)).thenReturn(Optional.of(existingPet));
        when(petRepository.save(any(Pet.class))).thenReturn(updatedPet);

        var response = petService.updatePetById(1L, updateRequest);

        assertEquals("Pet updated", response.info());
        assertEquals(1L, response.response().pet().id());
        assertEquals("Andy", response.response().pet().name());
        assertEquals("Negro", response.response().pet().color());
        assertEquals("Cat", response.response().pet().race());
        assertEquals(5, response.response().pet().age());
        assertFalse(response.response().pet().available());

        verify(petRepository).findById(1L);
        verify(petRepository).save(any(Pet.class));
    }

    @Test
    void updatePetById_WhenPetDoesNotExist_ShouldThrowException() {
        var updateRequest = new PetController.PetUpdateRequest(
                "Andy",
                "Negro",
                "Cat",
                5,
                true
        );

        when(petRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(PetNotFoundException.class, () ->
                petService.updatePetById(99L, updateRequest)
        );

        verify(petRepository).findById(99L);
        verify(petRepository, never()).save(any(Pet.class));
    }

    @Test
    void updatePetById_WhenAvailableIsNull_ShouldThrowException() {
        Pet existingPet = new Pet("Frijol", "Brown", "Chihuahua", 2);
        existingPet.setId(1L);

        var updateRequest = new PetController.PetUpdateRequest(
                "Andy",
                "Negro",
                "Cat",
                5,
                null
        );

        when(petRepository.findById(1L)).thenReturn(Optional.of(existingPet));

        assertThrows(InvalidPetDataException.class, () ->
                petService.updatePetById(1L, updateRequest)
        );

        verify(petRepository).findById(1L);
        verify(petRepository, never()).save(any(Pet.class));
    }

    @Test
    void patchAvailability_ShouldUpdateAvailability() {
        Pet existingPet = new Pet("Frijol", "Brown", "Chihuahua", 2);
        existingPet.setId(1L);
        existingPet.setAvailable(true);

        var request = new PetController.PetAvailabilityRequest(false);

        Pet savedPet = new Pet("Frijol", "Brown", "Chihuahua", 2);
        savedPet.setId(1L);
        savedPet.setAvailable(false);

        when(petRepository.findById(1L)).thenReturn(Optional.of(existingPet));
        when(petRepository.save(any(Pet.class))).thenReturn(savedPet);

        var response = petService.patchAvailability(1L, request);

        assertEquals("Pet availability updated", response.info());
        assertEquals(1L, response.response().pet().id());
        assertFalse(response.response().pet().available());

        verify(petRepository).findById(1L);
        verify(petRepository).save(any(Pet.class));
    }

    @Test
    void patchAvailability_WhenPetDoesNotExist_ShouldThrowException() {
        var request = new PetController.PetAvailabilityRequest(false);

        when(petRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(PetNotFoundException.class, () ->
                petService.patchAvailability(99L, request)
        );

        verify(petRepository).findById(99L);
        verify(petRepository, never()).save(any(Pet.class));
    }

    @Test
    void patchAvailability_WhenAvailableIsNull_ShouldThrowException() {
        Pet existingPet = new Pet("Frijol", "Brown", "Chihuahua", 2);
        existingPet.setId(1L);

        var request = new PetController.PetAvailabilityRequest(null);

        when(petRepository.findById(1L)).thenReturn(Optional.of(existingPet));

        assertThrows(InvalidPetDataException.class, () ->
                petService.patchAvailability(1L, request)
        );

        verify(petRepository).findById(1L);
        verify(petRepository, never()).save(any(Pet.class));
    }
}