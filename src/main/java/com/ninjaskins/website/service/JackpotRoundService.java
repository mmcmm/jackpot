package com.ninjaskins.website.service;

import com.ninjaskins.website.domain.JackpotDeposit;
import com.ninjaskins.website.repository.JackpotDepositRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

@Service
@Transactional
public class JackpotRoundService {

    private final Logger log = LoggerFactory.getLogger(JackpotRoundService.class);

    @Inject
    private JackpotDepositRepository jackpotDepositRepository;

    public boolean jackpotDeposit(JackpotDeposit jackpotDeposit) {
        log.debug("Depositing into the current jackpot ", jackpotDeposit);
        if (jackpotDeposit.getAmount() <= jackpotDepositRepository.getCurrentUserCreditBalance()) {
            jackpotDepositRepository.takeFromCurrentUserCreditBalance(jackpotDeposit.getAmount());
            JackpotDeposit result = jackpotDepositRepository.save(jackpotDeposit);
            return (result.getId() != null);
        }
        return false;
    }


}
