package com.ninjaskins.website.service.dto;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * A DTO for the CreditDeposit entity.
 */
public class JackpotDepositDTO {

    @NotNull
    private Integer amount;

    public Integer getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        JackpotDepositDTO jackpotDepositDTO = (JackpotDepositDTO) o;

        if ( ! Objects.equals(amount, jackpotDepositDTO.amount)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(amount);
    }

    @Override
    public String toString() {
        return "CreditDepositDTO{" +
            "amount='" + amount + "'" +
            '}';
    }
}
