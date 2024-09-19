package ru.skypro.homework.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import ru.skypro.homework.utils.FileUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FileDownloadControllerTests {

    @LocalServerPort
    int port;

    @Autowired
    FileUtils fileUtils;

    @Autowired
    FileDownloadController fileDownloadController;

    @Autowired
    TestRestTemplate restTemplate;


    @Test
    void downloadAvatar() {
        ResponseEntity<String> jsonResponse = restTemplate.getForEntity("http://localhost:" + port +
                "/download/avatar/1", String.class);

        assertTrue(jsonResponse.hasBody());
        assertEquals(HttpStatus.OK, jsonResponse.getStatusCode());
        assertEquals(MediaType.IMAGE_JPEG, jsonResponse.getHeaders().getContentType());
    }

    @Test
    void downloadImage() {
        ResponseEntity<String> jsonResponse = restTemplate.getForEntity("http://localhost:" + port +
                "/download/image/1", String.class);

        assertTrue(jsonResponse.hasBody());
        assertEquals(HttpStatus.OK, jsonResponse.getStatusCode());
        assertEquals(MediaType.IMAGE_JPEG, jsonResponse.getHeaders().getContentType());
    }
}
