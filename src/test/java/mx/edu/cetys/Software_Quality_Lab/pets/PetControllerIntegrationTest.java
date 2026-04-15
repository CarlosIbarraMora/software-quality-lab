package mx.edu.cetys.Software_Quality_Lab.pets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PetControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc; //Cliente REST como Postman

    @Autowired
    private PetRepository petRepository;

    @BeforeEach
    void tearDown(){
        //Limpiar BD después de pruebas
        petRepository.deleteAll();
    }

    @Test
    void shouldCreatePetAndReturn201() throws Exception {
        //Arrange
        String requestBody =
                """
                {
                    "name": "pop",
                    "race": "Dalmate",
                    "color": "Blanco y negro",
                    "age": 1
                }
                """;

        mockMvc.perform(
                //Act
                        post("/pets")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                //Assert
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.info")
                        .value("Pet saved"))
                .andExpect(jsonPath("$.response.id").
                        value(1)
                )
                .andExpect(jsonPath("$.response.name").
                        value("pop")
                )
                .andExpect(jsonPath("$.response.race").
                        value("Dalmate")
                )
                .andExpect(jsonPath("$.response.color").
                        value("Blanco y negro")
                )
                .andExpect(jsonPath("$.response.age").
                        value(1)
                )
        ;

    }
    //TODO integration test for 404 like .andExpect(status().isCreated())
    //but for 404, 400 (invalid input) etc.
}
