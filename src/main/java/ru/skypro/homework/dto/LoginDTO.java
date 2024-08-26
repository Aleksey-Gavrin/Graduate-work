package ru.skypro.homework.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class LoginDTO {

    @Email(message = "E-mail should be valid")
    @Size(min = 4, max = 32, message = "E-mail length should be between 4 and 32 symbols")
    private String username;

    @NotBlank(message = "The field 'password' should be filled")
    @Size(min = 8, max = 16, message = "Password length should be between 8 and 16 symbols")
    private String password;
}
