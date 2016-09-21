package com.ninjaskins.website.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ninjaskins.website.domain.util.DomainUtils;
import com.ninjaskins.website.repository.CreditDepositRepository;
import com.ninjaskins.website.repository.UserRepository;
import com.ninjaskins.website.security.SecurityUtils;
import com.ninjaskins.website.service.dto.CreditDepositDTO;
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
 * REST controller for managing CreditDeposit.
 */
@RestController
@RequestMapping("/api")
public class CreditDepositController {

    private final Logger log = LoggerFactory.getLogger(CreditDepositController.class);
    private final int MIN_DEPOSIT = 100;
    @Inject
    private CreditDepositRepository creditDepositRepository;
    @Inject
    private UserRepository userRepository;

    /**
     * GET  /credit-deposits : get all the creditDeposits.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of creditDeposits in body
     */
    @RequestMapping(value = "/credit-deposit",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CreditDepositDTO> getAllCreditDeposits() {
        log.debug("REST request to get the CreditDeposit Balance");
        return userRepository
            .findOneByLogin(SecurityUtils.getCurrentUserLogin())
            .map(u -> {
                if (DomainUtils.userCanDeposit(u)) {
                    return Optional.ofNullable(u.getCredits())
                        .map(cb -> new ResponseEntity<>(new CreditDepositDTO(cb, MIN_DEPOSIT), HttpStatus.OK))
                        .orElse(new ResponseEntity<>(new CreditDepositDTO(0, MIN_DEPOSIT), HttpStatus.OK));
                }
                return new ResponseEntity<>(new CreditDepositDTO(-1, MIN_DEPOSIT), HttpStatus.OK);
            })
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * POST  /credit-deposit : Updates the credits.
     *
     * @param creditDepositDTO the creditDepositDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new creditDepositDTO, or with status 400 (Bad Request) if the creditDeposit has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/credit-deposit",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CreditDepositDTO> createCreditDeposit(@Valid @RequestBody CreditDepositDTO creditDepositDTO) throws URISyntaxException {
        log.debug("REST request to update CreditDeposit Balance: {}", creditDepositDTO);
        return userRepository
            .findOneByLogin(SecurityUtils.getCurrentUserLogin())
            .map(u -> {
                if (DomainUtils.userCanDeposit(u)) {
                    return Optional.of(creditDepositRepository.updateCurrentUserCreditBalance(creditDepositDTO.getDepositCredits()))
                        .map(cb -> new ResponseEntity<>(new CreditDepositDTO(creditDepositDTO.getCreditBalance() + creditDepositDTO.getDepositCredits(), MIN_DEPOSIT), HttpStatus.OK))
                        .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
                }
                return new ResponseEntity<>(new CreditDepositDTO(-1, MIN_DEPOSIT), HttpStatus.OK);
            })
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

}
