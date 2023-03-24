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
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<ResidentialType> residentialTypes;

    public AdResidential(AdType adType, Location newLocation, Double area, int floor, int price, String description) {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AdResidential that = (AdResidential) o;
        return Objects.equals(residentialTypes, that.residentialTypes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), residentialTypes);
    }
}
