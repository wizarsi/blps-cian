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

import javax.inject.Named;
import java.util.ArrayList;
import java.util.Arrays;
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
    public void execute(DelegateExecution delegateExecution){
        try {
            String realEstateType = delegateExecution.getVariable("realEstateType").toString();
            log.info("1: " + realEstateType);
            AdType adType = AdType.valueOf( delegateExecution.getVariable("adType").toString());
            log.info("2: " + adType);
            log.info("3: " + delegateExecution.getVariable("adType").toString());

            String address = delegateExecution.getVariable("address").toString();
            log.info("4: " + delegateExecution.getVariable("adType").toString());

            int priceMin = Integer.parseInt(delegateExecution.getVariable("priceMin").toString());
            log.info("5: " + priceMin);

            int priceMax = Integer.parseInt(delegateExecution.getVariable("priceMax").toString());
            log.info("6: " + priceMax);

            if (realEstateType.equals("commercial")) {
                int areaMax = Integer.parseInt(delegateExecution.getVariable("maxCommercialArea").toString());
                log.info("7: " + areaMax);

                int areaMin = Integer.parseInt(delegateExecution.getVariable("minCommercialArea").toString());
                log.info("8: " + areaMin);

                String[] checkedValues = (String[]) delegateExecution.getVariable("premisesCommercialTypes");
                Set<CommercialType> premisesCommercialTypes = Arrays.stream(checkedValues)
                        .map(CommercialType::valueOf)
                        .collect(Collectors.toSet());
                log.info("9: " + premisesCommercialTypes);

                SearchCommercialAdDto searchCommercialAdDto = new SearchCommercialAdDto(adType, address, priceMin, priceMax, premisesCommercialTypes, areaMin, areaMax);
                ArrayList<AdCommercialDto> commercialAds = (ArrayList<AdCommercialDto>) adCommercialService.searchCommercialAds(searchCommercialAdDto);
                delegateExecution.setVariable("result", commercialAds);
                log.info("Found commercial ads: " + commercialAds);
            } else {
                Set<ResidentialType> premisesResidentialTypes = delegateExecution.getVariableTyped("premisesResidentialTypes");
                log.info("10: " + premisesResidentialTypes);

                SearchResidentialAdDto searchCommercialAdDto;
                if (premisesResidentialTypes.contains(ResidentialType.GARAGE)) {
                    int areaMax = Integer.parseInt(delegateExecution.getVariable("maxResidentialGarageArea").toString());
                    log.info("11: " + areaMax);

                    int areaMin = Integer.parseInt(delegateExecution.getVariable("minResidentialGarageArea").toString());
                    log.info("12: " + areaMin);

                    searchCommercialAdDto = new SearchResidentialAdDto(adType, address, priceMin, priceMax, premisesResidentialTypes, 1, areaMin, areaMax);
                } else {
                    int areaMax = Integer.parseInt(delegateExecution.getVariable("maxResidentialArea").toString());
                    log.info("13: " + areaMax);

                    int areaMin = Integer.parseInt(delegateExecution.getVariable("minResidentialArea").toString());
                    log.info("14: " + areaMin);

                    int amountOfRooms = Integer.parseInt(delegateExecution.getVariable("amountOfRoomsResidential").toString());
                    log.info("15: " + amountOfRooms);

                    searchCommercialAdDto = new SearchResidentialAdDto(adType, address, priceMin, priceMax, premisesResidentialTypes, amountOfRooms, areaMin, areaMax);
                }
                ArrayList<AdResidentialDto> residentialAds = (ArrayList<AdResidentialDto>) adResidentialService.searchResidentialAds(searchCommercialAdDto);
                delegateExecution.setVariable("result", residentialAds);
                log.info("Found residential ads: " + residentialAds);
            }
            log.info( "Ads were successfully found");
        } catch (Throwable throwable) {
            throw new BpmnError("search-ads-error", throwable.getMessage());
        }
    }
}
