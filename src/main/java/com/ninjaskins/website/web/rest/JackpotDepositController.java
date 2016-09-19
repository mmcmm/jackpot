package com.ninjaskins.website.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ninjaskins.website.domain.JackpotDeposit;
import com.ninjaskins.website.repository.JackpotDepositRepository;
import com.ninjaskins.website.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * REST controller for managing JackpotDeposit.
 */
@RestController
@RequestMapping("/api")
public class JackpotDepositController {

    private final Logger log = LoggerFactory.getLogger(JackpotDepositController.class);

    @Inject
    private JackpotDepositRepository jackpotDepositRepository;



}
