package com.ninjaskins.website.repository;

import com.ninjaskins.website.domain.Jackpot;
import com.ninjaskins.website.domain.JackpotDeposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Jackpot entity.
 */
@SuppressWarnings("unused")
public interface JackpotRepository extends JpaRepository<Jackpot,Long> {

    @Query("select jackpot from Jackpot jackpot where jackpot.winner.login = ?#{principal.username}")
    List<Jackpot> findByWinnerIsCurrentUser();

    // get latest jackpot
    Optional<Jackpot> findFirstByOrderByIdDesc();

    List<Jackpot> findAllByCreatedDateBefore (ZonedDateTime dateTime);
}
