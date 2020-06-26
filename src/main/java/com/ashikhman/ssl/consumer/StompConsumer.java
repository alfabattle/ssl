package com.ashikhman.ssl.consumer;

import com.ashikhman.ssl.config.StompProperties;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Stomp consumer connects to websocket service and starts listening on topics.
 */
@RequiredArgsConstructor
@Log4j2
public class StompConsumer {

    /**
     * Minimum time in seconds between connection attempts.
     */
    private static final long RETRY_INTERVAL = TimeUnit.SECONDS.toMillis(10);

    /**
     * Stomp configuration properties.
     */
    private final StompProperties properties;

    /**
     * Configured stomp client.
     */
    private final WebSocketStompClient client;

    /**
     * Time when was the last connection attempt.
     */
    private long lastConnectionAttemptAt;

    public StompSession stompSession() throws ExecutionException, InterruptedException {
        return connect();
    }

    /**
     * Connects to the broker and initializes the consumer.
     */
    private StompSession connect() throws ExecutionException, InterruptedException {
//        log.info("Connecting to `{}` with username `{}`", properties.getUrl(), properties.getUsername());

        lastConnectionAttemptAt = System.currentTimeMillis();

        StompHeaders headers = new StompHeaders();
        if (!StringUtils.isEmpty(properties.getUsername())) {
            headers.add("login", properties.getUsername());
        }
        if (!StringUtils.isEmpty(properties.getPassword())) {
            headers.add("passcode", properties.getPassword());
        }

        //return client.connect(properties.getUrl(), (WebSocketHttpHeaders) null, headers, new SessionHandler()).get();

        return null;
    }

    /**
     * Retries the connection no more than once every RETRY_INTERVAL milliseconds.
     */
    private void retryConnect() throws InterruptedException, ExecutionException {
        var waitTime = System.currentTimeMillis() - lastConnectionAttemptAt;
        if (waitTime < RETRY_INTERVAL) {
            TimeUnit.MILLISECONDS.sleep(RETRY_INTERVAL - waitTime);
        }

        connect();
    }

    /**
     * SessionHandler handle STOMP connection session.
     */
    private class SessionHandler extends StompSessionHandlerAdapter {
        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            log.info("Successfully connected to the broker");
        }

        @Override
        public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload,
                                    Throwable e) {

            log.error("Exception occurred while STOMP session processing", e);
        }

        @SneakyThrows
        @Override
        public void handleTransportError(StompSession sess, Throwable e) {
            log.error("STOMP transport error occurred, reconnecting", e);

            retryConnect();
        }
    }
}
