package com.ashikhman.ssl.client.alfabank;

import com.ashikhman.ssl.client.alfabank.model.Atms;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

@RequiredArgsConstructor
public class AlfaBankClient {

    private final WebClient webClient;

    private final ObjectMapper objectMapper;

    public Atms getAtms() {
//        return webClient
//                .get()
//                .uri("https://apiws.alfabank.ru/alfabank/alfadevportal/atm-service/atms")
//                .accept(MediaType.APPLICATION_JSON)
//                .retrieve()
//                .bodyToMono(Atms.class)
//                .block();

        try {
            var atmsFile = new ClassPathResource("atms.json").getFile();

            return objectMapper.readValue(atmsFile, Atms.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse atms json file", e);
        }
    }
}
