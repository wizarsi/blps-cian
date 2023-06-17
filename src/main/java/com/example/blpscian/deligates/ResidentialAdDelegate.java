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
            AdType adType = AdType.valueOf(delegateExecution.getVariable("ad_type").toString());
            String address = delegateExecution.getVariable("address").toString();
            Double area = Double.valueOf(delegateExecution.getVariable("area").toString());
            int floor = Integer.parseInt(delegateExecution.getVariable("floor").toString());
            int price = Integer.parseInt(delegateExecution.getVariable("price").toString());
            String description = delegateExecution.getVariable("description").toString();
            ResidentialType residentialType = ResidentialType.valueOf(delegateExecution.getVariable("residential_type").toString());
            int amountOfRooms = Integer.parseInt(delegateExecution.getVariable("amount_of_rooms").toString());
            String token = delegateExecution.getVariable("token").toString();
            String email = jwtUtil.usernameFromAccessToken(token);
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
