package ru.skypro.homework.dto.mapper;

import ru.skypro.homework.dto.RegisterDTO;
import ru.skypro.homework.dto.UpdateUserDTO;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.model.UserModel;

public class UserMapper {

    public UserModel mapRegisterDTOToUserModel (UserModel userModel, RegisterDTO register) {
        userModel.setEmail(register.getUsername());
        userModel.setPassword(register.getPassword());
        userModel.setFirstName(register.getFirstName());
        userModel.setLastName(register.getLastName());
        userModel.setPhone(register.getPhone());
        userModel.setRole(register.getRole());
        return userModel;
    }

    public UserDTO mapUserModelToUserDTO (UserModel userModel) {
        UserDTO properties = new UserDTO();
        properties.setId(userModel.getId());
        properties.setEmail(userModel.getEmail());
        properties.setFirstName(userModel.getFirstName());
        properties.setLastName(userModel.getLastName());
        properties.setPhone(userModel.getPhone());
        properties.setRole(userModel.getRole());
        properties.setImage(userModel.getImage());
        return properties;
    }

    public UserModel mapUpdateUserDTOToUserModel (UserModel userModel, UpdateUserDTO properties) {
        userModel.setFirstName(properties.getFirstName());
        userModel.setLastName(properties.getLastName());
        userModel.setPhone(properties.getPhone());
        return userModel;
    }

    public UpdateUserDTO mapUserModelToUpdateUserDTO (UserModel userModel) {
        UpdateUserDTO properties = new UpdateUserDTO();
        properties.setFirstName(userModel.getFirstName());
        properties.setLastName(userModel.getLastName());
        properties.setPhone(userModel.getPhone());
        return properties;
    }
}
