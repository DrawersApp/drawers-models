package com.drawers.dao.packets;

import com.drawers.dao.ChatConstant;
import com.drawers.dao.MqttChatMessage;
import com.drawers.dao.utils.Singletons;
import com.drawers.dao.mqttinterface.PublisherImpl;
import com.drawers.dao.packets.listeners.NewMessageListener;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;


/**
 * Created by harshit on 14/2/16.
 * Usage - For single user chat subscribe to your own uid/o/m
 * To send message publish to frienduid/o/m.
 * For groupchat subscribe to groupid/o/m
 * To send message publish to groupid/o/m.
 * The information if it is a group chat or not is not contained here.
 */
public class MqttChat extends MqttStanaza {

    public static final String NAMESPACE = "/o/m";
    private static final String TAG = MqttChat.class.getSimpleName();
    private final MqttChatMessage mqttChatMessage;
    protected final String uid;
    public static final int QOS = 1;

    public MqttChat(String uid, String messageid, String  message, boolean deliveryReceipt,
                    ChatConstant.ChatType chatType, String selfClientId) {
        this.uid = uid;
        mqttChatMessage = new MqttChatMessage(messageid, message, selfClientId, chatType, deliveryReceipt);
    }

    @Override
    public String getChannel() {
        return uid + NAMESPACE;
    }

    @Override
    public String getMessage() {
        return Singletons.singletonsInstance.gson.toJson(mqttChatMessage);
    }


    @Override
    public void sendStanza(PublisherImpl publisher) {
        MqttMessage mqttMessage = new MqttMessage(getMessage().getBytes());
        mqttMessage.setQos(QOS);
        mqttMessage.setRetained(false);
        publisher.publish(getChannel(), mqttMessage, null, null);
    }

    public static class MqttMessageProvider extends MqttProvider {

        public Set<NewMessageListener> messageListeners = new CopyOnWriteArraySet<>();

        public void addMessageListener(NewMessageListener messageListener) {
            messageListeners.add(messageListener);
        }

        public void removeMessageListener(NewMessageListener messageListener) {
            messageListeners.remove(messageListener);
        }

        @Override
        public void clearListener() {
            messageListeners.clear();
        }

        @Override
        public void processStanza(String topic, String mqttStanaza, PublisherImpl mqttConnection) {
            MqttChatMessage mqttChatMessage = fromString(mqttStanaza);
            // Notify Listeners.
            if (!validate(mqttChatMessage)) {
                return;
            }
            for (NewMessageListener messageListener : messageListeners) {
                messageListener.receiveMessage(mqttChatMessage);
            }
            // If delivery receipt required, send one.
            if (mqttChatMessage.deliveryReceipt) {
                new DeliveryReceipt(mqttChatMessage.messageId, mqttChatMessage.senderUid).sendStanza(mqttConnection);
            }
        }

        private boolean validate(MqttChatMessage mqttChatMessage) {
            if (mqttChatMessage.message == null) {
                return false;
            }
            if (mqttChatMessage.messageId == null) {
                return false;
            }
            if (!ChatConstant.validType(mqttChatMessage.chatType)) {
                return false;
            }
            if (mqttChatMessage.senderUid == null) {
                return false;
            }
            return true;
        }

        @Override
        public void acknowledgeStanza(final String topic, final String mqttStanza) {
            final MqttChatMessage mqttChatMessage = fromString(mqttStanza);
            for (NewMessageListener messageListener : messageListeners) {
                messageListener.acknowledgeStanza(mqttChatMessage);
            }

        }
    }

    public static MqttChatMessage fromString(String json) {
        return Singletons.singletonsInstance.gson.fromJson(json, MqttChatMessage.class);
    }

}
