package com.example.blpscian.deligates;

import com.example.blpscian.schedulers.ArchiveAdsScheduler;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import javax.inject.Named;

@Component
@Named
@Slf4j
public class ArchiveAdsDelegate implements JavaDelegate {
    private final ArchiveAdsScheduler archiveAdsScheduler;

    public ArchiveAdsDelegate(ArchiveAdsScheduler archiveAdsScheduler) {
        this.archiveAdsScheduler = archiveAdsScheduler;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        try {
            archiveAdsScheduler.archiveAds();
            log.info("Ads were successfully archived");
        } catch (Throwable throwable) {
            throw new BpmnError("ads-archiver-error", throwable.getMessage());
        }
    }
}
