package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.model.UserModel;
import ru.skypro.homework.repository.UserModelRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserDetailsManagerImplTests {
    @MockBean
    UserModelRepository userModelRepository;
    @MockBean
    PasswordEncoder passwordEncoder;

    @Autowired
    UserDetailsManagerImpl userDetailsManager;

    UserModel testUserModel;

    @BeforeEach
    void init() {
        testUserModel = new UserModel("user", "password", "testFirstName",
                "testLastName", "+7(000)000-00-00", Role.USER);
        testUserModel.setId(1);
    }

    @Test
    void loadUserByUsername() {
        when(userModelRepository.findOneByEmailIgnoreCase("user")).thenReturn(Optional.of(testUserModel));

        UserDetails userDetails = userDetailsManager.loadUserByUsername("user");

        assertEquals("user", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertEquals("ROLE_USER", userDetails.getAuthorities().toArray()[0].toString());
    }

    @Test
    void changePassword() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("user");
        SecurityContextHolder.setContext(securityContext);
        when(userModelRepository.findOneByEmailIgnoreCase("user")).thenReturn(Optional.of(testUserModel));
        when(passwordEncoder.encode("NewPassword")).thenReturn("EncodedPassword");

        userDetailsManager.changePassword("password", "NewPassword");

        assertEquals("EncodedPassword", testUserModel.getPassword());
        verify(userModelRepository, times(1)).save(testUserModel);
    }
}
