package com.ashikhman.ssl.listener;

import com.ashikhman.ssl.model.TopicItem;
import com.ashikhman.ssl.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;

/**
 * Stomp topic listener.
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class TopicStompListener extends AbstractStompListener {

    /**
     * Topic destination name.
     */
    public static final String NAME = "/topic/example";

    private final ItemService itemService;

    @SneakyThrows
    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        var item = (TopicItem) payload;
        log.error("New topic message received: {}", item);

        itemService.addItem(item);
    }

    @Override
    public String getTopicName() {
        return NAME;
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return TopicItem.class;
    }
}
