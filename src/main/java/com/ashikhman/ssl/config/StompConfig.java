package com.ashikhman.ssl.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.*;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.ArrayList;

/**
 * Stomp beans.
 */
@Configuration
@RequiredArgsConstructor
public class StompConfig {

    private final StompProperties properties;

    @Bean
    public WebSocketStompClient webSocketStompClient() {
        var converters = new ArrayList<MessageConverter>();
        converters.add(new ByteArrayMessageConverter());
        converters.add(new StringMessageConverter());
        converters.add(new MappingJackson2MessageConverter());


        WebSocketClient webSocketClient = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
        stompClient.setTaskScheduler(new ConcurrentTaskScheduler());
        stompClient.setMessageConverter(new CompositeMessageConverter(converters));

        return stompClient;
    }

//    @Bean
//    public StompSessionManager stompSessionManager(WebSocketStompClient client) {
//        StompHeaders headers = new StompHeaders();
//        if (!StringUtils.isEmpty(properties.getUsername())) {
//            headers.add("login", properties.getUsername());
//        }
//        if (!StringUtils.isEmpty(properties.getPassword())) {
//            headers.add("passcode", properties.getPassword());
//        }
//
//        var manager = new WebSocketStompSessionManager(client, properties.getUrl());
//        manager.setConnectHeaders(headers);
//        manager.setAutoStartup(true);
//
//        return manager;
//    }

//    @Bean
//    @Qualifier("input")
//    public PollableChannel stompInputChannel() {
//        return new QueueChannel();
//    }
//
//    @Bean
//    public StompInboundChannelAdapter stompInputAdapter(StompSessionManager manager) {
//        StompInboundChannelAdapter adapter = new StompInboundChannelAdapter(manager, "/topic/alfik");
//        adapter.setOutputChannel(stompInputChannel());
//        adapter.setPayloadType(DeviceDto.class);
//
//        return adapter;
//    }
//
//    @Bean
//    @Qualifier("output")
//    public PollableChannel stompOutputChannel() {
//        return new QueueChannel();
//    }
//
//    @Bean
//    public StompInboundChannelAdapter stompOutputAdapter(StompSessionManager manager) {
//        StompInboundChannelAdapter adapter = new StompInboundChannelAdapter(manager, "/");
//        adapter.setOutputChannel(stompOutputChannel());
//        adapter.setPayloadType(DeviceDto.class);
//
//        return adapter;
//    }

//    @Bean
//    @ServiceActivator(inputChannel = "stompOutputChannel")
//    public MessageHandler stompMessageHandler(StompSessionManager manager) {
//        StompMessageHandler handler = new StompMessageHandler(manager);
//        handler.setDestination("/topic/example");
//        return handler;
//    }
}
