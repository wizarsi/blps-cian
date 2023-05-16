package com.example.blpscian.model;

import com.example.blpscian.model.enums.AdType;
import com.example.blpscian.model.enums.CommercialType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdCommercial extends Ad {
    @Enumerated(EnumType.STRING)
    private CommercialType commercialType;


    public AdCommercial(AdType adType, Location location, Double area, int floor, int price, String description, User user, CommercialType commercialType, LocalDateTime publishedAt) {
        super(adType, location, area, floor, price, description,user, publishedAt);
        this.commercialType = commercialType;
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
