package com.example.blpscian.model.dto;

import com.example.blpscian.model.Location;
import com.example.blpscian.model.enums.AdType;
import com.example.blpscian.model.enums.CommercialType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdCommercialDto{
    private AdType adType;

    private CommercialType commercialType;

    private String address;

    private Double area;

    private int floor;

    private int price;

    private String description;
}
