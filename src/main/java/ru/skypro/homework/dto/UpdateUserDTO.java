package ru.skypro.homework.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UpdateUserDTO {

    @NotBlank
    @Size(min = 3, max = 10, message = "Firstname should contains a number of characters between 3 and 10")
    private String firstName;

    @NotBlank
    @Size(min = 3, max = 10, message = "Lastname should contains a number of characters between 3 and 10")
    private String lastName;

    @Pattern(regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}", message = "Not valid phone-number format." +
            " Example of valid format: +7(000)000-00-00")
    private String phone;
}
