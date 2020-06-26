package com.ashikhman.ssl.client.alfabank;

import com.ashikhman.ssl.client.alfabank.model.Atms;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
public class AlfaBankClient {

    private static final String CONTENT = "{\n" +
            "  \"leads\": [\n" +
            "    {\n" +
            "      \"birthDate\": \"1990-09-15\",\n" +
            "      \"email\": \"hello@yandex.ru\",\n" +
            "      \"firstName\": \"Иван\",\n" +
            "      \"lastName\": \"Петров\",\n" +
            "      \"middleName\": \"Иванович\",\n" +
            "      \"mobilePhone\": \"9857733131\",\n" +
            "      \"passportBirthPlace\": \"гор. Москва\",\n" +
            "      \"passportIssueDate\": \"2015-09-22\",\n" +
            "      \"passportNumber\": \"985632\",\n" +
            "      \"passportOffice\": \"ОВД Петровно\",\n" +
            "      \"passportOfficeCode\": \"332-231\",\n" +
            "      \"passportSeries\": \"15 97\",\n" +
            "      \"platformId\": \"leadgiddi_cpa_id1200_cc-mastercardplatinum-30fields-aprove\",\n" +
            "      \"product\": \"visa_gold\",\n" +
            "      \"productType\": \"CC\",\n" +
            "      \"sex\": \"m\",\n" +
            "      \"lateCall\": \"True\",\n" +
            "      \"workRegionCode\": \"77\",\n" +
            "      \"workCompanyName\": \"ЗАО ТРУД\",\n" +
            "      \"workInn\": \"7728168971\",\n" +
            "      \"workPhone\": \"4950001101\",\n" +
            "      \"workPost\": \"Продавец\",\n" +
            "      \"workStatus\": \"true\",\n" +
            "      \"partnerLeadId\": \"36CBI2018110132039\",\n" +
            "      \"createdTimePartner\": \"2020-01-30 12:00:00\",\n" +
            "      \"riskStatus\": \"ACCEPT\",\n" +
            "      \"consentPartnerDate\": \"2020-01-30 12:00:00\",\n" +
            "      \"consentPartnerCode\": \"991234\",\n" +
            "      \"consentPartnerId\": \"456789546\",\n" +
            "      \"consentVersion\": \"456789546\",\n" +
            "      \"income\": \"35000\",\n" +
            "      \"education\": \"SS\",\n" +
            "      \"contactFirstName\": \"Ольга\",\n" +
            "      \"contactMobilePhone\": \"4959518521\",\n" +
            "      \"secondaryDocument\": \"INN\",\n" +
            "      \"additionalDocument\": \"TSR\",\n" +
            "      \"salaryDocument\": \"FBK\",\n" +
            "      \"fullFlag\": \"Y\",\n" +
            "      \"registrationRegionCode\": \"77\",\n" +
            "      \"confirmationType\": \"2NDFL\",\n" +
            "      \"obtainingCity\": \"5900000100000\",\n" +
            "      \"officeMnemonic\": \"MODG\",\n" +
            "      \"cardShippingType\": \"C\",\n" +
            "      \"cardPaymentSystem\": \"VISA\",\n" +
            "      \"cardType\": \"visa_classic\",\n" +
            "      \"cardCategoryLimits\": \"150000\",\n" +
            "      \"cardCategory\": \"CLASSIC\",\n" +
            "      \"gracePeriod\": \"100\",\n" +
            "      \"cardEmbossingLastName\": \"OLGA BARKOVSKAYA\",\n" +
            "      \"workStartDate\": \"2010-05-15\",\n" +
            "      \"maritalStatus\": \"M\",\n" +
            "      \"paymentDay\": \"25\",\n" +
            "      \"creditTerm\": \"15\",\n" +
            "      \"lendingAmount\": \"900000\",\n" +
            "      \"creditPurpose\": \"На машину\",\n" +
            "      \"gettingWay\": \"delivery\",\n" +
            "      \"gettingCity\": \"5400000200000\",\n" +
            "      \"pensionDocument\": \"Пенсионное удостоверение\",\n" +
            "      \"additionalIncome\": \"100000\",\n" +
            "      \"additionalIncomeType\": \"PRIV_TAXI\",\n" +
            "      \"incomePension\": \"18000\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";
    private final WebClient webClient;

    public Atms aga() {
        return webClient
                .get()
                .uri("https://apiws.alfabank.ru/alfabank/alfadevportal/atm-service/atms")
                .accept(MediaType.APPLICATION_JSON)
                .header("x-ibm-client-id", "aa2936f7-aab1-4569-98e8-55872a9d10b7")
                .retrieve()
                .bodyToMono(Atms.class)
                .block();
    }

    public String test() {
        return webClient
                .post()
                .uri("https://apiws.alfabank.ru/alfabank/alfadevportal/alfaform-extended-leads-api/save")
                .body(BodyInserters.fromValue(CONTENT))
                .accept(MediaType.APPLICATION_JSON)
                .header("Content-Type", "application/json")
                .header("x-ibm-client-id", "aa2936f7-aab1-4569-98e8-55872a9d10b7")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

}
