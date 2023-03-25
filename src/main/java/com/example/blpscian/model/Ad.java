package com.example.blpscian.model;

import com.example.blpscian.model.enums.AdType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ad {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "ad_type")
    private AdType adType;

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

    @Column(name = "description")
    private String description;

    public Ad(AdType adType, Location location, Double area, int floor, int price, String description) {
        this.adType = adType;
        this.location = location;
        this.area = area;
        this.floor = floor;
        this.price = price;
        this.description = description;
    }

    public Ad(AdType adType, Location location, Double area, int amountOfRooms,  int floor, int price, String description) {
        this.adType = adType;
        this.location = location;
        this.amountOfRooms = amountOfRooms;
        this.area = area;
        this.floor = floor;
        this.price = price;
        this.description = description;
    }
}
