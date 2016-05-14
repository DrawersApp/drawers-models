package com.drawers.dao.packets;


import com.drawers.dao.mqttinterface.PublisherImpl;
import com.drawers.dao.packets.listeners.PresenceListener;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;


/**
 * Last will and testament feature.
 */
public class Presence extends MqttStanaza {
    public static final String NAMESPACE = "/s/l";
    private final String uid;
    private final PresenceStatus status;
    public static final int QOS = 0;
    public Presence(String uid, PresenceStatus status) {
        this.uid = uid;
        this.status = status;
    }
    @Override
    public String getChannel() {
        return uid + NAMESPACE;
    }

    @Override
    public String getMessage() {
        return status.name();
    }



    @Override
    public void sendStanza(PublisherImpl mqttConnection) {
        MqttMessage mqttMessage = new MqttMessage(getMessage().getBytes());
        mqttMessage.setQos(QOS);
        mqttMessage.setRetained(true);
        mqttConnection.publish(getChannel(), mqttMessage, null, null);
    }

    public static class PresenceProvider extends MqttProvider {

        public Set<PresenceListener> presenceListeners = new CopyOnWriteArraySet<>();

        public void addPresenceListener(PresenceListener presenceListener) {
            presenceListeners.add(presenceListener);
        }

        @Override
        public void clearListener() {
            presenceListeners.clear();
        }

        private static final String TAG = PresenceProvider.class.getSimpleName();

        @Override
        public void processStanza(final String topic, final String mqttStanaza, PublisherImpl mqttConnection) {
            // Mark the presence of client.
            String userId = topic;
            if (!PresenceStatus.values.contains(mqttStanaza)) {
                return;
            }
            PresenceStatus presence = PresenceStatus.valueOf(mqttStanaza);
            for (PresenceListener presenceListener : presenceListeners) {
                presenceListener.notifyPresence(presence, userId, topic);
            }
        }
    }

    public enum PresenceStatus {
        L("Online"),
        F("Offline");

        private final String status;
        PresenceStatus(String status) {
            this.status = status;
        }
        static HashSet<String> values = new HashSet<String>();
        static {
            for (PresenceStatus value : PresenceStatus.values()) {
                values.add(value.name());
            }
        }
    }
}
