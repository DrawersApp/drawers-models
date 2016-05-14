package com.drawers.dao.mqttinterface;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by harshit on 13/5/16.
 */
public interface PublisherImpl {

    public IMqttDeliveryToken publish(String topic, MqttMessage message,
                                      String invocationContext, String activityToken);
    public void subscribe(final String topic, final int qos,
                          String invocationContext, String activityToken);
    public void unsubscribe(final String topic, String invocationContext,
                            String activityToken);
}
