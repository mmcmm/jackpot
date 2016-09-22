package com.ninjaskins.website.service.dto;

import com.ninjaskins.website.config.Constants;
import com.ninjaskins.website.config.DomainConstants;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Jackpot entity.
 */
public class CurrentJackpotDTO implements Serializable {

    @NotNull
    @Size(max = 64)
    private String hash;

    @NotNull
    private int minDepositsNr = DomainConstants.JACKPOT_MIN_DEPOSITS_NR;

    @NotNull
    private float percentFee = DomainConstants.JACKPOT_PERCENT_FEE;

    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 100)
    private String winner = null;

    public CurrentJackpotDTO() {
    }

    public CurrentJackpotDTO(String hash){
        this.hash = hash;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public int getMinDepositsNr() {
        return minDepositsNr;
    }

    public void setMinDepositsNr(int minDepositsNr) {
        this.minDepositsNr = minDepositsNr;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public float getPercentFee() {
        return percentFee;
    }

    public void setPercentFee(float percentFee) {
        this.percentFee = percentFee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CurrentJackpotDTO currentJackpotDTO = (CurrentJackpotDTO) o;

        if (!Objects.equals(hash, currentJackpotDTO.hash)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(hash);
    }

    @Override
    public String toString() {
        return "CurrentJackpotDTO{" +
            "hash='" + hash + "'" +
            "winner='" + winner + "'" +
            "minDepositsNr='" + minDepositsNr + "'" +
            "percentFee='" + percentFee + "'" +
            '}';
    }
}
