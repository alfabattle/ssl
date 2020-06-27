package com.ashikhman.ssl.service;

import com.ashikhman.ssl.client.alfabank.AlfaBankClient;
import com.ashikhman.ssl.dto.DeviceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.Lifecycle;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
@Service
public class AtmService {

    private final MessageProducer producer;

    private final AlfaBankClient client;

//    @PostConstruct
    public void init() {
        client.getAtms().getData().getAtms().forEach(atm -> {
            producer.send("/", new DeviceDto().setDeviceId(atm.getDeviceId()));
        });
    }
}
