package com.drawers.dao.packets.listeners;

import com.drawers.dao.MqttCallMessage;

/**
 * Created by harshit on 13/5/16.
 */
public interface NewCallListener {
    public void receiveCall(MqttCallMessage mqttCallMessage);
}
