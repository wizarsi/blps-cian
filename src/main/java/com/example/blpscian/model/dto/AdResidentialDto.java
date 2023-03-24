package com.example.blpscian.model.dto;

import com.example.blpscian.model.Location;
import com.example.blpscian.model.enums.AdType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdResidentialDto {
    private AdType adType;

    private Location location;

    private int amountOfRooms;

    private Double area;

    private int floor;

    private int price;
}
