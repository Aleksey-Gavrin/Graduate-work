package ru.skypro.homework.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import ru.skypro.homework.dto.Role;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

/**
 * Модель для хранения и обработки сущности "пользователь"
 */
@Getter
@Setter
@Entity
@Table(name = "users")
public class UserModel {
    /**
     * Идентификатор пользователя. Генерируется на уровне базы данных
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * Адрес электронной почты, указанный пользователем при регистрации в приложении. Используется для идентификации
     * пользователя при авторизации, а также внутри логики приложения. Поэтому имеет ограничения уникальности и не может
     * быть Null.
     */
    @Column(name = "user_name", nullable = false, unique = true)
    private String email;
    /**
     * Пароль для входа в личный кабинет и доступа ко всем эндпоинтам, требующим аутентификации. Хранится в базе данных
     * в зашифрованном виде.
     */
    private String password;
    /**
     * Имя пользователя, указанное при регистрации
     */
    private String firstName;
    /**
     * Фамилия пользователя, указанная при регистрации
     */
    private String lastName;
    /**
     * Номер телефона пользователя, указанный при регистрации. Значение поля валидируется при помощи регулярного выражения,
     * прописанного в классе {@link ru.skypro.homework.dto.UpdateUserDTO}
     */
    private String phone;
    /**
     * Роль пользователя в системе, определяющая набор доступных ему функций. Задается при регистрации пользователя.
     * Поле содержит одно из константных значений, определенных в классе.
     */
    private Role role;
    /**
     * Ссылка на эндпоинт, по которому доступна загрузка изображения - аватара пользователя. Генерируется в процессе обработки
     * запроса на обновление аватара пользователя в методе updateUserAvatar() в сервисе {@link ru.skypro.homework.service.UserService}
     */
    private String image;
    /**
     * Список сущностей - объявлений {@link AdModel}, которые связаны с пользователем. Для извлечения из базы данных
     * объявлений при обращении к сущности пользователя используется тип извлечения LAZY для нивелирования эффекта N+1
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @JsonIgnore
    private List<AdModel> ads;
    /**
     * Список сущностей - комментариев {@link CommentModel}, авторами которых является пользователь. Для извлечения из базы данных
     * комментариев при обращении к сущности пользователя используется тип извлечения LAZY для нивелирования эффекта N+1
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @JsonIgnore
    private List<CommentModel> comments;

    public UserModel() {
    }

    public UserModel(String email, String password, String firstName, String lastName, String phone, Role role) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserModel userModel = (UserModel) o;
        return Objects.equals(getEmail(), userModel.getEmail()) && Objects.equals(getPassword(), userModel.getPassword())
                && Objects.equals(getFirstName(), userModel.getFirstName()) && Objects.equals(getLastName(),
                userModel.getLastName()) && Objects.equals(getPhone(), userModel.getPhone()) && getRole() == userModel.getRole()
                && Objects.equals(getImage(), userModel.getImage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmail(), getPassword(), getFirstName(), getLastName(), getPhone(), getRole(), getImage());
    }
}
