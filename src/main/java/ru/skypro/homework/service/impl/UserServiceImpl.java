package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.RegisterDTO;
import ru.skypro.homework.dto.UpdateUserDTO;
import ru.skypro.homework.dto.mapper.UserMapper;
import ru.skypro.homework.exception.EntityModelNotFoundException;
import ru.skypro.homework.model.UserModel;
import ru.skypro.homework.repository.UserModelRepository;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.utils.ValidationUtils;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Value("${avatars.dir.path:./data}")
    private String avatarsDir;
    @Value("${avatars.download.url}")
    private String avatarDownloadURL;
    private final UserModelRepository userModelRepository;
    private final UserMapper userMapper;
    private final ValidationUtils validationUtils;

    public UserServiceImpl(UserModelRepository userModelRepository, UserMapper userMapper, ValidationUtils validationUtils) {
        this.userModelRepository = userModelRepository;
        this.userMapper = userMapper;
        this.validationUtils = validationUtils;
    }

    public void createUserWithRegistrationInfo(UserDetails userDetails, RegisterDTO register) {
        UserModel creatingUser = new UserModel();
        creatingUser.setEmail(userDetails.getUsername());
        creatingUser.setPassword(userDetails.getPassword());
        creatingUser.setFirstName(register.getFirstName());
        creatingUser.setLastName(register.getLastName());
        creatingUser.setRole(register.getRole());
        creatingUser.setPhone(register.getPhone());
        userModelRepository.save(creatingUser);
    }

    @Override
    public UserModel findUserByUserName(String username) throws EntityModelNotFoundException {
        return userModelRepository.findOneByEmailIgnoreCase(username)
                .orElseThrow(() -> new EntityModelNotFoundException("User = " + username + " not found"));

    }

    @Override
    public UserModel updateUser(String username, UpdateUserDTO update) throws EntityModelNotFoundException {
        UserModel existingUser = findUserByUserName(username);
        userMapper.mapUpdateUserDTOToUserModel(update, existingUser);
        return userModelRepository.save(existingUser);
    }

    @Override
    public void updateUserAvatar(String username, MultipartFile file) throws IOException {

        UserModel existingUser = findUserByUserName(username);

        if (file.getOriginalFilename() != null) {
            Path filePath = Path.of(avatarsDir, "avatar" + existingUser.getId() + "." + validationUtils.getFileExtension(file));
            Files.deleteIfExists(filePath);
            try {
                Files.createDirectory(filePath.getParent());
            } catch (FileAlreadyExistsException exception) {
                log.info("Directory already exists");
            }

            try (InputStream is = file.getInputStream();
                 OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                 BufferedInputStream bis = new BufferedInputStream(is, 1024);
                 BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
            ) {
                bis.transferTo(bos);
            }
        }
        existingUser.setImage(avatarDownloadURL + existingUser.getId());
        userModelRepository.save(existingUser);
    }

}
