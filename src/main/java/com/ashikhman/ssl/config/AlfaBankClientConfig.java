package com.ashikhman.ssl.config;

import com.ashikhman.ssl.client.alfabank.AlfaBankClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.ssl.SslContextBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.io.File;

@Configuration
@RequiredArgsConstructor
public class AlfaBankClientConfig {

    private static final int MAX_IN_MEMORY_SIZE = 1024 * 1024 * 64;

    private final ObjectMapper objectMapper;

    private final AlfaBankProperties properties;

    @Bean
    public AlfaBankClient alfaBankClient() {
        var privateKeyFile = new File(properties.getPrivateKeyFile());
        var certFile = new File(properties.getCertFile());
        var trustFile = new File(properties.getTrustFile());
        var sslContext = SslContextBuilder
                .forClient()
                .keyManager(certFile, privateKeyFile, null)
                .trustManager(trustFile);

        var httpClient = HttpClient.create().secure(sslContextSpec -> sslContextSpec.sslContext(sslContext));

        var webClient = WebClient.builder()
                .baseUrl("https://apiws.alfabank.ru/alfabank/alfadevportal/atm-service")
                .defaultHeader("x-ibm-client-id", "")
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer
                                .defaultCodecs()
                                .maxInMemorySize(MAX_IN_MEMORY_SIZE))
                        .build())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();

        return new AlfaBankClient(webClient, objectMapper);
    }
}
