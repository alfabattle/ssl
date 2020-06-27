package com.ashikhman.ssl.dto;

import lombok.Data;

@Data
public class AtmDto {

    private Long deviceId;

    private String latitude;

    private String longitude;

    private String city;

    private String location;

    private boolean payments = false;
}
