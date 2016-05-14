package com.drawers.dao.packets;

import com.drawers.dao.mqttinterface.PublisherImpl;

/**
 * Created by harshit on 12/2/16.
 */
public abstract class MqttProvider {
    public abstract void processStanza(String topic, String mqttStanaza, PublisherImpl publisher);
    public void acknowledgeStanza(String topic, String mqttStanza) {
    }
    public void clearListener() {

    }
}
