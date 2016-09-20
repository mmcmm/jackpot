package com.ninjaskins.website.repository;

import com.ninjaskins.website.domain.JackpotDeposit;
import com.ninjaskins.website.service.dto.AllJackpotDepositsDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for the JackpotDeposit entity.
 */
@SuppressWarnings("unused")
@Transactional
public interface JackpotDepositRepository extends JpaRepository<JackpotDeposit,Long> {

    @Transactional(readOnly = true)
    @Query("select jackpotDeposit from JackpotDeposit jackpotDeposit where jackpotDeposit.user.login = ?#{principal.username}")
    List<JackpotDeposit> findByUserIsCurrentUser();

    @Transactional(readOnly = true)
    @Query("select credits from User user where login = ?#{principal.username}")
    Integer getCurrentUserCreditBalance();

    @Modifying
    @Query("update User user set credits = credits + ?1 where login = ?#{principal.username}")
    Integer addToCurrentUserCreditBalance(Integer credits);

    @Modifying
    @Query("update User user set credits = credits - ?1 where login = ?#{principal.username}")
    Integer takeFromCurrentUserCreditBalance(Integer credits);

    @Transactional(readOnly = true)
    @Query("select jackpotDeposit from JackpotDeposit jackpotDeposit where jackpotDeposit.jackpot.id = ?1")
    List<JackpotDeposit> findByJackpotIsCurrentJackpot (long currentJackpot);

    @Transactional(readOnly = true)
    List<JackpotDeposit> findAllByCreatedDateBefore (ZonedDateTime dateTime);

}
