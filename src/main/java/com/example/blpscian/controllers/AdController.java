package com.example.blpscian.controllers;

import com.example.blpscian.exceptions.InvalidDataException;
import com.example.blpscian.model.AdCommercial;
import com.example.blpscian.model.AdResidential;
import com.example.blpscian.model.dto.*;
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

    @PostMapping(value = "search/commercial")
    public ResponseEntity<?> searchCommercial(@RequestBody SearchCommercialAdDto searchCommercialAdDto) {
        return new ResponseEntity<>(adResidentialService.searchCommercialAds(searchCommercialAdDto), HttpStatus.OK);
    }

    @PostMapping(value = "search/residential")
    public ResponseEntity<?> searchResidential(@RequestBody SearchResidentialAdDto searchResidentialAdDto) {
        return new ResponseEntity<>(adResidentialService.searchResidentialAds(searchResidentialAdDto), HttpStatus.OK);

    }


    @PostMapping(value = "add/ad-residential")
    public ResponseEntity<?> addAdResidential(@RequestBody AdResidentialDto adDto) throws InvalidDataException {
        Map<Object, Object> model = new HashMap<>();
        adResidentialService.addResidentialAd(adDto);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @PostMapping(value = "add/ad-commercial")
    public ResponseEntity<?> addAdCommercial(@RequestBody AdCommercialDto adDto) throws InvalidDataException {
        Map<Object, Object> model = new HashMap<>();
        adCommercialService.addCommercialAd(adDto);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }
}
