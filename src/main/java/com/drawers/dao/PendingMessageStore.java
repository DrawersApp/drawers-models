package com.drawers.dao;

import java.util.List;

/**
 * Created by harshit on 28/5/16.
 */
public interface PendingMessageStore {
    void addPendingMessage(PendingMqttMessage pendingMqttMessage);
    List<PendingMqttMessage> retrieveConversations();
}
