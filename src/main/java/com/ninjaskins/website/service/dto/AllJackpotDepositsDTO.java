package com.ninjaskins.website.service.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A DTO for getting all the CreditDeposits for the current jackpot.
 */
public class AllJackpotDepositsDTO implements Serializable {

    @NotNull
    @Min(value = 10)
    private int amount;

    @NotNull
    private String user;

    public AllJackpotDepositsDTO() {
    }

    public AllJackpotDepositsDTO(int amount, String user)
    {

    }
}
