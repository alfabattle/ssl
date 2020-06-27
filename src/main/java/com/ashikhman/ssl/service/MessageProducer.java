package com.ashikhman.ssl.service;

import com.ashikhman.ssl.client.alfabank.AlfaBankClient;
import com.ashikhman.ssl.config.StompProperties;
import com.ashikhman.ssl.dto.DeviceDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import javax.annotation.PostConstruct;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Log4j2
public class MessageProducer {

    private final AlfaBankClient client;
    private final WebSocketStompClient stompClient;
    private final StompProperties properties;
    private StompSession session;
    private static HashMap<Long, Long> devices = new HashMap<>();

    @PostConstruct
    public void init() {
        StompHeaders headers = new StompHeaders();
        if (!StringUtils.isEmpty(properties.getUsername())) {
            headers.add("login", properties.getUsername());
        }
        if (!StringUtils.isEmpty(properties.getPassword())) {
            headers.add("passcode", properties.getPassword());
        }

        stompClient.connect(properties.getUrl(), (WebSocketHttpHeaders) null, headers, new StompSessionHandlerAdapter() {
            @Override
            public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                MessageProducer.this.session = session;

                log.error("Connected to the broker");


                session.subscribe("/topic/alfik", new StompSessionHandlerAdapter() {
                    @Override
                    public void handleFrame(StompHeaders headers, Object payload) {
                        var device = (DeviceDto) payload;

                        log.error("Received aflik {} for device {}", device.getAlfik(), device.getDeviceId());

                        devices.put(device.getDeviceId(), device.getAlfik());

                        super.handleFrame(headers, payload);
                    }

                    @Override
                    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                        log.error("Subscribed");
                    }

                    @Override
                    public Type getPayloadType(StompHeaders headers) {
                        return DeviceDto.class;
                    }

                    /**
                     * This implementation is empty.
                     */
                    @Override
                    public void handleException(StompSession session, @Nullable StompCommand command,
                                                StompHeaders headers, byte[] payload, Throwable exception) {

                        log.error("OLOLO");
                    }

                    /**
                     * This implementation is empty.
                     */
                    @Override
                    public void handleTransportError(StompSession session, Throwable exception) {
                        log.error("OLOLO");
                    }
                });

                client.getAtms().getData().getAtms().forEach(atm -> {
                    send("/", new DeviceDto().setDeviceId(atm.getDeviceId()));
                });
            }

            /**
             * This implementation is empty.
             */
            @Override
            public void handleException(StompSession session, @Nullable StompCommand command,
                                        StompHeaders headers, byte[] payload, Throwable exception) {

                log.error("OLOLO");
            }

            /**
             * This implementation is empty.
             */
            @Override
            public void handleTransportError(StompSession session, Throwable exception) {
                log.error("OLOLO");
            }
        });
    }

    synchronized public void send(String destination, Object payload) {
        //log.error("Sending {}", payload);
        session.send(destination, payload);
    }

    public long getAflik(long deviceId) {
        return devices.containsKey(deviceId) ? devices.get(deviceId) : 0;
    }
}
