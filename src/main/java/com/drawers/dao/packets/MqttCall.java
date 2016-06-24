package com.drawers.dao.packets;


import com.drawers.dao.ChatConstant;
import com.drawers.dao.MqttCallMessage;
import com.drawers.dao.mqttinterface.PublisherImpl;
import com.drawers.dao.packets.listeners.NewCallListener;
import com.drawers.dao.utils.Singletons;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;


/**
 * Created by harshit on 14/2/16.
 */
public class MqttCall extends MqttStanaza {

    public static final String NAMESPACE = "/o/l";
    private static final String TAG = MqttCall.class.getSimpleName();
    private final MqttCallMessage mqttCallMessage;
    private final String uid;
    public int QOS = 1;


    public MqttCall(String uid, String messageid, String message, long time,
                    ChatConstant.ChatType chatType, int QOS, String selfClientId) {
        this.uid = uid;
        mqttCallMessage = new MqttCallMessage(messageid, message, selfClientId, chatType, time);
        this.QOS = QOS;
    }


    @Override
    public String getChannel() {
        return uid + NAMESPACE;
    }

    @Override
    public String getMessage() {
        return Singletons.singletonsInstance.gson.toJson(mqttCallMessage);
    }

    @Override
    public void sendStanza(PublisherImpl publisher) {
        MqttMessage mqttMessage = new MqttMessage(getMessage().getBytes());
        mqttMessage.setQos(QOS);
        mqttMessage.setRetained(false);
        publisher.publish(getChannel(), mqttMessage, null, null);
    }

    public static class MqttCallProvider extends MqttProvider {

        private Set<NewCallListener> callListeners = new CopyOnWriteArraySet<>();

        public void addCallListener(NewCallListener newCallListener) {
            callListeners.add(newCallListener);
        }

        @Override
        public void clearListener() {
            callListeners.clear();
        }

        @Override
        public void processStanza(String topic, String mqttStanaza, PublisherImpl publisher) {
            for (NewCallListener callListener : callListeners) {
                callListener.receiveCall(MqttCallMessage.fromString(mqttStanaza));
            }
        }
    }

    public static String getChatChannel(String groupId) {
        return groupId + NAMESPACE;
    }


}

