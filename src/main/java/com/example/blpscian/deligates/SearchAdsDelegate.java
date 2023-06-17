package com.example.blpscian.deligates;

import com.example.blpscian.model.AdCommercial;
import com.example.blpscian.model.AdResidential;
import com.example.blpscian.model.dto.AdCommercialDto;
import com.example.blpscian.model.dto.AdResidentialDto;
import com.example.blpscian.model.dto.SearchCommercialAdDto;
import com.example.blpscian.model.dto.SearchResidentialAdDto;
import com.example.blpscian.model.enums.AdType;
import com.example.blpscian.model.enums.CommercialType;
import com.example.blpscian.model.enums.ResidentialType;
import com.example.blpscian.services.AdService;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import com.google.gson.Gson;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
@Named
@Slf4j
public class SearchAdsDelegate implements JavaDelegate {
    private final AdService<AdResidential> adResidentialService;
    private final AdService<AdCommercial> adCommercialService;

    public SearchAdsDelegate(AdService<AdResidential> adResidentialService, AdService<AdCommercial> adCommercialService) {
        this.adResidentialService = adResidentialService;
        this.adCommercialService = adCommercialService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) {
        Gson gson = new Gson();
        try {
            String realEstateType = delegateExecution.getVariable("realEstateType").toString();
            AdType adType = AdType.valueOf(delegateExecution.getVariable("adType").toString());
            String address = delegateExecution.getVariable("address").toString();
            int priceMin = Integer.parseInt(delegateExecution.getVariable("priceMin").toString());
            int priceMax = Integer.parseInt(delegateExecution.getVariable("priceMax").toString());

            if (realEstateType.equals("commercial")) {
                int areaMax = Integer.parseInt(delegateExecution.getVariable("maxCommercialArea").toString());
                int areaMin = Integer.parseInt(delegateExecution.getVariable("minCommercialArea").toString());
                String[] checkedBoxes = gson.fromJson(delegateExecution.getVariable("premisesCommercialTypes").toString(), String[].class);
                Set<CommercialType> premisesCommercialTypes = Arrays.stream(checkedBoxes)
                        .map(CommercialType::valueOf)
                        .collect(Collectors.toSet());
                SearchCommercialAdDto searchCommercialAdDto = new SearchCommercialAdDto(adType, address, priceMin, priceMax, premisesCommercialTypes, areaMin, areaMax);
                ArrayList<AdCommercialDto> commercialAds = (ArrayList<AdCommercialDto>) adCommercialService.searchCommercialAds(searchCommercialAdDto);
                delegateExecution.setVariable("result", commercialAds.toString());
            } else {
                String[] checkedBoxes = gson.fromJson(delegateExecution.getVariable("premisesResidentialTypes").toString(), String[].class);

                Set<ResidentialType> premisesResidentialTypes = Arrays.stream(checkedBoxes)
                        .map(ResidentialType::valueOf)
                        .collect(Collectors.toSet());

                SearchResidentialAdDto searchCommercialAdDto;
                if (premisesResidentialTypes.contains(ResidentialType.GARAGE)) {
                    int areaMax = Integer.parseInt(delegateExecution.getVariable("maxResidentialGarageArea").toString());

                    int areaMin = Integer.parseInt(delegateExecution.getVariable("minResidentialGarageArea").toString());

                    searchCommercialAdDto = new SearchResidentialAdDto(adType, address, priceMin, priceMax, premisesResidentialTypes, 1, areaMin, areaMax);
                } else {
                    int areaMax = Integer.parseInt(delegateExecution.getVariable("maxResidentialArea").toString());

                    int areaMin = Integer.parseInt(delegateExecution.getVariable("minResidentialArea").toString());

                    int amountOfRooms = Integer.parseInt(delegateExecution.getVariable("amountOfRoomsResidential").toString());

                    searchCommercialAdDto = new SearchResidentialAdDto(adType, address, priceMin, priceMax, premisesResidentialTypes, amountOfRooms, areaMin, areaMax);
                }
                ArrayList<AdResidentialDto> residentialAds = (ArrayList<AdResidentialDto>) adResidentialService.searchResidentialAds(searchCommercialAdDto);
                delegateExecution.setVariable("result", residentialAds.toString());
                log.info("Found residential ads: " + residentialAds);
            }
            log.info("Ads were successfully found");
        } catch (Throwable throwable) {
            throw new BpmnError("search-ads-error", throwable.getMessage());
        }
    }
}
