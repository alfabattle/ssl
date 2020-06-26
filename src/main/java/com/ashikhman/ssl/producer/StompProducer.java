package com.ashikhman.ssl.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.integration.stomp.StompSessionManager;
import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
public class StompProducer {

    private final StompSessionManager sessionManager;

    @Nullable
    private StompSession session;

//    @PostConstruct
    public void init() {
        sessionManager.connect(new StompSessionHandlerAdapter() {
            @Override
            public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                StompProducer.this.session = session;
            }
        });
    }

    public void send(String destination, Object payload) {
        if (null == session) {
            throw new RuntimeException("Stomp session has not been initialized.");
        }

        session.send(destination, payload);
    }
}
