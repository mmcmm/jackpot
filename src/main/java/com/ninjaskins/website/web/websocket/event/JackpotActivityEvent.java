package com.ninjaskins.website.web.websocket.event;

import com.ninjaskins.website.domain.enumeration.JackpotActivityEvents;
import org.springframework.context.ApplicationEvent;

public class JackpotActivityEvent extends ApplicationEvent {

    private JackpotActivityEvents type;

    public JackpotActivityEvent(Object source, JackpotActivityEvents type) {
        super(source);
        this.type = type;
    }

    public JackpotActivityEvents getType() {
        return type;
    }
}
