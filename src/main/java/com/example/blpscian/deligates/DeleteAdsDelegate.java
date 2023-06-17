package com.example.blpscian.deligates;

import com.example.blpscian.model.AdResidential;
import com.example.blpscian.services.AdService;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import javax.inject.Named;

@Component
@Named
@Slf4j
public class DeleteAdsDelegate implements JavaDelegate {
    private final AdService<AdResidential> adResidentialAdService;

    public DeleteAdsDelegate(AdService<AdResidential> adResidentialAdService) {
        this.adResidentialAdService = adResidentialAdService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        try {
            String email = delegateExecution.getVariable("email_to_delete").toString();
            int countDeletes = adResidentialAdService.deleteAdsByUser(email);
            delegateExecution.setVariable("deletes_count", countDeletes);

            log.info("Current activity is " + delegateExecution.getCurrentActivityName());
            log.info(countDeletes + "ad(s) was(were) successfully deleted");
        } catch (Throwable throwable) {
            throw new BpmnError("delete_ads_error", throwable.getMessage());
        }
    }
}
