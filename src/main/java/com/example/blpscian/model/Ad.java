package com.example.blpscian.model;

import com.example.blpscian.model.enums.AdType;
import com.example.blpscian.model.enums.PropertyType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "ads")
@NoArgsConstructor
public class Ad {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "ad_type")
    private AdType adType;

    @Enumerated(EnumType.STRING)
    @Column(name = "property_type")
    private PropertyType propertyType;

    @ManyToOne
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;

    @Column(name = "amount_of_rooms")
    private int amountOfRooms;

    @Column(name = "area")
    private Double area;

    @Column(name = "floor")
    private int floor;

    @Column(name = "price")
    private int price;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
