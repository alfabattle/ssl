package com.ashikhman.ssl.config;

import com.ashikhman.ssl.listener.AbstractStompListener;
import lombok.RequiredArgsConstructor;
import org.springframework.integration.stomp.StompSessionManager;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Subscribes all listeners on STOMP connection set up.
 */
@RequiredArgsConstructor
@Component
public class StompListenersConfig {
    private final List<AbstractStompListener> listeners;

    private final StompSessionManager sessionManager;

    /**
     * Registers all STOMP listeners.
     */
    //@PostConstruct
    public void init() {
        sessionManager.connect(new StompSessionHandlerAdapter() {
            @Override
            public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                listeners.forEach(listener -> session.subscribe(listener.getTopicName(), listener));
            }
        });
    }
}
