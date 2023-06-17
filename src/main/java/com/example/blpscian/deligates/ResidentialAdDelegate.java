package com.example.blpscian.deligates;

import com.example.blpscian.model.AdResidential;
import com.example.blpscian.model.dto.AdResidentialDto;
import com.example.blpscian.model.enums.AdType;
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
public class ResidentialAdDelegate implements JavaDelegate {
    private final AdService<AdResidential> adResidentialService;
    private final JwtUtil jwtUtil;

    @Autowired
    public ResidentialAdDelegate(AdService<AdResidential> adResidentialService, JwtUtil jwtUtil) {
        this.adResidentialService = adResidentialService;
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
            ResidentialType residentialType = (ResidentialType) delegateExecution.getVariable("residential_type");
            int amountOfRooms = (int) delegateExecution.getVariable("amount_of_rooms");
            String email = jwtUtil.usernameFromAccessToken((String) delegateExecution.getVariable("token"));
            log.info("AAAAAAAAAAAAAAAAAAAAAA " + email);
            AdResidentialDto adResidentialDto = new AdResidentialDto(
                    adType, address, area, floor, price, description, residentialType, amountOfRooms, email
            );

            adResidentialService.addResidentialAd(adResidentialDto);
            delegateExecution.setVariable("result", "Ad was successfully created");

            log.info("Current activity is " + delegateExecution.getCurrentActivityName());
            log.info("Ad was successfully created");
        } catch (Throwable throwable) {
            throw new BpmnError("add_ad_error", throwable.getMessage());
        }
    }
}
