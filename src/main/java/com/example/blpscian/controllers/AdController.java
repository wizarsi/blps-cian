package com.example.blpscian.controllers;

import com.example.blpscian.exceptions.InvalidDataException;
import com.example.blpscian.model.AdCommercial;
import com.example.blpscian.model.AdResidential;
import com.example.blpscian.model.dto.AdCommercialDto;
import com.example.blpscian.model.dto.AdDto;
import com.example.blpscian.model.dto.AdResidentialDto;
import com.example.blpscian.services.AdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("ad")
public class AdController {
    private AdService<AdResidential> adResidentialService;
    private AdService<AdCommercial> adCommercialService;

    @Autowired
    public AdController(AdService<AdResidential> adResidentialService, AdService<AdCommercial> adCommercialService) {
        this.adResidentialService = adResidentialService;
        this.adCommercialService = adCommercialService;
    }

    @PostMapping(value = "search-commercial")
    public ResponseEntity<?> searchCommercial(@RequestBody AdCommercialDto) {

    }

    @PostMapping(value = "search-residential")
    public ResponseEntity<?> searchResidential(@RequestBody AdResidentialDto) {

    }

    @PostMapping(value = "add/ad-residential")
    public ResponseEntity<?> addAdResidential(@RequestBody AdDto adDto) throws InvalidDataException {
        Map<Object, Object> model = new HashMap<>();
        adResidentialService.addAd(adDto);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @PostMapping(value = "add/ad-commercial")
    public ResponseEntity<?> addAdCommercial(@RequestBody AdDto adDto) throws InvalidDataException {
        Map<Object, Object> model = new HashMap<>();
        adCommercialService.addAd(adDto);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }
}
