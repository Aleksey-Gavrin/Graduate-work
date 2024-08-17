package ru.skypro.homework.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class NewPasswordDTO {

    @NotBlank
    @Size(min = 8, max = 16, message = "Password length should be between 8 and 16 symbols")
    private String currentPassword;

    @NotBlank
    @Size(min = 8, max = 16, message = "Password length should be between 8 and 16 symbols")
    private String newPassword;
}
