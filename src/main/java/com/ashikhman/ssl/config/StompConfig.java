package com.ashikhman.ssl.config;

import com.ashikhman.ssl.model.TopicItem;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.stomp.StompSessionManager;
import org.springframework.integration.stomp.WebSocketStompSessionManager;
import org.springframework.integration.stomp.inbound.StompInboundChannelAdapter;
import org.springframework.integration.stomp.outbound.StompMessageHandler;
import org.springframework.integration.support.converter.PassThruMessageConverter;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.ReactorNettyTcpStompClient;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

/**
 * Stomp beans.
 */
@Configuration
@RequiredArgsConstructor
public class StompConfig {

    private final StompProperties properties;

    @Bean
    public StompSessionManager stompSessionManager(WebSocketStompClient client) {
        StompHeaders headers = new StompHeaders();
        if (!StringUtils.isEmpty(properties.getUsername())) {
            headers.add("login", properties.getUsername());
        }
        if (!StringUtils.isEmpty(properties.getPassword())) {
            headers.add("passcode", properties.getPassword());
        }

        var manager = new WebSocketStompSessionManager(client, properties.getUrl());
        manager.setConnectHeaders(headers);

        return manager;
    }

    @Bean
    public WebSocketStompClient webSocketStompClient() {
        WebSocketClient webSocketClient = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
        stompClient.setTaskScheduler(new ConcurrentTaskScheduler());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        return stompClient;
    }

    @Bean
    @Primary
    public PollableChannel stompInputChannel() {
        return new QueueChannel();
    }

    @Bean
    public StompInboundChannelAdapter stompInboundChannelAdapter(StompSessionManager manager) {
        StompInboundChannelAdapter adapter =
                new StompInboundChannelAdapter(manager, "/topic/example");
        adapter.setOutputChannel(stompInputChannel());
        adapter.setPayloadType(TopicItem.class);

        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "stompOutputChannel")
    public MessageHandler stompMessageHandler(StompSessionManager manager) {
        StompMessageHandler handler = new StompMessageHandler(manager);
        handler.setDestination("/topic/example");
        return handler;
    }
}
