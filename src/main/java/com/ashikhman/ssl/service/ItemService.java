package com.ashikhman.ssl.service;

import com.ashikhman.ssl.model.TopicItem;
import com.ashikhman.ssl.producer.StompProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Collects topic items.
 */
@Service
@RequiredArgsConstructor
public class ItemService {

    private final StompProducer producer;

    private final LinkedBlockingQueue<TopicItem> queue = new LinkedBlockingQueue<>();

    public void addItem(TopicItem item) throws InterruptedException {
        queue.put(item);
    }

    public TopicItem get() throws InterruptedException {
        producer.send("/", new TopicItem().setName("some name"));

        return queue.take();
    }
}
