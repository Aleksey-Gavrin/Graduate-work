package ru.skypro.homework.dto;

import lombok.Data;

import java.util.List;

@Data
public class AdsDTO {

    private int count;
    private List<AdDTO> results;
}
