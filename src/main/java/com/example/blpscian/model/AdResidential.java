package com.example.blpscian.model;

import com.example.blpscian.model.enums.AdType;
import com.example.blpscian.model.enums.ResidentialType;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdResidential extends Ad{
    @Enumerated(EnumType.STRING)
    private ResidentialType residentialType;

    public AdResidential(AdType adType, Location newLocation, Double area, int amountOfRooms, int floor, int price, String description) {
        super(adType, newLocation, area, amountOfRooms, floor, price, description);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AdResidential that = (AdResidential) o;
        return residentialType == that.residentialType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), residentialType);
    }
}
