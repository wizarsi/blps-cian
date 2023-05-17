package com.example.blpscian.repositories;

import com.example.blpscian.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Boolean existsByAddress(String address);

    Location getLocationByAddress(String address);
}
