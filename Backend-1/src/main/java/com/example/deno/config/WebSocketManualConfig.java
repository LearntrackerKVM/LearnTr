package com.example.deno.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.messaging.SubProtocolWebSocketHandler;

@Configuration
public class WebSocketManualConfig {

    @Bean
    public SimpMessagingTemplate simpMessagingTemplate(MessageChannel handler) {
        return new SimpMessagingTemplate(handler);
    }
}
