package com.drawers.dao.packets.listeners;

/**
 * Created by harshit on 13/5/16.
 */
public interface Subscriber {
    public void subscribe(final String topic, final int qos,
                          String invocationContext, String activityToken);
    public void unsubscribe(final String topic, String invocationContext,
                            String activityToken);
}
