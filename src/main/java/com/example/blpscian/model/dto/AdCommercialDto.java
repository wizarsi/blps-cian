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
public class AdCommercialDto extends AdDto{
    private CommercialType commercialType;

    public AdCommercialDto(AdType adType, CommercialType commercialType, String address, Double area, int floor, int price, String description) {
    }
}
