package com.drawers.dao.packets;

import org.eclipse.paho.client.mqttv3.internal.wire.MqttWireMessage;

/**
 * Created by nishant.pathak on 16/02/16.
 */
public abstract class MqttIqProvider<I extends MqttIq> {
    abstract void processIq(String substring, String action, MqttWireMessage message);
}
