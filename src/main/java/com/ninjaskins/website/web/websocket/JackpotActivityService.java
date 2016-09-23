package com.ninjaskins.website.web.websocket;

import com.ninjaskins.website.web.websocket.dto.JackpotActivityDTO;
import com.ninjaskins.website.web.websocket.event.JackpotActivityEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

@Controller
public class JackpotActivityService implements ApplicationListener<JackpotActivityEvent>{

    private static final Logger log = LoggerFactory.getLogger(JackpotActivityService.class);

    @Inject
    SimpMessageSendingOperations messagingTemplate;

    @Override
    @Async
    public void onApplicationEvent(JackpotActivityEvent event) {
        JackpotActivityDTO jackpotActivityDTO = new JackpotActivityDTO(event.getType(), event.getSource());
        log.debug("Sending user jackpot activity data {}", jackpotActivityDTO);

        messagingTemplate.convertAndSend("/topic/jackpot", jackpotActivityDTO);
    }
}
