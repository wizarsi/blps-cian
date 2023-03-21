package com.example.blpscian.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "locations")
@NoArgsConstructor
public class Location {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "address")
    private String address;

    @ManyToOne
    @JoinColumn(name = "coordinates_id", referencedColumnName = "id")
    private Coordinates coordinates;

}
