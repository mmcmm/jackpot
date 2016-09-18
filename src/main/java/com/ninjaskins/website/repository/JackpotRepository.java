package com.ninjaskins.website.repository;

import com.ninjaskins.website.domain.Jackpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Spring Data JPA repository for the Jackpot entity.
 */
@SuppressWarnings("unused")
public interface JackpotRepository extends JpaRepository<Jackpot,Long> {

    @Query("select jackpot from Jackpot jackpot where jackpot.winner.login = ?#{principal.username}")
    List<Jackpot> findByWinnerIsCurrentUser();

}
