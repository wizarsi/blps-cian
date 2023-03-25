package com.example.blpscian.model.dto;


import com.example.blpscian.model.enums.CommercialType;
import lombok.*;

import java.util.Objects;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchCommercialAdDto extends SearchAdDto{

    private Set<CommercialType> commercialTypes;

    private Double area;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SearchCommercialAdDto that = (SearchCommercialAdDto) o;
        return Objects.equals(commercialTypes, that.commercialTypes) && Objects.equals(area, that.area);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), commercialTypes, area);
    }
}
