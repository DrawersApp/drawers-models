package com.drawers.dao.packets.group;


import com.drawers.dao.mqttinterface.PublisherImpl;
import com.drawers.dao.packets.MqttProvider;
import com.drawers.dao.packets.MqttStanaza;
import com.drawers.dao.packets.listeners.GroupMembersListener;
import com.drawers.dao.utils.Singletons;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by harshit on 15/2/16.
 */
public class GroupMembers extends MqttStanaza {
    public static final String NAMESPACE = "/g/e";
    public static final int QOS = 1;
    public static final boolean retained = true;

    public GroupMembers(GroupMembersList groupMembersList, String id) {
        this.groupMembersList = groupMembersList;
        this.id = id;
    }

    private final GroupMembersList groupMembersList;
    private final String id;

    @Override
    public String getChannel() {
        return id + NAMESPACE;
    }

    @Override
    public String getMessage() {
        return Singletons.singletonsInstance.gson.toJson(groupMembersList);
    }

    @Override
    public void sendStanza(PublisherImpl publisher) {
        MqttMessage mqttMessage = new MqttMessage(getMessage().getBytes());
        mqttMessage.setQos(QOS);
        mqttMessage.setRetained(retained);
        publisher.publish(getChannel(), mqttMessage, null, null);
    }

    public static class GroupMembersProvider extends MqttProvider {

        private Set<GroupMembersListener> groupMembersListeners = new CopyOnWriteArraySet<>();

        public void addGroupMembersListener(GroupMembersListener groupMembersListener) {
            groupMembersListeners.add(groupMembersListener);
        }
        @Override
        public void clearListener() {
            groupMembersListeners.clear();
        }

        @Override
        public void processStanza(String topic, String mqttStanaza, PublisherImpl publisher) {
            GroupMembersList groupMembersList = fromString(mqttStanaza);
            for (GroupMembersListener groupMembersListener : groupMembersListeners) {
                groupMembersListener.groupMemberAdded(groupMembersList, topic, publisher);
            }
        }
    }

    public static class GroupMembersList {
        public GroupMembersList(Set<String> m) {
            this.m = m;
        }

        public Set<String> getM() {
            return m;
        }

        public void setM(Set<String> m) {
            this.m = m;
        }

        private Set<String> m; // members.
    }

    public static GroupMembersList fromString(String json) {
        return Singletons.singletonsInstance.gson.fromJson(json, GroupMembersList.class);
    }

}
