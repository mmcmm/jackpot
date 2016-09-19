package com.ninjaskins.website.repository;

import com.ninjaskins.website.domain.CreditDeposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Spring Data JPA repository for the CreditDeposit entity.
 */
@SuppressWarnings("unused")
public interface CreditDepositRepository extends JpaRepository<CreditDeposit,Long> {

    @Query("select creditDeposit from CreditDeposit creditDeposit where creditDeposit.user.login = ?#{principal.username}")
    List<CreditDeposit> findByUserIsCurrentUser();

    @Modifying
    @Query("update User user set credits = credits + ?1 where login = ?#{principal.username}")
    int updateCurrentUserCreditBalance(Integer credits);
}
