package com.ashikhman.ssl.config;

import com.ashikhman.ssl.client.alfabank.AlfaBankClient;
import io.netty.handler.ssl.SslContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.io.File;

@Configuration
public class AlfaBankClientConfig {

    private static final String DIR = "/home/vadim/projects/github.com/alfabattle/ssl/var";

    @Bean
    public AlfaBankClient alfaBankClient() {
        var privateKeyFile = new File(DIR + "/apidevelopers.pem");
        var certFile = new File(DIR + "/apidevelopers.cer");
        var trustFile = new File(DIR + "/mycertfile.crt");
        var sslContext = SslContextBuilder
                .forClient()
                .keyManager(certFile, privateKeyFile, null)
                .trustManager(trustFile);

        var httpClient = HttpClient.create().secure(sslContextSpec -> sslContextSpec.sslContext(sslContext));

        var webClient = WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer
                                .defaultCodecs()
                                .maxInMemorySize(1024 * 1024 * 16))
                        .build())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();

        return new AlfaBankClient(webClient);
    }
}
