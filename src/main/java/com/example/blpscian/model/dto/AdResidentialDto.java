package com.example.blpscian.model.dto;

import com.example.blpscian.model.Location;
import com.example.blpscian.model.enums.AdType;
import com.example.blpscian.model.enums.CommercialType;
import com.example.blpscian.model.enums.ResidentialType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdResidentialDto {
    private AdType adType;

    private ResidentialType residentialType;

    private String address;

    private int amountOfRooms;

    private Double area;

    private int floor;

    private int price;

    private String description;
}
