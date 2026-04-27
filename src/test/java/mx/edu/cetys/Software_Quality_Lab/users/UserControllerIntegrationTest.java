package mx.edu.cetys.Software_Quality_Lab.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateUserAndReturn201() throws Exception {
        String requestBody = """
            {
                "username": "ana123",
                "password": "password123",
                "email": "ana@test.com",
                "firstName": "Ana",
                "lastName": "Lopez"
            }
            """;

        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.info").value("User saved successfully"))
                .andExpect(jsonPath("$.response.user.id").isNumber())
                .andExpect(jsonPath("$.response.user.username").value("ana123"))
                .andExpect(jsonPath("$.response.user.firstName").value("Ana"))
                .andExpect(jsonPath("$.response.user.lastName").value("Lopez"))
                .andExpect(jsonPath("$.response.user.email").value("ana@test.com"))
                .andExpect(jsonPath("$.response.user.active").value(true));
    }

    @Test
    void shouldReturn400WhenJsonIsInvalid() throws Exception {
        String requestBody = """
            {
                "username": "ana123",
                "password": 
            }
            """;

        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.info").value("Bad request"));
    }

    @Test
    void shouldGetUserByIdAndReturn200() throws Exception {
        User user = new User(
                "ana123",
                "hash",
                "ana@test.com",
                "Ana",
                "Lopez",
                true
        );

        User savedUser = userRepository.save(user);

        mockMvc.perform(get("/users/" + savedUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info").value("User info"))
                .andExpect(jsonPath("$.response.user.id").value(savedUser.getId()))
                .andExpect(jsonPath("$.response.user.username").value("ana123"));
    }

    @Test
    void shouldGetAllUsersAndReturn200() throws Exception {
        userRepository.save(new User(
                "ana123",
                "hash",
                "ana@test.com",
                "Ana",
                "Lopez",
                true
        ));

        mockMvc.perform(get("/users?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info").value("All users"))
                .andExpect(jsonPath("$.response.content[0].user.username").value("ana123"));
    }

    @Test
    void shouldGetUserByUsernameAndReturn200() throws Exception {
        userRepository.save(new User(
                "ana123",
                "hash",
                "ana@test.com",
                "Ana",
                "Lopez",
                true
        ));

        mockMvc.perform(get("/users/username/ana123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info").value("User info"))
                .andExpect(jsonPath("$.response.user.username").value("ana123"));
    }

    @Test
    void shouldUpdateUserAndReturn200() throws Exception {
        User savedUser = userRepository.save(new User(
                "ana123",
                "hash",
                "ana@test.com",
                "Ana",
                "Lopez",
                true
        ));

        String requestBody = """
            {
                "username": "anaUpdated",
                "email": "updated@test.com",
                "firstName": "Anita",
                "lastName": "Perez",
                "active": false
            }
            """;

        mockMvc.perform(
                        put("/users/" + savedUser.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info").value("User updated successfully"))
                .andExpect(jsonPath("$.response.user.username").value("anaUpdated"))
                .andExpect(jsonPath("$.response.user.firstName").value("Anita"))
                .andExpect(jsonPath("$.response.user.lastName").value("Perez"))
                .andExpect(jsonPath("$.response.user.email").value("updated@test.com"))
                .andExpect(jsonPath("$.response.user.active").value(false));
    }

    @Test
    void shouldDeleteUserAndReturn200() throws Exception {
        User savedUser = userRepository.save(new User(
                "ana123",
                "hash",
                "ana@test.com",
                "Ana",
                "Lopez",
                true
        ));

        mockMvc.perform(delete("/users/" + savedUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info").value("User deleted successfully"))
                .andExpect(jsonPath("$.response").doesNotExist());
    }
}