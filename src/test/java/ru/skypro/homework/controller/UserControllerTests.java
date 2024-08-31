package ru.skypro.homework.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.dto.mapper.UserMapper;
import ru.skypro.homework.repository.UserModelRepository;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.utils.ValidationUtils;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTests {

    @LocalServerPort
    int port;
    @Autowired
    UserDetailsManager manager;
    @Autowired
    ValidationUtils validationUtils;
    @Autowired
    UserMapper mapper;
    @Autowired
    UserService userService;
    @Autowired
    UserController userController;
    @Autowired
    TestRestTemplate restTemplate;
    @Autowired
    UserModelRepository userModelRepository;

    @BeforeEach
    void init() {

        RegisterDTO testRegister = new RegisterDTO();
        testRegister.setUsername("test@test.com");
        testRegister.setPassword("testPassword");
        testRegister.setFirstName("testFirstName");
        testRegister.setLastName("testLastName");
        testRegister.setPhone("+7(000)000-00-00");
        testRegister.setRole(Role.ADMIN);

        restTemplate.postForEntity("http://localhost:" + port + "/register", testRegister, ResponseEntity.class);
    }

    @AfterEach
    void cleanDB() {
        userModelRepository.deleteAll();
    }

    @Test
    void setPasswordForOK() {
        NewPasswordDTO newPasswordDTO = new NewPasswordDTO();
        newPasswordDTO.setCurrentPassword("testPassword");
        newPasswordDTO.setNewPassword("newTestPassword");

        ResponseEntity<String> response = restTemplate.withBasicAuth("test@test.com", "testPassword")
                .postForEntity("http://localhost:" + port + "/users/set_password", newPasswordDTO, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void setPasswordForUNAUTHORIZED() {
        NewPasswordDTO newPasswordDTO = new NewPasswordDTO();
        newPasswordDTO.setCurrentPassword("testPassword");
        newPasswordDTO.setNewPassword("newTestPassword");

        ResponseEntity<String> response = restTemplate.withBasicAuth("test@test.com", "WRONGPassword")
                .postForEntity("http://localhost:" + port + "/users/set_password", newPasswordDTO, String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void getUserForOK() {
        ResponseEntity<UserDTO> response = restTemplate.withBasicAuth("test@test.com", "testPassword")
                .getForEntity("http://localhost:" + port + "/users/me", UserDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getUserForUNAUTHORIZED() {
        ResponseEntity<UserDTO> response = restTemplate.withBasicAuth("test@test.com", "WRONGPassword")
                .getForEntity("http://localhost:" + port + "/users/me", UserDTO.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void updateUserForOK() {
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setFirstName("updated");
        updateUserDTO.setLastName("updated");
        updateUserDTO.setPhone("+7(111)111-11-11");

        RequestEntity<UpdateUserDTO> request = new RequestEntity<>(updateUserDTO, HttpMethod.PATCH,
                URI.create("http://localhost:" + port + "/users/me"));
        ResponseEntity<UpdateUserDTO> response = restTemplate.withBasicAuth("test@test.com", "testPassword")
                .exchange(request, UpdateUserDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void updateUserForUNAUTHORIZED() {
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setFirstName("updated");
        updateUserDTO.setLastName("updated");
        updateUserDTO.setPhone("+7(111)111-11-11");

        RequestEntity<UpdateUserDTO> request = new RequestEntity<>(updateUserDTO, HttpMethod.PATCH,
                URI.create("http://localhost:" + port + "/users/me"));
        ResponseEntity<UpdateUserDTO> response = restTemplate.withBasicAuth("test@test.com", "WRONGPassword")
                .exchange(request, UpdateUserDTO.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

    }

    @Test
    void updateUserImageForOK() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", new FileSystemResource("src/test/data/TestAvatar.jpg"));

        RequestEntity<MultiValueMap<String, Object>> request = new RequestEntity<>(body, headers, HttpMethod.PATCH,
                URI.create("http://localhost:" + port + "/users/me/image"));
        ResponseEntity<String> response = restTemplate.withBasicAuth("test@test.com", "testPassword")
                .exchange(request, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    void updateUserImageForUNAUTHORIZED() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", new FileSystemResource("src/test/data/TestAvatar.jpg"));

        RequestEntity<MultiValueMap<String, Object>> request = new RequestEntity<>(body, headers, HttpMethod.PATCH,
                URI.create("http://localhost:" + port + "/users/me/image"));
        ResponseEntity<String> response = restTemplate.withBasicAuth("test@test.com", "WRONGPassword")
                .exchange(request, String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

    }
}
