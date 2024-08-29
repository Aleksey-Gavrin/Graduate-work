package ru.skypro.homework.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import ru.skypro.homework.dto.Role;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "users")
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "user_name", nullable = false, unique = true)
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private Role role;
    private String image;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<AdModel> ads;

    @OneToMany(mappedBy = "user")
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
