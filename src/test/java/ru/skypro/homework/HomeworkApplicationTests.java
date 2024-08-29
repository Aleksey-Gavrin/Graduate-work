package ru.skypro.homework;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.skypro.homework.controller.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class HomeworkApplicationTests {

    @Autowired
    AdController adController;
    @Autowired
    AuthController authController;
    @Autowired
    CommentController commentController;
    @Autowired
    FileDownloadController fileDownloadController;
    @Autowired
    UserController userController;

    @Test
    void contextLoads() {
        assertNotNull(adController);
        assertNotNull(commentController);
        assertNotNull(fileDownloadController);
        assertNotNull(authController);
        assertNotNull(userController);
    }

}
