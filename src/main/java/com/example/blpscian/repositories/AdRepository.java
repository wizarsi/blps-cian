package com.example.blpscian.repositories;

import com.example.blpscian.model.Ad;
import com.example.blpscian.model.User;
import com.example.blpscian.model.enums.AdType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdRepository<T extends Ad> extends JpaRepository<Ad, Long> {
    @Query("SELECT e FROM Ad e WHERE e.adType = :adType AND e.location IN (SELECT l FROM Location l WHERE l.address = :address) AND e.price >=:priceMin AND e.price <=:priceMax")
    List<T> findEntitiesByAdDto(@Param("adType") AdType adType, @Param("address") String address, @Param("priceMin") int priceMin, @Param("priceMax") int priceMax);

    List<Ad> findAllByUser(User user);
}
