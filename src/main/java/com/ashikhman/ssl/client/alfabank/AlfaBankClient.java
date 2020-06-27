package com.ashikhman.ssl.client.alfabank;

import com.ashikhman.ssl.client.alfabank.model.Atms;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.lang.Nullable;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

@RequiredArgsConstructor
public class AlfaBankClient {

    private final WebClient webClient;

    private final ObjectMapper objectMapper;

    @Nullable
    private Atms atms;

    synchronized public Atms getAtms() {
//        if (null == atms) {
//            atms = webClient
//                    .get()
//                    .uri("/atms")
//                    .accept(MediaType.APPLICATION_JSON)
//                    .retrieve()
//                    .bodyToMono(Atms.class)
//                    .block();
//        }
//
//        return atms;

        try {
            var atmsFile = new ClassPathResource("atms.json").getFile();

            return objectMapper.readValue(atmsFile, Atms.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse atms json file", e);
        }
    }
}
