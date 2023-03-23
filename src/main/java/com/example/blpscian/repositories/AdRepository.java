package com.example.blpscian.repositories;

import com.example.blpscian.model.Ad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdRepository <T extends Ad> extends JpaRepository<T, Long> {

}
