package mx.edu.cetys.Software_Quality_Lab.pets;

import mx.edu.cetys.Software_Quality_Lab.pets.exceptions.InvalidPetDataException;
import mx.edu.cetys.Software_Quality_Lab.pets.exceptions.PetNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PetServiceTest {

    @Mock
    PetRepository petRepository;

    @InjectMocks
    PetService petService;

    @Test
    void savePet_ShouldSavePetCorrectly() {
        var request = new PetController.PetRequest(
                "Frijol",
                "Brown",
                "Chihuahua",
                2
        );

        when(petRepository.save(any(Pet.class))).thenAnswer(invocation -> {
            Pet pet = invocation.getArgument(0);
            pet.setId(1L);
            return pet;
        });

        var response = petService.savePet(request);

        assertEquals("Pet saved", response.info());
        assertNotNull(response.response());
        assertEquals(1L, response.response().pet().id());
        assertEquals("Frijol", response.response().pet().name());
        assertEquals("Brown", response.response().pet().color());
        assertEquals("Chihuahua", response.response().pet().race());
        assertEquals(2, response.response().pet().age());
        assertTrue(response.response().pet().available());
        assertNull(response.error());

        verify(petRepository).save(any(Pet.class));
    }

    @Test
    void savePet_WhenAgeIsNull_ShouldThrowException() {
        var request = new PetController.PetRequest(
                "Frijol",
                "Brown",
                "Chihuahua",
                null
        );

        assertThrows(InvalidPetDataException.class, () ->
                petService.savePet(request)
        );

        verify(petRepository, never()).save(any(Pet.class));
    }

    @Test
    void savePet_WhenAgeIsZero_ShouldThrowException() {
        var request = new PetController.PetRequest(
                "Frijol",
                "Brown",
                "Chihuahua",
                0
        );

        assertThrows(InvalidPetDataException.class, () ->
                petService.savePet(request)
        );

        verify(petRepository, never()).save(any(Pet.class));
    }

    @Test
    void savePet_WhenNameIsNull_ShouldThrowException() {
        var request = new PetController.PetRequest(
                null,
                "Brown",
                "Chihuahua",
                2
        );

        assertThrows(InvalidPetDataException.class, () ->
                petService.savePet(request)
        );

        verify(petRepository, never()).save(any(Pet.class));
    }

    @Test
    void savePet_WhenNameIsBlank_ShouldThrowException() {
        var request = new PetController.PetRequest(
                "",
                "Brown",
                "Chihuahua",
                2
        );

        assertThrows(InvalidPetDataException.class, () ->
                petService.savePet(request)
        );

        verify(petRepository, never()).save(any(Pet.class));
    }

    @Test
    void savePet_WhenNameIsTooShort_ShouldThrowException() {
        var request = new PetController.PetRequest(
                "Fi",
                "Brown",
                "Chihuahua",
                2
        );

        assertThrows(InvalidPetDataException.class, () ->
                petService.savePet(request)
        );

        verify(petRepository, never()).save(any(Pet.class));
    }

    @Test
    void savePet_WhenColorIsNull_ShouldThrowException() {
        var request = new PetController.PetRequest(
                "Frijol",
                null,
                "Chihuahua",
                2
        );

        assertThrows(InvalidPetDataException.class, () ->
                petService.savePet(request)
        );

        verify(petRepository, never()).save(any(Pet.class));
    }

    @Test
    void savePet_WhenColorIsBlank_ShouldThrowException() {
        var request = new PetController.PetRequest(
                "Frijol",
                "",
                "Chihuahua",
                2
        );

        assertThrows(InvalidPetDataException.class, () ->
                petService.savePet(request)
        );

        verify(petRepository, never()).save(any(Pet.class));
    }

    @Test
    void savePet_WhenRaceIsNull_ShouldThrowException() {
        var request = new PetController.PetRequest(
                "Frijol",
                "Brown",
                null,
                2
        );

        assertThrows(InvalidPetDataException.class, () ->
                petService.savePet(request)
        );

        verify(petRepository, never()).save(any(Pet.class));
    }

    @Test
    void savePet_WhenRaceIsBlank_ShouldThrowException() {
        var request = new PetController.PetRequest(
                "Frijol",
                "Brown",
                "",
                2
        );

        assertThrows(InvalidPetDataException.class, () ->
                petService.savePet(request)
        );

        verify(petRepository, never()).save(any(Pet.class));
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
        assertEquals("Brown", response.response().getContent().getFirst().pet().color());
        assertEquals("Chihuahua", response.response().getContent().getFirst().pet().race());
        assertEquals(2, response.response().getContent().getFirst().pet().age());
        assertTrue(response.response().getContent().getFirst().pet().available());

        verify(petRepository).findAll(pageable);
    }

    @Test
    void getPetById_WhenPetExists_ShouldReturnPet() {
        Pet pet = new Pet("Frijol", "Brown", "Chihuahua", 2);
        pet.setId(1L);
        pet.setAvailable(true);

        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));

        var response = petService.getPetById(1L);

        assertEquals("Pet info: ", response.info());
        assertEquals(1L, response.response().pet().id());
        assertEquals("Frijol", response.response().pet().name());
        assertEquals("Brown", response.response().pet().color());
        assertEquals("Chihuahua", response.response().pet().race());
        assertEquals(2, response.response().pet().age());
        assertTrue(response.response().pet().available());

        verify(petRepository).findById(1L);
    }

    @Test
    void getPetById_WhenPetDoesNotExist_ShouldThrowException() {
        when(petRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(PetNotFoundException.class, () ->
                petService.getPetById(99L)
        );

        verify(petRepository).findById(99L);
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

        when(petRepository.findById(1L)).thenReturn(Optional.of(existingPet));
        when(petRepository.save(any(Pet.class))).thenAnswer(invocation -> invocation.getArgument(0));

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
    void updatePetById_WhenNameIsInvalid_ShouldThrowException() {
        Pet existingPet = new Pet("Frijol", "Brown", "Chihuahua", 2);
        existingPet.setId(1L);

        var updateRequest = new PetController.PetUpdateRequest(
                "",
                "Negro",
                "Cat",
                5,
                true
        );

        when(petRepository.findById(1L)).thenReturn(Optional.of(existingPet));

        assertThrows(InvalidPetDataException.class, () ->
                petService.updatePetById(1L, updateRequest)
        );

        verify(petRepository).findById(1L);
        verify(petRepository, never()).save(any(Pet.class));
    }

    @Test
    void updatePetById_WhenColorIsInvalid_ShouldThrowException() {
        Pet existingPet = new Pet("Frijol", "Brown", "Chihuahua", 2);
        existingPet.setId(1L);

        var updateRequest = new PetController.PetUpdateRequest(
                "Andy",
                "",
                "Cat",
                5,
                true
        );

        when(petRepository.findById(1L)).thenReturn(Optional.of(existingPet));

        assertThrows(InvalidPetDataException.class, () ->
                petService.updatePetById(1L, updateRequest)
        );

        verify(petRepository).findById(1L);
        verify(petRepository, never()).save(any(Pet.class));
    }

    @Test
    void updatePetById_WhenRaceIsInvalid_ShouldThrowException() {
        Pet existingPet = new Pet("Frijol", "Brown", "Chihuahua", 2);
        existingPet.setId(1L);

        var updateRequest = new PetController.PetUpdateRequest(
                "Andy",
                "Negro",
                "",
                5,
                true
        );

        when(petRepository.findById(1L)).thenReturn(Optional.of(existingPet));

        assertThrows(InvalidPetDataException.class, () ->
                petService.updatePetById(1L, updateRequest)
        );

        verify(petRepository).findById(1L);
        verify(petRepository, never()).save(any(Pet.class));
    }

    @Test
    void updatePetById_WhenAgeIsInvalid_ShouldThrowException() {
        Pet existingPet = new Pet("Frijol", "Brown", "Chihuahua", 2);
        existingPet.setId(1L);

        var updateRequest = new PetController.PetUpdateRequest(
                "Andy",
                "Negro",
                "Cat",
                0,
                true
        );

        when(petRepository.findById(1L)).thenReturn(Optional.of(existingPet));

        assertThrows(InvalidPetDataException.class, () ->
                petService.updatePetById(1L, updateRequest)
        );

        verify(petRepository).findById(1L);
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

        when(petRepository.findById(1L)).thenReturn(Optional.of(existingPet));
        when(petRepository.save(any(Pet.class))).thenAnswer(invocation -> invocation.getArgument(0));

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