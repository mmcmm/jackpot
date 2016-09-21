package com.ninjaskins.website.service.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for getting all the jackpot deposits for the current jackpot.
 */
public class JackpotRoundDepositDTO implements Serializable {

    @NotNull
    @Min(value = 10)
    private int amount;

    @NotNull
    private String user;

    public JackpotRoundDepositDTO() {
    }

    public JackpotRoundDepositDTO(int amount, String user) {
        this.amount = amount;
        this.user = user;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        JackpotRoundDepositDTO JackpotRoundDepositDTO = (JackpotRoundDepositDTO) o;

        if (!Objects.equals(amount, JackpotRoundDepositDTO.amount)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(amount);
    }

    @Override
    public String toString() {
        return "JackpotRoundDepositDTO{" +
            "amount='" + amount + "'" +
            "user='" + user + "'" +
            '}';
    }
}
