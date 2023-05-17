package com.example.blpscian.controllers;

import com.example.blpscian.exceptions.InvalidDataException;
import com.example.blpscian.exceptions.TimeoutExceededException;
import com.example.blpscian.model.AdCommercial;
import com.example.blpscian.model.AdResidential;
import com.example.blpscian.model.dto.*;
import com.example.blpscian.services.AdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ads")
public class AdController {
    private final AdService<AdResidential> adResidentialService;
    private final AdService<AdCommercial> adCommercialService;

    @Autowired
    public AdController(AdService<AdResidential> adResidentialService, AdService<AdCommercial> adCommercialService) {
        this.adResidentialService = adResidentialService;
        this.adCommercialService = adCommercialService;
    }

    @PostMapping(value = "search/commercial")
    public ResponseEntity<?> searchCommercial(@RequestBody SearchCommercialAdDto searchCommercialAdDto) throws InvalidDataException {
        return new ResponseEntity<>(adResidentialService.searchCommercialAds(searchCommercialAdDto), HttpStatus.OK);
    }

    @PostMapping(value = "search/residential")
    public ResponseEntity<?> searchResidential(@RequestBody SearchResidentialAdDto searchResidentialAdDto) throws InvalidDataException {
        return new ResponseEntity<>(adResidentialService.searchResidentialAds(searchResidentialAdDto), HttpStatus.OK);
    }


    @PostMapping(value = "add/residential")
    public ResponseEntity<?> addAdResidential(@RequestBody AdResidentialDto adDto) throws InvalidDataException {
        Map<Object, Object> model = new HashMap<>();
        try {
            adResidentialService.addResidentialAd(adDto);
        } catch (TimeoutExceededException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @PostMapping(value = "add/commercial")
    public ResponseEntity<?> addAdCommercial(@RequestBody AdCommercialDto adDto) throws InvalidDataException {
        Map<Object, Object> model = new HashMap<>();
        try {
            adCommercialService.addCommercialAd(adDto);
        }catch (TimeoutExceededException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @DeleteMapping(value = "delete")
    public ResponseEntity<?> deleteAdsResidentialByUserEmail(@RequestParam String email) throws InvalidDataException {
        int deletedAdsCount = adResidentialService.deleteAdsByUser(email);
        return new ResponseEntity<>(deletedAdsCount, HttpStatus.OK);
    }
}
