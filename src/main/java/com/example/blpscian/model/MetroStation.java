package com.example.blpscian.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "metro_stations")
@NoArgsConstructor
public class MetroStation {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne
    @JoinColumn(name = "coordinates_id", referencedColumnName = "id")
    private Coordinates coordinates;
}
