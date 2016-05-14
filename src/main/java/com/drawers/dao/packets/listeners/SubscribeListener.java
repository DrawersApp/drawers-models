package com.drawers.dao.packets.listeners;

import org.eclipse.paho.client.mqttv3.internal.wire.MqttWireMessage;

/**
 * Created by harshit on 13/5/16.
 */
public interface SubscribeListener {
    void processIq(String id, String action, MqttWireMessage message);
}
