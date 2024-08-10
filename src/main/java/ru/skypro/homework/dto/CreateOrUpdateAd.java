package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class CreateOrUpdateAd {

    String title;
    Integer price;
    String description;
}
