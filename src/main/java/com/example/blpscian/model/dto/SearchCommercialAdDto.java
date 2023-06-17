package com.example.blpscian.model.dto;


import com.example.blpscian.model.enums.AdType;
import com.example.blpscian.model.enums.CommercialType;
import lombok.*;

import java.util.Objects;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
public class SearchCommercialAdDto extends SearchAdDto{

    private Set<CommercialType> commercialTypes;

    private int areaMin;
    private int areaMax;

    public SearchCommercialAdDto(AdType adType, String address, int priceMin, int priceMax, Set<CommercialType> commercialTypes, int areaMin, int areaMax) {
        super(adType, address, priceMin, priceMax);
        this.commercialTypes = commercialTypes;
        this.areaMin = areaMin;
        this.areaMax = areaMax;
    }

    public SearchCommercialAdDto(Set<CommercialType> commercialTypes, int areaMin, int areaMax) {
        this.commercialTypes = commercialTypes;
        this.areaMin = areaMin;
        this.areaMax = areaMax;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SearchCommercialAdDto that = (SearchCommercialAdDto) o;
        return Objects.equals(commercialTypes, that.commercialTypes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), commercialTypes);
    }
}
