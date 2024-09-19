package ru.skypro.homework.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class NewPasswordDTO {

    @NotBlank(message = "The field 'currentPassword' should be filled")
    @Size(min = 8, max = 16, message = "Password length should be between 8 and 16 symbols")
    private String currentPassword;

    @NotBlank(message = "The field 'newPassword' should be filled")
    @Size(min = 8, max = 16, message = "Password length should be between 8 and 16 symbols")
    private String newPassword;
}
