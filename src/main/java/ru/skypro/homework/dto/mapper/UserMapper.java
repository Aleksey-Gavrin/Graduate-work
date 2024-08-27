package ru.skypro.homework.dto.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.UpdateUserDTO;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.model.UserModel;
@Component
public class UserMapper {

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

    public void mapUpdateUserDTOToUserModel (UpdateUserDTO properties, UserModel userModel) {
        userModel.setFirstName(properties.getFirstName());
        userModel.setLastName(properties.getLastName());
        userModel.setPhone(properties.getPhone());
    }

    public UpdateUserDTO mapUserModelToUpdateUserDTO (UserModel userModel) {
        UpdateUserDTO properties = new UpdateUserDTO();
        properties.setFirstName(userModel.getFirstName());
        properties.setLastName(userModel.getLastName());
        properties.setPhone(userModel.getPhone());
        return properties;
    }
}
