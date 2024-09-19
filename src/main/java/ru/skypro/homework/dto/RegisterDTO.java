package ru.skypro.homework.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class RegisterDTO {

    @Email(message = "E-mail should be valid")
    @Size(min = 4, max = 32, message = "E-mail length should be between 4 and 32 symbols")
    private String username;

    @NotBlank(message = "The field 'password' should be filled")
    @Size(min = 8, max = 16, message = "Password length should be between 8 and 16 symbols")
    private String password;

    @NotBlank(message = "The field 'firstName' should be filled")
    @Size(min = 2, max = 16, message = "Firstname should contains a number of characters between 2 and 16")
    private String firstName;

    @NotBlank(message = "The field 'lastName' should be filled")
    @Size(min = 2, max = 16, message = "Lastname should contains a number of characters between 2 and 16")
    private String lastName;

    @NotBlank(message = "The field 'phone' should be filled")
    @Pattern(regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}", message = "Not valid phone-number format." +
            " Example of valid format: +7(000)000-00-00")
    private String phone;

    private Role role;
}
