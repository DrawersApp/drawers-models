package com.drawers.dao.packets;

import com.drawers.dao.packets.listeners.SubscribeListener;
import com.drawers.dao.packets.listeners.Subscriber;

import org.eclipse.paho.client.mqttv3.internal.wire.MqttWireMessage;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;


/**
 * Created by nishant.pathak on 16/02/16.
 * others will publish on this, so subscribe it to listen
 */

public class SubscribeOthers implements MqttIq {
    public static final String OTHERS_NAMESPACE = "/o/+";
    public static final String SELF_NAMESPACE = "/s/+";
    public static final String GROUP_NAMESPACE = "/g/+";

    private final String clientId;
    public SubscribeOthers(String nameSpace, String clientId) {
        this.nameSpace = nameSpace;
        this.clientId = clientId;
    }

    private final String nameSpace;

    @Override
    public String getChannel() {
        return clientId + nameSpace;
    }

    @Override
    public void subscribe(Subscriber subscriber) {
        subscriber.subscribe(getChannel(), 1, null, null);
    }

    @Override
    public void unsubscribe(Subscriber subscriber) {
        subscriber.unsubscribe(getChannel(), null, null);
    }

    public static class SubscribeOthersProvider extends MqttIqProvider<MqttIq> {

        Set<SubscribeListener> subscribeListeners = new CopyOnWriteArraySet<>();

        public void addSubscribeListener(SubscribeListener subscribeListener) {
            subscribeListeners.add(subscribeListener);
        }

        @Override
        void processIq(String id, String action, MqttWireMessage message) {
            for (SubscribeListener subscribeListener : subscribeListeners) {
                subscribeListener.processIq(id, action, message);
            }
        }
    }
}
