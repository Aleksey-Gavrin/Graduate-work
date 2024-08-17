package ru.skypro.homework.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Data
public class CreateOrUpdateAdDTO {
    @NotBlank
    @Size(min = 4, max = 32, message = "Title length should be between 4 and 32 symbols")
    private String title;

    @PositiveOrZero
    @Max(value = 10_000_000, message = "Price should be between 0 and 10_000_000")
    private int price;

    @NotBlank
    @Size(min = 8, max = 64, message = "Description length should be between 8 and 64 symbols")
    private String description;
}
