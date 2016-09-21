package com.ninjaskins.website.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the CreditDeposit entity.
 */
public class CreditDepositDTO implements Serializable {

    @NotNull
    private Integer creditBalance;

    @NotNull
    private Integer depositCredits;

    public CreditDepositDTO() {
    }

    public CreditDepositDTO(Integer creditBalance, Integer depositCredits) {
        this.creditBalance = creditBalance;
        this.depositCredits = depositCredits;
    }

    public Integer getCreditBalance() {
        return creditBalance;
    }

    public void setCreditBalance(Integer creditBalance) {
        this.creditBalance = creditBalance;
    }

    public Integer getDepositCredits() {
        return depositCredits;
    }

    public void setDepositCredits(Integer depositCredits) {
        this.depositCredits = depositCredits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CreditDepositDTO creditDepositDTO = (CreditDepositDTO) o;

        if (!Objects.equals(creditBalance, creditDepositDTO.creditBalance)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(creditBalance);
    }

    @Override
    public String toString() {
        return "CreditDepositDTO{" +
            "creditBalance='" + creditBalance + "'" +
            "depositCredits='" + depositCredits + "'" +
            '}';
    }
}
