package ru.skypro.homework.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.skypro.homework.dto.LoginDTO;
import ru.skypro.homework.dto.RegisterDTO;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.repository.UserModelRepository;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.utils.ValidationUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTests {

    @LocalServerPort
    int port;
    @Autowired
    AuthService authService;
    @Autowired
    ValidationUtils validationUtils;
    @Autowired
    AuthController authController;
    @Autowired
    TestRestTemplate restTemplate;
    @Autowired
    UserModelRepository userModelRepository;

    LoginDTO testLogin;
    RegisterDTO testRegister;

    @BeforeEach
    void init() {
        testLogin = new LoginDTO();
        testLogin.setUsername("test@test.com");
        testLogin.setPassword("testPassword");

        testRegister = new RegisterDTO();
        testRegister.setUsername("test@test.com");
        testRegister.setPassword("testPassword");
        testRegister.setFirstName("testFirstName");
        testRegister.setLastName("testLastName");
        testRegister.setPhone("+7(000)000-00-00");
        testRegister.setRole(Role.ADMIN);
    }

    @AfterEach
    void cleanDB() {
        userModelRepository.deleteAll();
    }

    @Test
    void loginForOK() {
        restTemplate.postForEntity("http://localhost:" + port + "/register", testRegister, ResponseEntity.class);

        ResponseEntity<?> response = restTemplate.postForEntity("http://localhost:" + port + "/login", testLogin,
                ResponseEntity.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void loginForUNAUTHORIZED() {
        restTemplate.postForEntity("http://localhost:" + port + "/register", testRegister, ResponseEntity.class);
        testLogin.setPassword("NotCorrectPass");

        ResponseEntity<?> response = restTemplate.postForEntity("http://localhost:" + port + "/login", testLogin,
                ResponseEntity.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void registerForCREATED() {
        ResponseEntity<?> response = restTemplate.postForEntity("http://localhost:" + port + "/register",
                testRegister, ResponseEntity.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void registerForBAD_REQUEST() {
        restTemplate.postForEntity("http://localhost:" + port + "/register", testRegister, ResponseEntity.class);

        ResponseEntity<?> response = restTemplate.postForEntity("http://localhost:" + port + "/register",
                testRegister, ResponseEntity.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
