package com.drawers.dao.packets;

import com.drawers.dao.packets.listeners.Subscriber;

/**
 * Created by nishant.pathak on 16/02/16.
 */
public interface MqttIq {
    public String getChannel();
    public void subscribe(Subscriber subscriber);
    public void unsubscribe(Subscriber subscriber);
}
