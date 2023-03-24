package com.example.blpscian.controllers;

import com.example.blpscian.model.dto.AdCommercialDto;
import com.example.blpscian.model.dto.AdResidentialDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("ad")
public class AdController {
    @PostMapping(value = "search-commercial")
    public ResponseEntity<?> searchCommercial(@RequestBody AdCommercialDto adCommercialDto) {
        return (ResponseEntity<?>) ResponseEntity.ok();
    }

    @PostMapping(value = "search-residential")
    public ResponseEntity<?> searchResidential(@RequestBody AdResidentialDto adResidentialDto) {
        return (ResponseEntity<?>) ResponseEntity.ok();
    }

    @GetMapping(value = "get-locations")
    public ResponseEntity<?> getLocations() {
        return (ResponseEntity<?>) ResponseEntity.ok();
    }
}
