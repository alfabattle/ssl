package com.ashikhman.ssl.listener;

import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

/**
 * Generic interface for topic listeners.
 */
public abstract class AbstractStompListener extends StompSessionHandlerAdapter {

    /**
     * Returns topic name for subscription.
     *
     * @return topic name
     */
    public abstract String getTopicName();
}
