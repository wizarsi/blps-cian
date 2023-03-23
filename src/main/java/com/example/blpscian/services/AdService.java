package com.example.blpscian.services;

import com.example.blpscian.model.Ad;
import com.example.blpscian.repositories.AdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdService<T extends Ad>{
    private final AdRepository<T>repository;

    @Autowired
    public AdService(AdRepository<T> repository) {
        this.repository = repository;
    }

    public List<T> findAll() {
        return repository.findAll();
    }

}
