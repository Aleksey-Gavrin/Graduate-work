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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.dto.mapper.AdMapper;
import ru.skypro.homework.model.AdModel;
import ru.skypro.homework.repository.AdModelRepository;
import ru.skypro.homework.repository.UserModelRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.utils.AuthUtils;
import ru.skypro.homework.utils.FileUtils;
import ru.skypro.homework.utils.ValidationUtils;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdControllerTests {

    @LocalServerPort
    int port;
    @Autowired
    ValidationUtils validationUtils;
    @Autowired
    AdMapper adMapper;
    @Autowired
    AdService adService;
    @Autowired
    UserService userService;
    @Autowired
    FileUtils fileUtils;
    @Autowired
    AuthUtils authUtils;
    @Autowired
    AdController adController;
    @Autowired
    TestRestTemplate restTemplate;
    @Autowired
    UserModelRepository userModelRepository;
    @Autowired
    AdModelRepository adModelRepository;

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
    }

    @AfterEach
    void cleanDB() {
        adModelRepository.deleteAll();
        userModelRepository.deleteAll();
    }

    @Test
    void adAdForCREATED() {
        CreateOrUpdateAdDTO properties = new CreateOrUpdateAdDTO();
        properties.setTitle("testTitle");
        properties.setDescription("testDescription");
        properties.setPrice(1000);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String,Object> body = new LinkedMultiValueMap<>();
        body.add("properties", properties);
        body.add("image", new FileSystemResource("src/test/data/TestImage.jpg"));

        RequestEntity<MultiValueMap<String,Object>> request = new RequestEntity<>(body, headers, HttpMethod.POST,
                URI.create("http://localhost:" + port + "/ads"));
        ResponseEntity<AdDTO> response = restTemplate.withBasicAuth("admin@test.com", "testPassword")
                .exchange(request, AdDTO.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void adAdForUNAUTHORIZED() {
        CreateOrUpdateAdDTO properties = new CreateOrUpdateAdDTO();
        properties.setTitle("testTitle");
        properties.setDescription("testDescription");
        properties.setPrice(1000);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String,Object> body = new LinkedMultiValueMap<>();
        body.add("properties", properties);
        body.add("image", new FileSystemResource("src/test/data/TestImage.jpg"));

        RequestEntity<MultiValueMap<String,Object>> request = new RequestEntity<>(body, headers, HttpMethod.POST,
                URI.create("http://localhost:" + port + "/ads"));
        ResponseEntity<AdDTO> response = restTemplate.withBasicAuth("admin@test.com", "WRONGPassword")
                .exchange(request, AdDTO.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void getAllAdsForOK() {
        adAdForCREATED();

        ResponseEntity<AdsDTO> response = restTemplate.getForEntity("http://localhost:" + port + "/ads", AdsDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getCount());
    }

    @Test
    void getAdsForOK() {
        adAdForCREATED();
        List<AdModel> adModelList = adService.getAllAds();
        assertFalse(adModelList.isEmpty());
        int adId = adModelList.get(0).getId();

        ResponseEntity<ExtendedAdDTO> response = restTemplate.withBasicAuth("admin@test.com", "testPassword")
                .getForEntity("http://localhost:" + port + "/ads/" + adId, ExtendedAdDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getAdsForUNAUTHORIZED() {
        adAdForCREATED();
        List<AdModel> adModelList = adService.getAllAds();
        assertFalse(adModelList.isEmpty());
        int adId = adModelList.get(0).getId();

        ResponseEntity<ExtendedAdDTO> response = restTemplate.withBasicAuth("admin@test.com", "WRONGPassword")
                .getForEntity("http://localhost:" + port + "/ads/" + adId, ExtendedAdDTO.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void getAdsForNOT_FOUND() {
        adAdForCREATED();

        int adId = -1;

        ResponseEntity<String> response = restTemplate.withBasicAuth("admin@test.com", "testPassword")
                .getForEntity("http://localhost:" + port + "/ads/" + adId, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void removeAdForNO_CONTENT() {
        adAdForCREATED();
        List<AdModel> adModelList = adService.getAllAds();
        assertFalse(adModelList.isEmpty());
        int adId = adModelList.get(0).getId();

        RequestEntity<?> request = new RequestEntity<>(HttpMethod.DELETE, URI.create("http://localhost:" + port + "/ads/" + adId));
        ResponseEntity<String> response = restTemplate.withBasicAuth("admin@test.com", "testPassword")
                .exchange(request, String.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void removeAdForUNAUTHORIZED() {
        adAdForCREATED();
        List<AdModel> adModelList = adService.getAllAds();
        assertFalse(adModelList.isEmpty());
        int adId = adModelList.get(0).getId();

        RequestEntity<?> request = new RequestEntity<>(HttpMethod.DELETE, URI.create("http://localhost:" + port + "/ads/" + adId));
        ResponseEntity<String> response = restTemplate.withBasicAuth("admin@test.com", "WRONGPassword")
                .exchange(request, String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void removeAdForFORBIDDEN() {
        adAdForCREATED();
        List<AdModel> adModelList = adService.getAllAds();
        assertFalse(adModelList.isEmpty());
        int adId = adModelList.get(0).getId();

        RequestEntity<?> request = new RequestEntity<>(HttpMethod.DELETE, URI.create("http://localhost:" + port + "/ads/" + adId));
        ResponseEntity<String> response = restTemplate.withBasicAuth("user@test.com", "testPassword")
                .exchange(request, String.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void removeAdForNOT_FOUND() {
        adAdForCREATED();

        int adId = -1;

        RequestEntity<?> request = new RequestEntity<>(HttpMethod.DELETE, URI.create("http://localhost:" + port + "/ads/" + adId));
        ResponseEntity<String> response = restTemplate.withBasicAuth("admin@test.com", "testPassword")
                .exchange(request, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateAdsForOK() {
        adAdForCREATED();

        CreateOrUpdateAdDTO properties = new CreateOrUpdateAdDTO();
        properties.setTitle("updated");
        properties.setDescription("updatedDescription");
        properties.setPrice(500);

        List<AdModel> adModelList = adService.getAllAds();
        assertFalse(adModelList.isEmpty());
        int adId = adModelList.get(0).getId();

        RequestEntity<CreateOrUpdateAdDTO> request = new RequestEntity<>(properties, HttpMethod.PATCH, URI.create("http://localhost:" + port + "/ads/" + adId));
        ResponseEntity<AdDTO> response = restTemplate.withBasicAuth("admin@test.com", "testPassword")
                .exchange(request, AdDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void updateAdsForUNAUTHORIZED() {
        adAdForCREATED();

        CreateOrUpdateAdDTO properties = new CreateOrUpdateAdDTO();
        properties.setTitle("updated");
        properties.setDescription("updatedDescription");
        properties.setPrice(500);

        List<AdModel> adModelList = adService.getAllAds();
        assertFalse(adModelList.isEmpty());
        int adId = adModelList.get(0).getId();

        RequestEntity<CreateOrUpdateAdDTO> request = new RequestEntity<>(properties, HttpMethod.PATCH, URI.create("http://localhost:" + port + "/ads/" + adId));
        ResponseEntity<AdDTO> response = restTemplate.withBasicAuth("admin@test.com", "WRONGPassword")
                .exchange(request, AdDTO.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void updateAdsForFORBIDDEN() {
        adAdForCREATED();

        CreateOrUpdateAdDTO properties = new CreateOrUpdateAdDTO();
        properties.setTitle("updated");
        properties.setDescription("updatedDescription");
        properties.setPrice(500);

        List<AdModel> adModelList = adService.getAllAds();
        assertFalse(adModelList.isEmpty());
        int adId = adModelList.get(0).getId();

        RequestEntity<CreateOrUpdateAdDTO> request = new RequestEntity<>(properties, HttpMethod.PATCH, URI.create("http://localhost:" + port + "/ads/" + adId));
        ResponseEntity<AdDTO> response = restTemplate.withBasicAuth("user@test.com", "testPassword")
                .exchange(request, AdDTO.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void updateAdsForNOT_FOUND() {
        adAdForCREATED();

        CreateOrUpdateAdDTO properties = new CreateOrUpdateAdDTO();
        properties.setTitle("updated");
        properties.setDescription("updatedDescription");
        properties.setPrice(500);

        int adId = -1;

        RequestEntity<CreateOrUpdateAdDTO> request = new RequestEntity<>(properties, HttpMethod.PATCH,
                URI.create("http://localhost:" + port + "/ads/" + adId));
        ResponseEntity<String> response = restTemplate.withBasicAuth("admin@test.com", "testPassword")
                .exchange(request, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getAdsMeForOK() {
        adAdForCREATED();

        ResponseEntity<AdsDTO> response = restTemplate.withBasicAuth("admin@test.com", "testPassword")
                .getForEntity("http://localhost:" + port + "/ads/me", AdsDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getCount());
    }

    @Test
    void getAdsMeForUNAUTHORIZED() {
        adAdForCREATED();

        ResponseEntity<AdsDTO> response = restTemplate.withBasicAuth("admin@test.com", "WRONGPassword")
                .getForEntity("http://localhost:" + port + "/ads/me", AdsDTO.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void updateImageForOK() {
        adAdForCREATED();

        List<AdModel> adModelList = adService.getAllAds();
        assertFalse(adModelList.isEmpty());
        int adId = adModelList.get(0).getId();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String,Object> body = new LinkedMultiValueMap<>();
        body.add("image", new FileSystemResource("src/test/data/TestImage.jpg"));

        RequestEntity<MultiValueMap<String,Object>> request = new RequestEntity<>(body, headers, HttpMethod.PATCH,
                URI.create("http://localhost:" + port + "/ads/" + adId + "/image"));
        ResponseEntity<byte[]> response = restTemplate.withBasicAuth("admin@test.com", "testPassword")
                .exchange(request, byte[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.hasBody());
    }

    @Test
    void updateImageForUNAUTHORIZED() {
        adAdForCREATED();

        List<AdModel> adModelList = adService.getAllAds();
        assertFalse(adModelList.isEmpty());
        int adId = adModelList.get(0).getId();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String,Object> body = new LinkedMultiValueMap<>();
        body.add("image", new FileSystemResource("src/test/data/TestImage.jpg"));

        RequestEntity<MultiValueMap<String,Object>> request = new RequestEntity<>(body, headers, HttpMethod.PATCH,
                URI.create("http://localhost:" + port + "/ads/" + adId + "/image"));
        ResponseEntity<byte[]> response = restTemplate.withBasicAuth("admin@test.com", "WRONGPassword")
                .exchange(request, byte[].class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void updateImageForFORBIDDEN() {
        adAdForCREATED();

        List<AdModel> adModelList = adService.getAllAds();
        assertFalse(adModelList.isEmpty());
        int adId = adModelList.get(0).getId();
        System.out.println(adId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String,Object> body = new LinkedMultiValueMap<>();
        body.add("image", new FileSystemResource("src/test/data/TestImage.jpg"));

        RequestEntity<MultiValueMap<String,Object>> request = new RequestEntity<>(body, headers, HttpMethod.PATCH,
                URI.create("http://localhost:" + port + "/ads/" + adId + "/image"));
        ResponseEntity<byte[]> response = restTemplate.withBasicAuth("user@test.com", "testPassword")
                .exchange(request, byte[].class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void updateImageForNOT_FOUND() {
        adAdForCREATED();

        int adId = -1;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String,Object> body = new LinkedMultiValueMap<>();
        body.add("image", new FileSystemResource("src/test/data/TestImage.jpg"));

        RequestEntity<MultiValueMap<String,Object>> request = new RequestEntity<>(body, headers, HttpMethod.PATCH,
                URI.create("http://localhost:" + port + "/ads/" + adId + "/image"));
        ResponseEntity<String> response = restTemplate.withBasicAuth("admin@test.com", "testPassword")
                .exchange(request, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
