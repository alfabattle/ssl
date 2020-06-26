package com.ashikhman.ssl;

import com.ashikhman.ssl.client.alfabank.AlfaBankClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ClientTest {
    @Autowired
    AlfaBankClient client;

    @Test
    void aga() {
        var result = client.aga();

        System.out.println(result);
    }
}
