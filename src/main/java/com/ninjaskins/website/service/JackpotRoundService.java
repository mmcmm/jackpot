package com.ninjaskins.website.service;

import com.ninjaskins.website.domain.Jackpot;
import com.ninjaskins.website.domain.JackpotDeposit;
import com.ninjaskins.website.repository.JackpotDepositRepository;
import com.ninjaskins.website.repository.JackpotRepository;
import com.ninjaskins.website.service.dto.JackpotRoundDepositDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class JackpotRoundService {

    private final Logger log = LoggerFactory.getLogger(JackpotRoundService.class);

    @Inject
    private JackpotDepositRepository jackpotDepositRepository;

    @Inject
    private JackpotRepository jackpotRepository;

    public boolean jackpotDeposit(JackpotDeposit jackpotDeposit) {
        log.debug("Depositing into the current jackpot ", jackpotDeposit);
        if (jackpotDeposit.getAmount() <= jackpotDepositRepository.getCurrentUserCreditBalance()) {
            jackpotDepositRepository.takeFromCurrentUserCreditBalance(jackpotDeposit.getAmount());
            JackpotDeposit result = jackpotDepositRepository.save(jackpotDeposit);
            return (result.getId() != null);
        }
        return false;
    }


    /**
     * Older jackpots automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     * </p>
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeOlderJackpotDeposits() {
        ZonedDateTime now = ZonedDateTime.now();
        List<JackpotDeposit> jackpotDeposits = jackpotDepositRepository.findAllByCreatedDateBefore(now.minusDays(3));
        for (JackpotDeposit jackpotDeposit : jackpotDeposits) {
            if(jackpotDeposit.getJackpot().getWinner() == null) continue;
            log.debug("Deleting older jackpot deposits {}", jackpotDeposit.getId());
            jackpotDepositRepository.delete(jackpotDeposit);
        }
    }

    /**
     * Older jackpot deposits automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     * </p>
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeOlderJackpots() {
        ZonedDateTime now = ZonedDateTime.now();
        List<Jackpot> jackpots = jackpotRepository.findAllByCreatedDateBefore(now.minusDays(3));
        for (Jackpot jackpot : jackpots) {
            log.debug("Deleting older jackpot {}", jackpot.getId());
            if(jackpot.getWinner() == null) continue;
            jackpotRepository.delete(jackpot);
        }
    }
}
