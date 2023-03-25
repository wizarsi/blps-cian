package com.example.blpscian.model;

import com.example.blpscian.model.enums.AdType;
import com.example.blpscian.model.enums.CommercialType;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdCommercial extends Ad {
    @Enumerated(EnumType.STRING)
    private CommercialType commercialType;


    public AdCommercial(AdType adType, Location location, Double area, int floor, int price, String description) {
        super(adType, location, area, floor, price, description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AdCommercial that = (AdCommercial) o;
        return commercialType == that.commercialType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), commercialType);
    }
}
