package mx.edu.cetys.Software_Quality_Lab.pets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PetControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PetRepository petRepository;

    @BeforeEach
    void setUp() {
        petRepository.deleteAll();
    }

    @Test
    void health_ShouldReturn200() throws Exception {
        mockMvc.perform(get("/pets/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info").value("This is a health check"))
                .andExpect(jsonPath("$.response").doesNotExist())
                .andExpect(jsonPath("$.error").doesNotExist());
    }

    @Test
    void shouldCreatePetAndReturn201() throws Exception {
        String requestBody = """
            {
                "name": "pop",
                "race": "Dalmate",
                "color": "Blanco y negro",
                "age": 1
            }
            """;

        mockMvc.perform(
                        post("/pets")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.info").value("Pet saved"))
                .andExpect(jsonPath("$.response.pet.id").isNumber())
                .andExpect(jsonPath("$.response.pet.name").value("pop"))
                .andExpect(jsonPath("$.response.pet.race").value("Dalmate"))
                .andExpect(jsonPath("$.response.pet.color").value("Blanco y negro"))
                .andExpect(jsonPath("$.response.pet.age").value(1))
                .andExpect(jsonPath("$.response.pet.available").value(true))
                .andExpect(jsonPath("$.error").doesNotExist());
    }

    @Test
    void createPet_WhenAgeIsInvalid_ShouldReturn500() throws Exception {
        String requestBody = """
            {
                "name": "pop",
                "race": "Dalmate",
                "color": "Blanco y negro",
                "age": 0
            }
            """;

        mockMvc.perform(
                        post("/pets")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.info").value("Internal server error"))
                .andExpect(jsonPath("$.error").value("Invalid age"));
    }

    @Test
    void createPet_WhenNameIsInvalid_ShouldReturn500() throws Exception {
        String requestBody = """
            {
                "name": "a",
                "race": "Dalmate",
                "color": "Blanco y negro",
                "age": 1
            }
            """;

        mockMvc.perform(
                        post("/pets")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.info").value("Internal server error"))
                .andExpect(jsonPath("$.error").value("Name must be greater than or equal to 2"));
    }

    @Test
    void createPet_WhenColorIsInvalid_ShouldReturn500() throws Exception {
        String requestBody = """
            {
                "name": "pop",
                "race": "Dalmate",
                "color": "",
                "age": 1
            }
            """;

        mockMvc.perform(
                        post("/pets")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.info").value("Internal server error"))
                .andExpect(jsonPath("$.error").value("Invalid color"));
    }

    @Test
    void createPet_WhenRaceIsInvalid_ShouldReturn500() throws Exception {
        String requestBody = """
            {
                "name": "pop",
                "race": "",
                "color": "Blanco y negro",
                "age": 1
            }
            """;

        mockMvc.perform(
                        post("/pets")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.info").value("Internal server error"))
                .andExpect(jsonPath("$.error").value("Invalid race"));
    }

    @Test
    void createPet_WhenJsonIsMalformed_ShouldReturn400() throws Exception {
        String requestBody = """
            {
                "name": "pop",
                "race":
            }
            """;

        mockMvc.perform(
                        post("/pets")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.info").value("Bad request"))
                .andExpect(jsonPath("$.response").doesNotExist())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void getPets_ShouldReturn200AndPagedPets() throws Exception {
        petRepository.save(new Pet("Frijol", "Brown", "Chihuahua", 2));
        petRepository.save(new Pet("Milaneso", "Black", "Pastor alemán", 3));

        mockMvc.perform(get("/pets?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info").value("Pet list"))
                .andExpect(jsonPath("$.response.content", hasSize(2)))
                .andExpect(jsonPath("$.response.content[0].pet.id").isNumber())
                .andExpect(jsonPath("$.response.content[0].pet.name").exists())
                .andExpect(jsonPath("$.response.totalElements").value(2));
    }

    @Test
    void getPetById_WhenPetExists_ShouldReturn200() throws Exception {
        Pet savedPet = petRepository.save(
                new Pet("Frijol", "Brown", "Chihuahua", 2)
        );

        mockMvc.perform(get("/pets/" + savedPet.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info").value("Pet info: "))
                .andExpect(jsonPath("$.response.pet.id").value(savedPet.getId()))
                .andExpect(jsonPath("$.response.pet.name").value("Frijol"))
                .andExpect(jsonPath("$.response.pet.color").value("Brown"))
                .andExpect(jsonPath("$.response.pet.race").value("Chihuahua"))
                .andExpect(jsonPath("$.response.pet.age").value(2))
                .andExpect(jsonPath("$.response.pet.available").value(true))
                .andExpect(jsonPath("$.error").doesNotExist());
    }

    @Test
    void getPetById_WhenPetDoesNotExist_ShouldReturn500() throws Exception {
        mockMvc.perform(get("/pets/999"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.info").value("Internal server error"))
                .andExpect(jsonPath("$.error").value("Pet was not found"));
    }

    @Test
    void updatePetById_WhenPetExists_ShouldReturn200() throws Exception {
        Pet savedPet = petRepository.save(
                new Pet("Frijol", "Brown", "Chihuahua", 2)
        );

        String requestBody = """
            {
                "name": "Andy",
                "color": "Negro",
                "race": "Cat",
                "age": 5,
                "available": false
            }
            """;

        mockMvc.perform(
                        put("/pets/" + savedPet.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info").value("Pet updated"))
                .andExpect(jsonPath("$.response.pet.id").value(savedPet.getId()))
                .andExpect(jsonPath("$.response.pet.name").value("Andy"))
                .andExpect(jsonPath("$.response.pet.color").value("Negro"))
                .andExpect(jsonPath("$.response.pet.race").value("Cat"))
                .andExpect(jsonPath("$.response.pet.age").value(5))
                .andExpect(jsonPath("$.response.pet.available").value(false))
                .andExpect(jsonPath("$.error").doesNotExist());
    }

    @Test
    void updatePetById_WhenPetDoesNotExist_ShouldReturn500() throws Exception {
        String requestBody = """
            {
                "name": "Andy",
                "color": "Negro",
                "race": "Cat",
                "age": 5,
                "available": true
            }
            """;

        mockMvc.perform(
                        put("/pets/999")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.info").value("Internal server error"))
                .andExpect(jsonPath("$.error").value("Pet was not found"));
    }

    @Test
    void updatePetById_WhenAvailableIsMissing_ShouldReturn500() throws Exception {
        Pet savedPet = petRepository.save(
                new Pet("Frijol", "Brown", "Chihuahua", 2)
        );

        String requestBody = """
            {
                "name": "Andy",
                "color": "Negro",
                "race": "Cat",
                "age": 5
            }
            """;

        mockMvc.perform(
                        put("/pets/" + savedPet.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.info").value("Internal server error"))
                .andExpect(jsonPath("$.error").value("Available is required"));
    }

    @Test
    void patchPetAvailability_WhenPetExists_ShouldReturn200() throws Exception {
        Pet savedPet = petRepository.save(
                new Pet("Frijol", "Brown", "Chihuahua", 2)
        );

        String requestBody = """
            {
                "available": false
            }
            """;

        mockMvc.perform(
                        patch("/pets/" + savedPet.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info").value("Pet availability updated"))
                .andExpect(jsonPath("$.response.pet.id").value(savedPet.getId()))
                .andExpect(jsonPath("$.response.pet.available").value(false))
                .andExpect(jsonPath("$.error").doesNotExist());
    }

    @Test
    void patchPetAvailability_WhenPetDoesNotExist_ShouldReturn500() throws Exception {
        String requestBody = """
            {
                "available": false
            }
            """;

        mockMvc.perform(
                        patch("/pets/999")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.info").value("Internal server error"))
                .andExpect(jsonPath("$.error").value("Pet with id=999 was not found"));
    }

    @Test
    void patchPetAvailability_WhenAvailableIsMissing_ShouldReturn500() throws Exception {
        Pet savedPet = petRepository.save(
                new Pet("Frijol", "Brown", "Chihuahua", 2)
        );

        String requestBody = """
            {
            }
            """;

        mockMvc.perform(
                        patch("/pets/" + savedPet.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.info").value("Internal server error"))
                .andExpect(jsonPath("$.error").value("Available is required"));
    }
}
