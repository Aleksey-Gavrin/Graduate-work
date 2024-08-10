package ru.skypro.homework.model;

import lombok.Getter;
import lombok.Setter;
import ru.skypro.homework.dto.Role;
@Getter
@Setter
public class User {

    private Integer id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private Role role;
    private String image;

    public User() {
    }

    public User(Integer id, String email, String firstName, String lastName, String phone, Role role, String image) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.role = role;
        this.image = image;
    }
}
