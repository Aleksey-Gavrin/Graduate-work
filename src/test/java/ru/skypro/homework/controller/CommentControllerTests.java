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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.dto.mapper.CommentMapper;
import ru.skypro.homework.model.AdModel;
import ru.skypro.homework.model.CommentModel;
import ru.skypro.homework.repository.AdModelRepository;
import ru.skypro.homework.repository.CommentModelRepository;
import ru.skypro.homework.repository.UserModelRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.utils.AuthUtils;
import ru.skypro.homework.utils.ValidationUtils;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class CommentControllerTests {

    @LocalServerPort
    int port;
    @Autowired
    ValidationUtils validationUtils;
    @Autowired
    CommentService commentService;
    @Autowired
    CommentMapper mapper;
    @Autowired
    AuthUtils authUtils;

    @Autowired
    CommentController commentController;

    @Autowired
    TestRestTemplate restTemplate;
    @Autowired
    UserModelRepository userModelRepository;
    @Autowired
    AdModelRepository adModelRepository;
    @Autowired
    CommentModelRepository commentModelRepository;
    @Autowired
    AdService adService;

    int adId;

    @BeforeEach
    void init() {

        RegisterDTO testRegisterAdmin = new RegisterDTO();
        testRegisterAdmin.setUsername("admin@test.com");
        testRegisterAdmin.setPassword("testPassword");
        testRegisterAdmin.setFirstName("testFirstName");
        testRegisterAdmin.setLastName("testLastName");
        testRegisterAdmin.setPhone("+7(000)000-00-00");
        testRegisterAdmin.setRole(Role.ADMIN);

        restTemplate.postForEntity("http://localhost:" + port + "/register", testRegisterAdmin, ResponseEntity.class);

        RegisterDTO testRegisterUser = new RegisterDTO();
        testRegisterUser.setUsername("user@test.com");
        testRegisterUser.setPassword("testPassword");
        testRegisterUser.setFirstName("testFirstName");
        testRegisterUser.setLastName("testLastName");
        testRegisterUser.setPhone("+7(000)000-00-00");
        testRegisterUser.setRole(Role.USER);

        restTemplate.postForEntity("http://localhost:" + port + "/register", testRegisterUser, ResponseEntity.class);

        CreateOrUpdateAdDTO properties = new CreateOrUpdateAdDTO();
        properties.setTitle("testTitle");
        properties.setDescription("testDescription");
        properties.setPrice(1000);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("properties", properties);
        body.add("image", new FileSystemResource("src/test/data/TestImage.jpg"));

        RequestEntity<MultiValueMap<String, Object>> request = new RequestEntity<>(body, headers, HttpMethod.POST,
                URI.create("http://localhost:" + port + "/ads"));
        ResponseEntity<AdDTO> response = restTemplate.withBasicAuth("admin@test.com", "testPassword")
                .exchange(request, AdDTO.class);

        List<AdModel> adModelList = adService.getAllAds();
        assertFalse(adModelList.isEmpty());
        adId = adModelList.get(0).getId();
    }

    @AfterEach
    void cleanDB() {
        commentModelRepository.deleteAll();
        adModelRepository.deleteAll();
        userModelRepository.deleteAll();
    }

    @Test
    void addCommentForOK() {
        CreateOrUpdateCommentDTO testProperties = new CreateOrUpdateCommentDTO();
        testProperties.setText("testCommentText");

        ResponseEntity<CommentDTO> response = restTemplate.withBasicAuth("admin@test.com", "testPassword")
                .postForEntity("http://localhost:" + port + "/ads/" + adId + "/comments", testProperties, CommentDTO.class);

        assertTrue(response.hasBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void addCommentForUNAUTHORIZED() {
        CreateOrUpdateCommentDTO testProperties = new CreateOrUpdateCommentDTO();
        testProperties.setText("testCommentText");

        ResponseEntity<CommentDTO> response = restTemplate.withBasicAuth("admin@test.com", "WRONGPassword")
                .postForEntity("http://localhost:" + port + "/ads/" + adId + "/comments", testProperties, CommentDTO.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void addCommentForNOT_FOUND() {
        CreateOrUpdateCommentDTO testProperties = new CreateOrUpdateCommentDTO();
        testProperties.setText("testCommentText");

        adId = -1;

        ResponseEntity<String> response = restTemplate.withBasicAuth("admin@test.com", "testPassword")
                .postForEntity("http://localhost:" + port + "/ads/" + adId + "/comments", testProperties, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getCommentsForOK() {
        addCommentForOK();
        addCommentForOK();

        ResponseEntity<CommentsDTO> response = restTemplate.withBasicAuth("admin@test.com", "testPassword")
                .getForEntity("http://localhost:" + port + "/ads/" + adId + "/comments", CommentsDTO.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().getCount());
    }

    @Test
    void getCommentsForUNAUTHORIZED() {
        addCommentForOK();

        ResponseEntity<CommentsDTO> response = restTemplate.withBasicAuth("admin@test.com", "WRONGPassword")
                .getForEntity("http://localhost:" + port + "/ads/" + adId + "/comments", CommentsDTO.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void getCommentsForNOT_FOUND() {
        addCommentForOK();

        adId = -1;

        ResponseEntity<String> response = restTemplate.withBasicAuth("admin@test.com", "testPassword")
                .getForEntity("http://localhost:" + port + "/ads/" + adId + "/comments", String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteCommentForOK() {
        addCommentForOK();

        List<CommentModel> commentModelList = commentService.getAllComments(adId);
        assertFalse(commentModelList.isEmpty());
        int commentId = commentModelList.get(0).getId();

        RequestEntity<?> request = new RequestEntity<>(HttpMethod.DELETE, URI.create("http://localhost:" + port + "/ads/"
                + adId + "/comments/" + commentId));
        ResponseEntity<String> response = restTemplate.withBasicAuth("admin@test.com", "testPassword")
                .exchange(request, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deleteCommentForUNAUTHORIZED() {
        addCommentForOK();

        List<CommentModel> commentModelList = commentService.getAllComments(adId);
        assertFalse(commentModelList.isEmpty());
        int commentId = commentModelList.get(0).getId();

        RequestEntity<?> request = new RequestEntity<>(HttpMethod.DELETE, URI.create("http://localhost:" + port + "/ads/"
                + adId + "/comments/" + commentId));
        ResponseEntity<String> response = restTemplate.withBasicAuth("admin@test.com", "WRONGPassword")
                .exchange(request, String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void deleteCommentForFORBIDDEN() {
        addCommentForOK();

        List<CommentModel> commentModelList = commentService.getAllComments(adId);
        assertFalse(commentModelList.isEmpty());
        int commentId = commentModelList.get(0).getId();

        RequestEntity<?> request = new RequestEntity<>(HttpMethod.DELETE, URI.create("http://localhost:" + port + "/ads/"
                + adId + "/comments/" + commentId));
        ResponseEntity<String> response = restTemplate.withBasicAuth("user@test.com", "testPassword")
                .exchange(request, String.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void deleteCommentForNOT_FOUND() {
        addCommentForOK();

        int commentId = -1;

        RequestEntity<?> request = new RequestEntity<>(HttpMethod.DELETE, URI.create("http://localhost:" + port + "/ads/"
                + adId + "/comments/" + commentId));
        ResponseEntity<String> response = restTemplate.withBasicAuth("admin@test.com", "testPassword")
                .exchange(request, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateCommentForOK() {
        CreateOrUpdateCommentDTO testProperties = new CreateOrUpdateCommentDTO();
        testProperties.setText("textUpdated");

        addCommentForOK();

        List<CommentModel> commentModelList = commentService.getAllComments(adId);
        assertFalse(commentModelList.isEmpty());
        int commentId = commentModelList.get(0).getId();

        RequestEntity<CreateOrUpdateCommentDTO> request = new RequestEntity<>(testProperties, HttpMethod.PATCH,
                URI.create("http://localhost:" + port + "/ads/" + adId + "/comments/" + commentId));
        ResponseEntity<CreateOrUpdateCommentDTO> response = restTemplate.withBasicAuth("admin@test.com", "testPassword")
                .exchange(request, CreateOrUpdateCommentDTO.class);

        assertTrue(response.hasBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void updateCommentForUNAUTHORIZED() {
        CreateOrUpdateCommentDTO testProperties = new CreateOrUpdateCommentDTO();
        testProperties.setText("textUpdated");

        addCommentForOK();

        List<CommentModel> commentModelList = commentService.getAllComments(adId);
        assertFalse(commentModelList.isEmpty());
        int commentId = commentModelList.get(0).getId();

        RequestEntity<CreateOrUpdateCommentDTO> request = new RequestEntity<>(testProperties, HttpMethod.PATCH,
                URI.create("http://localhost:" + port + "/ads/" + adId + "/comments/" + commentId));
        ResponseEntity<CreateOrUpdateCommentDTO> response = restTemplate.withBasicAuth("admin@test.com", "WRONGPassword")
                .exchange(request, CreateOrUpdateCommentDTO.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void updateCommentForFORBIDDEN() {
        CreateOrUpdateCommentDTO testProperties = new CreateOrUpdateCommentDTO();
        testProperties.setText("textUpdated");

        addCommentForOK();

        List<CommentModel> commentModelList = commentService.getAllComments(adId);
        assertFalse(commentModelList.isEmpty());
        int commentId = commentModelList.get(0).getId();

        RequestEntity<CreateOrUpdateCommentDTO> request = new RequestEntity<>(testProperties, HttpMethod.PATCH,
                URI.create("http://localhost:" + port + "/ads/" + adId + "/comments/" + commentId));
        ResponseEntity<CreateOrUpdateCommentDTO> response = restTemplate.withBasicAuth("user@test.com", "testPassword")
                .exchange(request, CreateOrUpdateCommentDTO.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void updateCommentForNOT_FOUND() {
        CreateOrUpdateCommentDTO testProperties = new CreateOrUpdateCommentDTO();
        testProperties.setText("textUpdated");

        addCommentForOK();

        int commentId = -1;

        RequestEntity<CreateOrUpdateCommentDTO> request = new RequestEntity<>(testProperties, HttpMethod.PATCH,
                URI.create("http://localhost:" + port + "/ads/" + adId + "/comments/" + commentId));
        ResponseEntity<String> response = restTemplate.withBasicAuth("admin@test.com", "testPassword")
                .exchange(request, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
