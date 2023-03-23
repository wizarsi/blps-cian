package com.example.blpscian.model;

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
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<CommercialType> commercialTypes;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AdCommercial that = (AdCommercial) o;
        return Objects.equals(commercialTypes, that.commercialTypes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), commercialTypes);
    }
}
