package com.drawers.dao.packets;

import com.drawers.dao.mqttinterface.PublisherImpl;
import com.drawers.dao.packets.listeners.DeliveryReceiptListener;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;



/**
 * Created by harshit on 14/2/16.
 */
public class DeliveryReceipt extends MqttStanaza {

    public static final String NAMESPACE = "/o/d";
    private final String uid;
    public static final int QOS = 1;

    public DeliveryReceipt(String id, String uid) {
        this.id = id;
        this.uid = uid;
    }

    private String id;
    @Override
    public String getChannel() {
        return uid + NAMESPACE;
    }

    @Override
    public String getMessage() {
        return id;
    }

    @Override
    public void sendStanza(PublisherImpl publisher) {
        MqttMessage mqttMessage = new MqttMessage(getMessage().getBytes());
        mqttMessage.setQos(QOS);
        mqttMessage.setRetained(false);
        publisher.publish(getChannel(), mqttMessage, null, null);
    }

    public static class DeliveryReceiptProvider extends MqttProvider {

        private Set<DeliveryReceiptListener> deliveryReceiptListeners = new CopyOnWriteArraySet<>();

        public void addDeliveryReceiptListener(DeliveryReceiptListener deliveryReceiptListener) {
            deliveryReceiptListeners.add(deliveryReceiptListener);
        }

        @Override
        public void clearListener() {
            deliveryReceiptListeners.clear();
        }

        @Override
        public void processStanza(String topic, String mqttStanaza, PublisherImpl publisher) {
            for (DeliveryReceiptListener deliveryReceiptListener : deliveryReceiptListeners) {
                deliveryReceiptListener.delivered(mqttStanaza);
            }
        }
    }

}
