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
     * Connection username.
     */
    private String username;

    /**
     * Connection password.
     */
    private String password;
}
