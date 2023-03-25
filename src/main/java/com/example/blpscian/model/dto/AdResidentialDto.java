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
public class AdResidentialDto extends AdDto {
    private ResidentialType residentialType;

    private int amountOfRooms;

    public AdResidentialDto(AdType adType, ResidentialType residentialType, String address, int amountOfRooms, Double area, int floor, int price, String description) {
    }
}
