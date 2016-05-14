package com.drawers.dao.packets;

import com.drawers.dao.packets.listeners.SubscribeListener;
import com.drawers.dao.packets.listeners.Subscriber;

import org.eclipse.paho.client.mqttv3.internal.wire.MqttWireMessage;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by nishant.pathak on 16/02/16.
 */
public class MqttIqProviderManager {
    public static final MqttIqProviderManager manager = new MqttIqProviderManager();

    private static Map<Subscriber, MqttIqProviderManager> INSTANCES = new WeakHashMap<>();

    public static synchronized MqttIqProviderManager getInstanceFor(Subscriber subscriber) {
        MqttIqProviderManager mqttProviderManager = INSTANCES.get(subscriber);
        if (mqttProviderManager == null) {
            mqttProviderManager = new MqttIqProviderManager();
            INSTANCES.put(subscriber, mqttProviderManager);
        }
        return mqttProviderManager;
    }
    private final Map<String, MqttIqProvider<MqttIq>> iqProviders = new ConcurrentHashMap<>();

    public MqttIqProviderManager() {
        iqProviders.put(SubscribeOthers.OTHERS_NAMESPACE, new SubscribeOthers.SubscribeOthersProvider());
    }

    public void process(String topic, String Action, MqttWireMessage message) {
        // Add error conditions.
        String key = topic.substring(topic.indexOf("/"));
        if (!iqProviders.containsKey(key)) {
            return;
        }

        iqProviders.get(key).processIq(topic.substring(0, topic.indexOf("/")), Action, message);

    }

    public void addSubscribeListener(SubscribeListener subscribeListener) {
        SubscribeOthers.SubscribeOthersProvider subscribeOthersProvider =
                (SubscribeOthers.SubscribeOthersProvider) iqProviders.get(SubscribeOthers.OTHERS_NAMESPACE);
        subscribeOthersProvider.addSubscribeListener(subscribeListener);
    }
}
