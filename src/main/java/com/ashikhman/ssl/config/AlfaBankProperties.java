package com.ashikhman.ssl.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;

@ConfigurationProperties(prefix = "app.alfa-bank")
@Component
@Data
public class AlfaBankProperties {
    
    @NotBlank
    private String privateKeyFile;

    @NotBlank
    private String certFile;

    @NotBlank
    private String trustFile;
}
