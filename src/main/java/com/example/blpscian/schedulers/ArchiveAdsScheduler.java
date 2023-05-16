package com.example.blpscian.schedulers;

import com.example.blpscian.model.Ad;
import com.example.blpscian.repositories.AdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@EnableScheduling
@Service
public class ArchiveAdsScheduler {
    private final AdRepository<Ad> adRepository;

    @Autowired
    public ArchiveAdsScheduler(AdRepository<Ad> adRepository) {
        this.adRepository = adRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void archiveAds() {
        LocalDateTime publishedAtDate = LocalDateTime.now().minus(30, ChronoUnit.DAYS);
        List<Ad> expiredAds = adRepository.findByPublishedAtIsLessThanEqual(publishedAtDate);
        expiredAds.forEach(ad -> ad.setArchived(true));
        adRepository.saveAll(expiredAds);
    }
}
