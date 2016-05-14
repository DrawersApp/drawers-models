package com.drawers.dao.packets.listeners;

import com.drawers.dao.MqttChatMessage;

/**
 * Created by harshit on 12/5/16.
 */
public interface NewMessageListener {
    public void receiveMessage(final MqttChatMessage message);
    public void acknowledgeStanza(final MqttChatMessage message);
}
