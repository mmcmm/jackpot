package com.ninjaskins.website.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ninjaskins.website.config.DomainConstants;
import com.ninjaskins.website.domain.Jackpot;
import com.ninjaskins.website.domain.JackpotDeposit;
import com.ninjaskins.website.domain.User;
import com.ninjaskins.website.domain.enumeration.JackpotActivityEvents;
import com.ninjaskins.website.domain.util.DomainUtils;
import com.ninjaskins.website.repository.JackpotDepositRepository;
import com.ninjaskins.website.repository.JackpotRepository;
import com.ninjaskins.website.repository.UserRepository;
import com.ninjaskins.website.security.SecurityUtils;
import com.ninjaskins.website.service.JackpotRoundService;
import com.ninjaskins.website.service.dto.CurrentJackpotDTO;
import com.ninjaskins.website.service.dto.JackpotDepositDTO;
import com.ninjaskins.website.service.dto.JackpotRoundDepositDTO;
import com.ninjaskins.website.web.websocket.event.JackpotActivityEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing JackpotDeposit.
 */
@RestController
@RequestMapping("/api")
public class JackpotDepositController {

    private final Logger log = LoggerFactory.getLogger(JackpotDepositController.class);

    @Inject
    private JackpotDepositRepository jackpotDepositRepository;

    @Inject
    private JackpotRoundService jackpotRoundService;

    @Inject
    private UserRepository userRepository;

    @Inject
    private JackpotRepository jackpotRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    /**
     * POST  /jackpot-deposits : Create a new jackpotDeposit.
     *
     * @param jackpotDepositDTO the JackpotDepositDTO amount
     * @return the ResponseEntity with status 201 (Created) and with body the new jackpotDeposit, or with status 400 (Bad Request) if the jackpotDeposit has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/jackpot-deposit",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<JackpotDeposit> createJackpotDeposit(@Valid @RequestBody JackpotDepositDTO jackpotDepositDTO) throws URISyntaxException {
        log.debug("REST request to save a JackpotDeposit : {}", jackpotDepositDTO);
        Optional<User> currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        if (currentUser.isPresent() && DomainUtils.userCanDeposit(currentUser.get())) {
            Optional<Jackpot> currentJackpot = jackpotRepository.findFirstByOrderByIdDesc();
            if (currentJackpot.isPresent() // some safety checks
                && DomainUtils.safeToJackpotDeposit(currentJackpot.get(), jackpotDepositDTO.getAmount(), currentUser.get().getCredits())) {
                int depositsCount = jackpotDepositRepository.countByJackpotIsCurrentJackpot(currentJackpot.get().getId());
                // check to make sure no extra deposits are handled, but we need to block on fronted too
                if (depositsCount >= DomainConstants.JACKPOT_MIN_DEPOSITS_NR) {
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
                try {
                    JackpotDeposit jackpotDeposit = jackpotRoundService.jackpotDeposit(new JackpotDeposit(jackpotDepositDTO.getAmount(), currentUser.get(), currentJackpot.get()));
                    applicationEventPublisher.publishEvent(new JackpotActivityEvent(new JackpotRoundDepositDTO(jackpotDeposit.getAmount(), jackpotDeposit.getUser().getLogin()), JackpotActivityEvents.JACKPOT_DEPOSIT));
                    // we draw our winner if we need to
                    if (depositsCount >= (DomainConstants.JACKPOT_MIN_DEPOSITS_NR - 1)) { // we query the nr before adding
                        Jackpot currentJackpotWithWinner = jackpotRoundService.selectWinner(currentJackpot.get());
                        CurrentJackpotDTO currentJackpotDTO = new CurrentJackpotDTO(currentJackpotWithWinner.getHash());
                        currentJackpotDTO.setWinner(currentJackpotWithWinner.getWinner().getLogin());
                        applicationEventPublisher.publishEvent(new JackpotActivityEvent(currentJackpotDTO, JackpotActivityEvents.JACKPOT_WINNER));
                        // we wait some more and we add the next round
                        DomainUtils.threadSleep(DomainConstants.JACKPOT_DELAY_AFTER_WINNER);
                        jackpotRoundService.addTheNextJackpot();
                    }
                    // everything is fine if we get here
                    return new ResponseEntity<>(HttpStatus.OK);
                }catch (NullPointerException ex){
                    log.error(ex.getMessage(), ex);
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * GET  /jackpot-deposits : get all the jackpotDeposits.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of jackpotDeposits in body
     */
    @RequestMapping(value = "/jackpot-deposits",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<JackpotRoundDepositDTO> getAllJackpotDeposits() {
        log.debug("REST request to get all JackpotDeposits");
        Optional<Jackpot> currentJackpot = jackpotRepository.findFirstByOrderByIdDesc();
        if (currentJackpot.isPresent()) {
            List<JackpotDeposit> jackpotDeposits = jackpotDepositRepository.findByJackpotIsCurrentJackpot(currentJackpot.get().getId());
            List<JackpotRoundDepositDTO> jackpotRoundDepositDTOs = new ArrayList<>();
            for (JackpotDeposit jackpotDeposit : jackpotDeposits) {
                if (!Objects.equals(jackpotDeposit.getJackpot().getId(), currentJackpot.get().getId())) continue;
                jackpotRoundDepositDTOs.add(new JackpotRoundDepositDTO(jackpotDeposit.getAmount(), jackpotDeposit.getUser().getLogin()));
            }
            return jackpotRoundDepositDTOs;
        }
        return null;
    }

    /**
     * GET  /jackpot : get the current jackpot data.
     *
     * @return the ResponseEntity with status 200 (OK) and the current jackpot data in body
     */
    @RequestMapping(value = "/jackpot",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public CurrentJackpotDTO getCurrentJackpot() {
        log.debug("REST request to get the current Jackpot");
        Optional<Jackpot> currentJackpot = jackpotRepository.findFirstByOrderByIdDesc();
        if (currentJackpot.isPresent()) {
            Jackpot jackpot = currentJackpot.get();
            CurrentJackpotDTO currentJackpotDTO = new CurrentJackpotDTO(jackpot.getHash());
            if (jackpot.getWinner() != null) {
                currentJackpotDTO.setWinner(jackpot.getWinner().getLogin());
            }
            return currentJackpotDTO;
        }
        return null;
    }
}
