package com.drawers.dao.packets;

import com.drawers.dao.mqttinterface.PublisherImpl;
import com.drawers.dao.packets.listeners.ProfileUpdateListener;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * For profile updates. Do we need this class or gcm is enough.
 */
public class Profile extends MqttStanaza {

    public static final String NAMESPACE = "/s/p";
    private final String uid;
    public static final int QOS = 1;

    public Profile(String uid) {
        this.uid = uid;
    }

    @Override
    public String getChannel() {
        return uid + NAMESPACE;
    }

    @Override
    public String getMessage() {
        return uid;
    }

    @Override
    public void sendStanza(PublisherImpl mqttConnection) {

    }

    public static class ProfileProvider extends MqttProvider {

        private Set<ProfileUpdateListener> profileUpdateListeners = new CopyOnWriteArraySet<>();

        public void addProfileUpdateListeners(ProfileUpdateListener profileUpdateListener) {
            profileUpdateListeners.add(profileUpdateListener);
        }

        @Override
        public void clearListener() {
            profileUpdateListeners.clear();
        }

        private static final String TAG = ProfileProvider.class.getSimpleName();

        @Override
        public void processStanza(String topic, String uid, PublisherImpl publisher) {

            for (ProfileUpdateListener profileUpdateListener : profileUpdateListeners) {
                profileUpdateListener.notifyProfileUpdate(uid);
            }
        }
    }
}
