package com.ninjaskins.website.web.websocket.dto;

import com.ninjaskins.website.domain.enumeration.JackpotActivityEvents;

/**
 * DTO for updating users with the Jackpot activity
 */
public class JackpotActivityDTO {

    private JackpotActivityEvents type;

    private Object object;

    public JackpotActivityDTO(JackpotActivityEvents type, Object object) {
        this.type = type;
        this.object = object;
    }

    public JackpotActivityEvents getType() {
        return type;
    }

    public void setType(JackpotActivityEvents type) {
        this.type = type;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return "JackpotActivityDTO{" +
            "type='" + type + '\'' +
            '}';


    }
}
