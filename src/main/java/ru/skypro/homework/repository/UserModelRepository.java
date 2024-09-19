package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.model.UserModel;

import java.util.Optional;
/**
 * Репозиторий для хранения сущностей "пользователь"
 */
public interface UserModelRepository extends JpaRepository<UserModel, Integer> {
    /**
     * Метод реализует поиск пользователя в базе данных по полю 'username', игнорируя регистр символов
     * @param username - строковое значение поля в таблице users. В рамках логики приложения соответствует адресу
     *                 электронной почты пользователя, указанному при регистрации
     * @return - найденную сущность пользователя, обернутую в Optional, либо пустой Optional, если пользователь не найден
     */
    Optional<UserModel> findOneByEmailIgnoreCase(String username);
}
