package ru.skypro.homework.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.RegisterDTO;
import ru.skypro.homework.dto.UpdateUserDTO;
import ru.skypro.homework.model.UserModel;

import java.io.IOException;

/**
 * Сервис, содержащий бизнес-логику по обработке сущности "пользователь"
 */
public interface UserService {
    /**
     * Добавляет нового пользователя в базу данных с переданными параметрами
     * @param userDetails - часть параметров, содержащая основные данные, такие как логин и пароль
     * @param register - DTO сущности "пользователь", содержащий другие данные для регистрации, такие как имя, фамилия,
     *                 номер телефона и роль
     */
    void createUserWithRegistrationInfo (UserDetails userDetails, RegisterDTO register);

    /**
     * Ищет пользователя в базе данных по его имени (адресу электронной почты)
     * @param username - - адрес электронной почты пользователя
     * @return найденный пользователь
     */
    UserModel findUserByUserName(String username);

    /**
     * Обновляет данные существующего пользователя по его имени (адресу электронной почты)
     * @param username - адрес электронной почты пользователя
     * @param update - DTO сущности "пользователь", содержащий данные для обновления
     * @return обновленный пользователь
     */
    UserModel updateUser(String username, UpdateUserDTO update);

    /**
     * Обновляет изображение - аватар существующего пользователя по его имени (адресу электронной почты)
     * @param username - адрес электронной почты пользователя
     * @param file - файл изображения
     * @throws IOException - в случае ошибки чтения-записи файла изображения
     */
    void updateUserAvatar(String username, MultipartFile file) throws IOException;
}
