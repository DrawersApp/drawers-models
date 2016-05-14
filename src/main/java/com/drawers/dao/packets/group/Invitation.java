package com.drawers.dao.packets.group;

import com.drawers.dao.mqttinterface.PublisherImpl;
import com.drawers.dao.packets.listeners.InvitationListener;
import com.drawers.dao.utils.Singletons;
import com.drawers.dao.packets.MqttProvider;
import com.drawers.dao.packets.MqttStanaza;
import com.drawers.dao.packets.SubscribeOthers;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;


/**
 * Created by harshit on 15/2/16.
 */
public class Invitation extends MqttStanaza {
    public static final String NAMESPACE = "/o/i";

    public Invitation(String groupId, String uid, String name, String image) {
        this.groupId = new InvitationInfo(name, image, groupId);
        this.uid = uid;
    }

    private final InvitationInfo groupId;
    private final String uid;

    public static final int QOS = 1;

    @Override
    public String getChannel() {
        return uid + NAMESPACE;
    }

    @Override
    public String getMessage() {
        return Singletons.singletonsInstance.gson.toJson(groupId);
    }

    @Override
    public void sendStanza(PublisherImpl publisher) {
        MqttMessage mqttMessage = new MqttMessage(getMessage().getBytes());
        mqttMessage.setQos(QOS);
        mqttMessage.setRetained(false);
        publisher.publish(getChannel(), mqttMessage, null, null);
    }

    public static class InvitationProvider extends MqttProvider {

        // Accept it by putting it in your db and subscribing to /g/+, /o/m. - mqttstanza - groupId.
        Set<InvitationListener> invitationListeners = new CopyOnWriteArraySet<>();

        public void addInvitationListener(InvitationListener invitationListener) {
            invitationListeners.add(invitationListener);
        }

        @Override
        public void clearListener() {
            invitationListeners.clear();
        }

        @Override
        public void processStanza(String topic, String mqttStanaza, PublisherImpl publisher) {
            InvitationInfo invitationInfo = fromString(mqttStanaza);
            if (invitationInfo.name == null || invitationInfo.groupId == null) {
                return;
            }
            publisher.subscribe(new SubscribeOthers(SubscribeOthers.GROUP_NAMESPACE,  invitationInfo.groupId).getChannel(), 1,
                    null, null);
            for (InvitationListener invitationListener : invitationListeners) {
                invitationListener.invited(invitationInfo, publisher);
            }
        }
    }

    public static class InvitationInfo {
        public InvitationInfo(String name, String image, String groupId) {
            this.name = name;
            this.image = image;
            this.groupId = groupId;
        }

        private String name;
        private String image;
        private String groupId;

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

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }
    }

    public static InvitationInfo fromString(String json) {
        return Singletons.singletonsInstance.gson.fromJson(json, InvitationInfo.class);
    }

}
