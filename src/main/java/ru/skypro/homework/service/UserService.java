package ru.skypro.homework.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.RegisterDTO;
import ru.skypro.homework.dto.UpdateUserDTO;
import ru.skypro.homework.model.UserModel;

import java.io.IOException;

public interface UserService {

    void createUserWithRegistrationInfo (UserDetails userDetails, RegisterDTO register);

    UserModel findUserByUserName(String username);

    UserModel updateUser(String username, UpdateUserDTO update);

    void updateUserAvatar(String username, MultipartFile file) throws IOException;
}
