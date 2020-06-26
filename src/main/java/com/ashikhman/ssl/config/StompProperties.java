package com.ashikhman.ssl.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * Stomp configuration properties.
 */
@ConfigurationProperties("app.stomp")
@Component
@Data
public class StompProperties {

    /**
     * Websocket url.
     */
    @NotBlank
    private String url;

    /**
     * Websocket host (e.g. 127.0.0.1).
     */
    @NotBlank
    private String host;

    /**
     * Websocket port.
     */
    @NotNull
    @Positive
    private Integer port;

    /**
     * Connection username.
     */
    private String username;

    /**
     * Connection password.
     */
    private String password;
}
