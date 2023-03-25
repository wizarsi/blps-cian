package com.example.blpscian.model.dto;

import com.example.blpscian.model.Location;
import com.example.blpscian.model.enums.AdType;
import com.example.blpscian.model.enums.CommercialType;
import com.example.blpscian.model.enums.ResidentialType;
import lombok.*;

import java.util.Objects;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
public class AdResidentialDto extends AdDto {
    private ResidentialType residentialType;
    private int amountOfRooms;

    public AdResidentialDto(AdType adType, String address, Double area, int floor, int price, String description, ResidentialType residentialType, int amountOfRooms) {
        super(adType, address, area, floor, price, description);
        this.residentialType = residentialType;
        this.amountOfRooms = amountOfRooms;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AdResidentialDto that = (AdResidentialDto) o;
        return amountOfRooms == that.amountOfRooms && residentialType == that.residentialType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), residentialType, amountOfRooms);
    }
}
