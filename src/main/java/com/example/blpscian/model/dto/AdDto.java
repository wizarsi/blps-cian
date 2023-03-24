package com.example.blpscian.model.dto;

import com.example.blpscian.model.enums.AdType;
import com.example.blpscian.model.enums.LocationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdDto {
    private AdType adType;

    private String address;

    private LocationType locationType;

    private int amountOfRooms;

    private Double area;

    private int floor;

    private int price;

    private String description;
}
