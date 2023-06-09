package com.example.blpscian.model;

import com.example.blpscian.model.enums.AdType;
import com.example.blpscian.model.enums.ResidentialType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdResidential extends Ad {
    @Enumerated(EnumType.STRING)
    private ResidentialType residentialType;

    public AdResidential(AdType adType, Location newLocation, Double area, int amountOfRooms, int floor, int price, String description, User user, ResidentialType residentialType, LocalDateTime publishedAt) {
        super(adType, newLocation, area, amountOfRooms, floor, price, description, user, publishedAt);
        this.residentialType = residentialType;
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
