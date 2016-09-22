package com.ninjaskins.website.web.websocket.dto;

/**
 * DTO for updating users with the Jackpot activity
 */
public class JackpotActivityDTO {

    private String aa = "aaaz";

    @Override
    public String toString() {
        return "JackpotActivityDTO{" +
            "sessionId='" + aa + '\'' +
            '}';
    }

    public String getAa() {
        return aa;
    }

    public void setAa(String aa) {
        this.aa = aa;
    }
}
