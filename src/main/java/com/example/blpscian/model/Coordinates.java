package com.example.blpscian.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "coordinates")
@NoArgsConstructor
public class Coordinates {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "latitude")
    private Double latitude;
    @Column(name = "longitude")
    private Double longitude;

    public Coordinates(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
