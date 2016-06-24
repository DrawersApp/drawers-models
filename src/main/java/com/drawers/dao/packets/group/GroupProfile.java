package com.drawers.dao.packets.group;

import com.drawers.dao.ChatConstant;
import com.drawers.dao.mqttinterface.PublisherImpl;
import com.drawers.dao.packets.listeners.GroupProfileListener;
import com.drawers.dao.utils.Singletons;
import com.drawers.dao.packets.MqttProvider;
import com.drawers.dao.packets.MqttStanaza;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;


/**
 * Created by harshit on 17/2/16.
 */
public class GroupProfile extends MqttStanaza {
    public static final String NAMESPACE = "/g/p";


    public GroupProfile(String uid, GroupProfileInfo groupProfileInfo) {
        this.uid = uid;
        this.groupProfileInfo = groupProfileInfo;
    }

    private final String uid;
    private final GroupProfileInfo groupProfileInfo;
    public static final int QOS = 1;
    public static final boolean retained = true;

    @Override
    public String getChannel() {
        return uid + NAMESPACE;
    }

    @Override
    public String getMessage() {
        return Singletons.singletonsInstance.gson.toJson(groupProfileInfo);
    }

    @Override
    public void sendStanza(PublisherImpl publisher) {
        MqttMessage mqttMessage = new MqttMessage(getMessage().getBytes());
        mqttMessage.setQos(QOS);
        mqttMessage.setRetained(retained);
        publisher.publish(getChannel(), mqttMessage, null, null);
    }

    public static class GroupProfileProvider extends MqttProvider {

        Set<GroupProfileListener> groupProfileListeners = new CopyOnWriteArraySet<>();

        public void addGroupProfileListener(GroupProfileListener groupProfileListener) {
            groupProfileListeners.add(groupProfileListener);
        }

        @Override
        public void clearListener() {
            groupProfileListeners.clear();
        }

        @Override
        public void processStanza(final String topic, String mqttStanaza, PublisherImpl publisher) {
            final GroupProfileInfo groupProfile = fromString(mqttStanaza);
            if (groupProfile.name == null) {
                return;
            }
            if (groupProfile.senderId == null || groupProfile.message == null) {
                return;
            }
            for (GroupProfileListener groupProfileListener : groupProfileListeners) {
                groupProfileListener.groupProfileChanged(groupProfile, topic);
            }
        }
    }

    public static GroupProfileInfo fromString(String json) {
        return Singletons.singletonsInstance.gson.fromJson(json, GroupProfileInfo.class);
    }


    public static class GroupProfileInfo {
        private String name;
        private String image;
        private String message;
        private String senderId;

        public GroupProfileInfo(String name, String image, String message, String senderId) {
            this.name = name;
            this.image = image;
            this.message = message;
            this.senderId = senderId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getSenderId() {
            return senderId;
        }

        public void setSenderId(String senderId) {
            this.senderId = senderId;
        }
    }
}
