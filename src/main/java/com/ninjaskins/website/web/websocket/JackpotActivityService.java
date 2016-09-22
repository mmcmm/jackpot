package com.ninjaskins.website.web.websocket;

import com.ninjaskins.website.web.websocket.dto.JackpotActivityDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import javax.inject.Inject;

@Controller
public class JackpotActivityService {

    private static final Logger log = LoggerFactory.getLogger(JackpotActivityService.class);

    @Inject
    SimpMessageSendingOperations messagingTemplate;

    @SendTo("/topic/jackpot")
    public JackpotActivityDTO sendJackpotActivity() {
        JackpotActivityDTO jackpotActivityDTO = new JackpotActivityDTO();

        log.debug("Sending user jackpot activity data {}", jackpotActivityDTO);
        return jackpotActivityDTO;
    }

}
