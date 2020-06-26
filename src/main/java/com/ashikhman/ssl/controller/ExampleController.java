package com.ashikhman.ssl.controller;

import com.ashikhman.ssl.model.TopicItem;
import com.ashikhman.ssl.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.integration.stomp.outbound.StompMessageHandler;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExampleController {

    private final ItemService itemService;

    private final PollableChannel channel;

    @GetMapping("/test")
    public String test() throws InterruptedException {

        return itemService.get().getName();
    }

    @GetMapping("/aga")
    public String aga() {
        return channel.receive().getPayload().toString();
    }

    @GetMapping("/publish")
    public void publish() {
        channel.send(new GenericMessage<>(new TopicItem().setName("BLAAA")));
    }
}
