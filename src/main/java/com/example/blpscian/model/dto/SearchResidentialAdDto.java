package com.example.blpscian.model.dto;

import com.example.blpscian.model.enums.AdType;
import com.example.blpscian.model.enums.ResidentialType;
import lombok.*;

import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchResidentialAdDto extends SearchAdDto{

    private Set<ResidentialType> residentialTypes;

    private int amountOfRooms;

    private int areaMin;
    private int areaMax;

    public SearchResidentialAdDto(AdType adType, String address, int priceMin, int priceMax, Set<ResidentialType> residentialTypes, int amountOfRooms, int areaMin, int areaMax) {
        super(adType, address, priceMin, priceMax);
        this.residentialTypes = residentialTypes;
        this.amountOfRooms = amountOfRooms;
        this.areaMin = areaMin;
        this.areaMax = areaMax;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SearchResidentialAdDto that = (SearchResidentialAdDto) o;
        return amountOfRooms == that.amountOfRooms && Objects.equals(residentialTypes, that.residentialTypes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), residentialTypes, amountOfRooms);
    }
}
