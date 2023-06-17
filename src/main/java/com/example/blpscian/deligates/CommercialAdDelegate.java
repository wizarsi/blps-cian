package com.example.blpscian.deligates;

import com.example.blpscian.model.Ad;
import com.example.blpscian.model.AdCommercial;
import com.example.blpscian.model.AdResidential;
import com.example.blpscian.model.dto.AdCommercialDto;
import com.example.blpscian.model.dto.AdResidentialDto;
import com.example.blpscian.model.enums.AdType;
import com.example.blpscian.model.enums.CommercialType;
import com.example.blpscian.model.enums.ResidentialType;
import com.example.blpscian.security.JwtUtil;
import com.example.blpscian.services.AdService;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.inject.Named;

@Component
@Named
@Slf4j
public class CommercialAdDelegate implements JavaDelegate {
    private final AdService<AdCommercial> adCommercialService;
    private final JwtUtil jwtUtil;

    @Autowired
    public CommercialAdDelegate(AdService<AdCommercial> adCommercialService, JwtUtil jwtUtil) {
        this.adCommercialService = adCommercialService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        try {
            AdType adType = (AdType) delegateExecution.getVariable("ad_type");
            String address = (String) delegateExecution.getVariable("address");
            Double area = (Double) delegateExecution.getVariable("area");
            int floor = (int) delegateExecution.getVariable("floor");
            int price = (int) delegateExecution.getVariable("price");
            String description = (String) delegateExecution.getVariable("description");
            CommercialType commercialType = (CommercialType) delegateExecution.getVariable("commercial_type");
            String email = jwtUtil.usernameFromAccessToken((String) delegateExecution.getVariable("token"));
            log.info("AAAAAAAAAAAAAAAAAAAAAA " + email);
            AdCommercialDto adCommercialDto = new AdCommercialDto(
                    adType, address, area, floor, price, description, commercialType, email
            );

            adCommercialService.addCommercialAd(adCommercialDto);
            delegateExecution.setVariable("result", "Ad was successfully created");

            log.info("Current activity is " + delegateExecution.getCurrentActivityName());
            log.info("Ad was successfully created");
        } catch (Throwable throwable) {
            throw new BpmnError("add_ad_error", throwable.getMessage());
        }
    }
}
