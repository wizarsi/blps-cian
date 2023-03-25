package com.example.blpscian.model.dto;

import com.example.blpscian.model.Location;
import com.example.blpscian.model.enums.AdType;
import com.example.blpscian.model.enums.CommercialType;
import lombok.*;

import java.util.Objects;


@Getter
@Setter
@NoArgsConstructor
public class AdCommercialDto extends AdDto{
    private CommercialType commercialType;

    public AdCommercialDto(AdType adType, String address, Double area, int floor, int price, String description, CommercialType commercialType) {
        super(adType, address, area, floor, price, description);
        this.commercialType = commercialType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AdCommercialDto that = (AdCommercialDto) o;
        return commercialType == that.commercialType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), commercialType);
    }
}
