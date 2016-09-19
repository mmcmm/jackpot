package com.ninjaskins.website.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ninjaskins.website.domain.Jackpot;
import com.ninjaskins.website.domain.JackpotDeposit;
import com.ninjaskins.website.domain.User;
import com.ninjaskins.website.domain.util.DomainUtils;
import com.ninjaskins.website.repository.JackpotDepositRepository;
import com.ninjaskins.website.repository.JackpotRepository;
import com.ninjaskins.website.repository.UserRepository;
import com.ninjaskins.website.security.SecurityUtils;
import com.ninjaskins.website.service.JackpotRoundService;
import com.ninjaskins.website.service.dto.JackpotDepositDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final int MIN_DEPOSIT = 10;

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
            if (currentJackpot.isPresent() && DomainUtils.safeToJackpotDeposit(currentJackpot.get(), jackpotDepositDTO.getAmount(), MIN_DEPOSIT)) {
                // create our jackpot deposit
                return jackpotRoundService.jackpotDeposit(new JackpotDeposit(jackpotDepositDTO.getAmount(), currentUser.get(), currentJackpot.get()))
                    ? new ResponseEntity<>(HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
