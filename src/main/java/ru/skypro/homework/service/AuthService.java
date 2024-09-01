package ru.skypro.homework.service;

import ru.skypro.homework.dto.RegisterDTO;

/**
 * Сервис, содержащий методы для авторизации и регистрации пользователей
 */
public interface AuthService {
    /**
     * Проводит авторизацию пользователя в системе
     * @param userName - адрес электронной почты пользователя для идентификации его в системе
     * @param password - пароль в незашифрованном виде
     * @return true, если авторизация успешна, false - если нет
     */
    boolean login(String userName, String password);

    /**
     * Регистрирует в системе нового пользователя с переданными параметрами
     * @param register - DTO сущности "пользователь", содержащий необходимый для регистрации нового пользователя набор полей
     * @return true, если регистрация прошла успешно, false - если пользователь уже был зарегистрирован ранее
     */
    boolean register(RegisterDTO register);
}
