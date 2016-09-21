package com.ninjaskins.website.web.rest;

import com.ninjaskins.website.repository.JackpotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

/**
 * REST controller for managing Jackpot.
 */
@RestController
@RequestMapping("/api")
public class JackpotController {

    private final Logger log = LoggerFactory.getLogger(JackpotController.class);

    @Inject
    private JackpotRepository jackpotRepository;


}
