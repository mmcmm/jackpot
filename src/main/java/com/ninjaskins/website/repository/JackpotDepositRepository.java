package com.ninjaskins.website.repository;

import com.ninjaskins.website.domain.JackpotDeposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Spring Data JPA repository for the JackpotDeposit entity.
 */
@SuppressWarnings("unused")
public interface JackpotDepositRepository extends JpaRepository<JackpotDeposit,Long> {

    @Query("select jackpotDeposit from JackpotDeposit jackpotDeposit where jackpotDeposit.user.login = ?#{principal.username}")
    List<JackpotDeposit> findByUserIsCurrentUser();

}
