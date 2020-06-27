package com.ashikhman.ssl.client.alfabank.model;

import lombok.Data;

@Data
public class Atms {
    private AtmsData data;

    private Error error;

    private Boolean success;

    private Long total;
}
