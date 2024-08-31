package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.RegisterDTO;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.UpdateUserDTO;
import ru.skypro.homework.dto.mapper.UserMapper;
import ru.skypro.homework.exception.EntityModelNotFoundException;
import ru.skypro.homework.model.UserModel;
import ru.skypro.homework.repository.UserModelRepository;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.utils.ValidationUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = UserServiceImpl.class)
@ExtendWith(MockitoExtension.class)
public class UserServiceImplTests {

    @MockBean
    UserModelRepository userModelRepository;
    @SpyBean
    UserMapper userMapper;
    @MockBean
    ValidationUtils validationUtils;

    @Autowired
    UserService userService;

    UserModel testUserModel;

    @Value("${avatars.dir.path}")
    private String testAvatarsDir;
    @Value("${avatars.download.url}")
    private String testAvatarDownloadURL;

    @BeforeEach
    void init() {
        testUserModel = new UserModel("test@test.com", "testPassword", "testFirstName",
                "testLastName", "+7(000)000-00-00", Role.ADMIN);
        testUserModel.setId(1);
    }

    @Test
    void createUserWithRegistrationInfo() {
        UserDetails testUserDetails = User.builder()
                .username("test@test.com")
                .password("testPassword")
                .roles("ADMIN")
                .build();
        RegisterDTO testProperties = new RegisterDTO();
        testProperties.setFirstName("testFirstName");
        testProperties.setLastName("testLastName");
        testProperties.setRole(Role.ADMIN);
        testProperties.setPhone("+7(000)000-00-00");

        userService.createUserWithRegistrationInfo(testUserDetails, testProperties);

        verify(userModelRepository, times(1)).save(eq(testUserModel));
    }

    @Test
    void findUserByUserNamePositive() {
        when(userModelRepository.findOneByEmailIgnoreCase(testUserModel.getEmail())).thenReturn(Optional.of(testUserModel));

        UserModel actualUserModel = userService.findUserByUserName(testUserModel.getEmail());

        assertEquals(testUserModel, actualUserModel);
    }

    @Test
    void findUserByUserNameNegative() {
        assertThrows(EntityModelNotFoundException.class, ()->
                userService.findUserByUserName("invalidUsername"));
    }

    @Test
    void updateUser() {
        when(userModelRepository.findOneByEmailIgnoreCase(testUserModel.getEmail())).thenReturn(Optional.of(testUserModel));
        UpdateUserDTO testProperties = new UpdateUserDTO();
        testProperties.setPhone("+7(111)111-11-11");
        testProperties.setFirstName("updatedFirstName");
        testProperties.setLastName("updatedLastName");

        UserModel updatedUser = userService.updateUser(testUserModel.getEmail(), testProperties);

        assertEquals("+7(111)111-11-11", updatedUser.getPhone());
        assertEquals("updatedFirstName", updatedUser.getFirstName());
        assertEquals("updatedLastName", updatedUser.getLastName());
        verify(userModelRepository, times(1)).save(eq(updatedUser));
    }

    @Test
    void updateUserAvatar() throws IOException {
        when(userModelRepository.findOneByEmailIgnoreCase(testUserModel.getEmail())).thenReturn(Optional.of(testUserModel));
        byte[] testFileContent = Files.readAllBytes(Path.of(testAvatarsDir, "TestAvatar.jpg"));
        MultipartFile testImage = new MockMultipartFile("TestAvatar.jpg", testFileContent);
        when(validationUtils.getFileExtension(testImage)).thenReturn("jpg");

        userService.updateUserAvatar(testUserModel.getEmail(), testImage);

        assertEquals(testAvatarDownloadURL + "1", testUserModel.getImage());
        assertTrue(Files.exists(Path.of(testAvatarsDir, "image1.jpg")));
        verify(userModelRepository, times(1)).save(any(UserModel.class));
    }
}
