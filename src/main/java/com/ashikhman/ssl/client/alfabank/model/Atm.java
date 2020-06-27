package com.ashikhman.ssl.client.alfabank.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Atm {
    private Long deviceId;

    private Address address;

    private String addressComments;

    private List<String> availablePaymentSystems;

    private List<String> cashInCurrencies;

    private List<String> cashOutCurrencies;

    private Coordinates coordinates;

    private String nfc;

    private String publicAccess;

    private Date recordUpdated;

    private Services services;

    private SupportInfo supportInfo;

    private TimeAccess timeAccess;

    private Long timeShift;

    public int getPaymentsCount() {
        if (null == availablePaymentSystems) {
            return 0;
        }

        return availablePaymentSystems.size();
    }

    public boolean hasPayments() {
        if (null == services) {
            return false;
        }

        return "Y".equalsIgnoreCase(services.getPayments());
    }
}
