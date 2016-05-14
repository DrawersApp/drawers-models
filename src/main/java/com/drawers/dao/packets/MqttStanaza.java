package com.drawers.dao.packets;

import com.drawers.dao.mqttinterface.PublisherImpl;

/**
 * Created by harshit on 12/2/16.
 */
public abstract class MqttStanaza<T> {
    public abstract String getChannel();
    public abstract String getMessage();
    public abstract void sendStanza(PublisherImpl publisher);
}
