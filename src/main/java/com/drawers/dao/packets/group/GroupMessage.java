package com.drawers.dao.packets.group;

import com.drawers.dao.mqttinterface.PublisherImpl;
import com.drawers.dao.packets.listeners.GroupMessageListener;
import com.drawers.dao.utils.Singletons;
import com.google.gson.annotations.SerializedName;
import com.drawers.dao.packets.MqttChat;
import com.drawers.dao.packets.MqttProvider;
import com.drawers.dao.packets.MqttStanaza;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import com.drawers.dao.ChatConstant;

/**
 * Created by harshit on 18/2/16.
 */
public class GroupMessage extends MqttStanaza {
    public static final String NAMESPACE = "/g/m";
    private static final String TAG = MqttChat.class.getSimpleName();
    private final GroupMessageContainer groupMessageContainer;
    protected final String uid;
    public static final int QOS = 1;
    public GroupMessage(String uid, String messageid, String message, ChatConstant.ChatType chatType, String selfClientId) {
        this.uid = uid;
        groupMessageContainer = new GroupMessageContainer(messageid, message, selfClientId, chatType);
    }

    @Override
    public String getChannel() {
        return uid + NAMESPACE;
    }

    @Override
    public String getMessage() {
        return Singletons.singletonsInstance.gson.toJson(groupMessageContainer);
    }

    @Override
    public void sendStanza(PublisherImpl publisher) {
        MqttMessage mqttMessage = new MqttMessage(getMessage().getBytes());
        mqttMessage.setQos(QOS);
        mqttMessage.setRetained(true);
        publisher.publish(getChannel(), mqttMessage, null, null);
    }

    public static class GroupMessageContainer {

        @SerializedName("i")
        public String messageId;
        @SerializedName("m")
        public String message;
        @SerializedName("s")
        public String senderUid;

        public GroupMessageContainer(String messageId, String message, String senderUid, ChatConstant.ChatType chatType) {
            this.messageId = messageId;
            this.message = message;
            this.senderUid = senderUid;
            this.chatType = chatType;
        }

        @SerializedName("c")
        public ChatConstant.ChatType chatType;
    }

    public static class GroupMessageProvider extends MqttProvider {
        @Override
        public void processStanza(String topic, String mqttStanaza, PublisherImpl publisher) {
            GroupMessageContainer groupMessageContainer = fromString(mqttStanaza);
            // Add to realm.
            if (!validate(groupMessageContainer)) {
                return;
            }
            for (GroupMessageListener groupMessageListener : groupMessageListeners) {
                groupMessageListener.receiveMessage(groupMessageContainer, topic);
            }
        }

        private boolean validate(GroupMessageContainer groupMessageContainer) {
            if (groupMessageContainer.message == null) {
                return false;
            }
            if (groupMessageContainer.messageId == null) {
                return false;
            }
            if (!ChatConstant.validType(groupMessageContainer.chatType)) {
                return false;
            }
            if (groupMessageContainer.senderUid == null) {
                return false;
            }
            return true;
        }

        @Override
        public void acknowledgeStanza(final String topic, final String mqttStanza) {
            GroupMessageContainer groupMessageContainer = fromString(mqttStanza);
            for (GroupMessageListener groupMessageListener : groupMessageListeners) {
                groupMessageListener.messageSendAck(groupMessageContainer);
            }
        }

        Set<GroupMessageListener> groupMessageListeners = new CopyOnWriteArraySet<>();

        public void addGroupMessageListener(GroupMessageListener groupMessageListener) {
            groupMessageListeners.add(groupMessageListener);
        }

        @Override
        public void clearListener() {
            groupMessageListeners.clear();
        }
    }

    public static GroupMessageContainer fromString(String json) {
        return Singletons.singletonsInstance.gson.fromJson(json, GroupMessageContainer.class);
    }


}
