package com.ninjaskins.website.service;

import com.ninjaskins.website.config.DomainConstants;
import com.ninjaskins.website.domain.Jackpot;
import com.ninjaskins.website.domain.JackpotDeposit;
import com.ninjaskins.website.domain.User;
import com.ninjaskins.website.domain.util.DomainUtils;
import com.ninjaskins.website.repository.JackpotDepositRepository;
import com.ninjaskins.website.repository.JackpotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;

@Service
@Transactional
public class JackpotRoundService {

    private final Logger log = LoggerFactory.getLogger(JackpotRoundService.class);

    @Inject
    private JackpotDepositRepository jackpotDepositRepository;

    @Inject
    private JackpotRepository jackpotRepository;

    public JackpotDeposit jackpotDeposit(JackpotDeposit jackpotDeposit) {
        log.debug("Depositing into the current jackpot ", jackpotDeposit);
        if (jackpotDeposit.getAmount() <= jackpotDepositRepository.getCurrentUserCreditBalance()) {
            jackpotDepositRepository.takeFromCurrentUserCreditBalance(jackpotDeposit.getAmount());
            return jackpotDepositRepository.save(jackpotDeposit);
        }
        return null;
    }

    public Jackpot selectWinner(Jackpot currentJackpot) {
        try {
            List<JackpotDeposit> jackpotDeposits = jackpotDepositRepository.findByJackpotIsCurrentJackpot(currentJackpot.getId());
            Future<User> winner = getWinnerUser(jackpotDeposits, currentJackpot.getRandomNumber());
            DomainUtils.threadSleep(DomainConstants.JACKPOT_DELAY_SPIN_WINNER);
            assert winner != null;
            while (!winner.isDone()) Thread.sleep(10);
            currentJackpot.setWinner(winner.get());
            jackpotRepository.addJackpotPrizeToWinnerUserCreditBalance(DomainUtils.CalculateJackpotWinning(jackpotDeposits), winner.get().getLogin());
            return jackpotRepository.save(currentJackpot);
        } catch (Exception ex) {
            log.error("Jackpot winner select error!", ex.getMessage());
            return null;
        }
    }

    @Async
    private Future<User> getWinnerUser(List<JackpotDeposit> jackpotDeposits, double currentJackpotRandomNr) {
        int nrOfTicketsNeeded = 0;  // we calculate total tickets
        for (JackpotDeposit jackpotDeposit : jackpotDeposits) {
            nrOfTicketsNeeded += jackpotDeposit.getAmount();
        }
        Collections.shuffle(jackpotDeposits); // we try to make it more random
        long[] tickets = new long[nrOfTicketsNeeded]; // we fill up the tickets
        int ticketNr = 0;
        for (JackpotDeposit jackpotDeposit : jackpotDeposits) {
            for (int i = 0; i < jackpotDeposit.getAmount(); i++) {
                tickets[ticketNr] = jackpotDeposit.getUser().getId();
                ticketNr++;
            }
        }
        int winningTicket = (int) (currentJackpotRandomNr * nrOfTicketsNeeded);
        long winningUserId = tickets[winningTicket];
        for (JackpotDeposit jackpotDeposit : jackpotDeposits) {
            if (jackpotDeposit.getUser().getId() == winningUserId) {
                return new AsyncResult<>(jackpotDeposit.getUser());
            }
        }
        return null;
    }

    public Jackpot addTheNextJackpot() {
        double randomNumber = DomainUtils.getSecureRandomNumber();
        Jackpot jackpot = new Jackpot();
        jackpot.setHash(DomainUtils.getSHA256Hash(String.valueOf(randomNumber)));
        jackpot.setRandomNumber(randomNumber);
        return jackpotRepository.save(jackpot);
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
            if (jackpotDeposit.getJackpot().getWinner() == null) continue;
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
            if (jackpot.getWinner() == null) continue;
            jackpotRepository.delete(jackpot);
        }
    }
}
