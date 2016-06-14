package com.drawers.dao;

/**
 * Class to store and retrieve message when client is not connected.
 * Standard callbacks are provided to persist/retrieve this message from custom message store.
 */
public class PendingMqttMessage {
    private String topic;
    private byte[] payload;
    private int QOS;
    private boolean retained;


    public PendingMqttMessage(String topic, byte[] payload, int QOS, boolean retained) {
        this.topic = topic;
        this.payload = payload;
        this.QOS = QOS;
        this.retained = retained;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }

    public int getQOS() {
        return QOS;
    }

    public void setQOS(int QOS) {
        this.QOS = QOS;
    }

    public boolean isRetained() {
        return retained;
    }

    public void setRetained(boolean retained) {
        this.retained = retained;
    }
}
