package com.drawers.dao.packets;

import com.drawers.dao.mqttinterface.PublisherImpl;
import com.drawers.dao.packets.listeners.ChatStateListener;
import com.drawers.dao.packets.listeners.DeliveryReceiptListener;
import com.drawers.dao.packets.listeners.GroupMembersListener;
import com.drawers.dao.packets.listeners.GroupMessageListener;
import com.drawers.dao.packets.listeners.GroupProfileListener;
import com.drawers.dao.packets.listeners.InvitationListener;
import com.drawers.dao.packets.listeners.NewCallListener;
import com.drawers.dao.packets.listeners.PresenceListener;
import com.drawers.dao.packets.group.GroupMembers;
import com.drawers.dao.packets.group.GroupMessage;
import com.drawers.dao.packets.group.GroupProfile;
import com.drawers.dao.packets.group.Invitation;
import com.drawers.dao.packets.listeners.NewMessageListener;
import com.drawers.dao.packets.listeners.ProfileUpdateListener;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by harshit on 14/2/16.
 */
public final class MqttProviderManager {
    private static Map<PublisherImpl, MqttProviderManager> INSTANCES = new WeakHashMap<>();
    private String clientId;
    private String name;

    public static synchronized MqttProviderManager getInstanceFor(PublisherImpl publisher) {
        MqttProviderManager mqttProviderManager = INSTANCES.get(publisher);
        if (mqttProviderManager == null) {
            mqttProviderManager = new MqttProviderManager();
            INSTANCES.put(publisher, mqttProviderManager);
        }
        return mqttProviderManager;
    }

    private MqttProviderManager() {
        providerMap.put(MqttChat.NAMESPACE, new MqttChat.MqttMessageProvider());
        providerMap.put(DeliveryReceipt.NAMESPACE, new DeliveryReceipt.DeliveryReceiptProvider());
        providerMap.put(Presence.NAMESPACE, new Presence.PresenceProvider());
        providerMap.put(ChatState.NAMESPACE, new ChatState.ChatStateProvider());
        providerMap.put(Invitation.NAMESPACE, new Invitation.InvitationProvider());
        providerMap.put(GroupMembers.NAMESPACE, new GroupMembers.GroupMembersProvider());
        providerMap.put(MqttCall.NAMESPACE,  new MqttCall.MqttCallProvider());
        providerMap.put(GroupProfile.NAMESPACE, new GroupProfile.GroupProfileProvider());
        providerMap.put(GroupMessage.NAMESPACE, new GroupMessage.GroupMessageProvider());
        providerMap.put(Profile.NAMESPACE, new Profile.ProfileProvider());
    }

    public void setClientIdAndName(String clientId, String name) {
        this.clientId = clientId;
        this.name = name;
        addClientIdAndName(clientId, name);
    }

    private Map<String, MqttProvider> providerMap = new ConcurrentHashMap<>();

    public void addToProviderMap(String key, MqttProvider mqttProvider) {
        providerMap.put(key, mqttProvider);
    }

    public void dispatch(MqttMessage mqttMessage, String topic, PublisherImpl mqttConnection) throws ParseFailedException {
        // Add error conditions.
        String key = topic.substring(topic.indexOf("/"));
        if (!providerMap.containsKey(key)) {
            return;
        }
        try {
            providerMap.get(key).processStanza(topic.substring(0, topic.indexOf("/")), new String(mqttMessage.getPayload()), mqttConnection);
        } catch (Exception e) {
            throw new ParseFailedException(e.getMessage());
        }
    }

    public void delivered(MqttMessage mqttMessage, String topic) throws ParseFailedException {
        // Add error conditions.
        String key = topic.substring(topic.indexOf("/"));
        if (!providerMap.containsKey(key)) {
            return;
        }
        if (mqttMessage == null) {
            return;
        }
        // We need a try catch to avoid infinite loops.
        try {
            providerMap.get(key).acknowledgeStanza(topic.substring(0, topic.indexOf("/")), new String(mqttMessage.getPayload()));
        } catch (Exception e) {
            throw new ParseFailedException(e.getMessage());
        }
    }


    public void clearListeners() {
        for (MqttProvider mqttProvider:
             providerMap.values()) {
            mqttProvider.clearListener();
        }
    }

    public void addMessageListener(NewMessageListener newMessageListener) {
        MqttChat.MqttMessageProvider mqttMessageProvider = (MqttChat.MqttMessageProvider) providerMap.get(MqttChat.NAMESPACE);
        mqttMessageProvider.addMessageListener(newMessageListener);
    }

    public void addPresenceListener(PresenceListener presenceListener) {
        Presence.PresenceProvider presenceProvider = (Presence.PresenceProvider) providerMap.get(Presence.NAMESPACE);
        presenceProvider.addPresenceListener(presenceListener);
    }

    public void addChatStateListener(ChatStateListener chatStateListener) {
        ChatState.ChatStateProvider chatStateProvider = (ChatState.ChatStateProvider) providerMap.get(ChatState.NAMESPACE);
        chatStateProvider.addChatStateListener(chatStateListener);
    }

    public void addProfileUpdateListener(ProfileUpdateListener profileUpdateListener) {
        Profile.ProfileProvider profileProvider = (Profile.ProfileProvider) providerMap.get(Profile.NAMESPACE);
        profileProvider.addProfileUpdateListeners(profileUpdateListener);
    }

    public void addDeliveryReceiptListener(DeliveryReceiptListener deliveryReceiptListener) {
        DeliveryReceipt.DeliveryReceiptProvider deliveryReceiptProvider = (DeliveryReceipt.DeliveryReceiptProvider) providerMap.get(DeliveryReceipt.NAMESPACE);
        deliveryReceiptProvider.addDeliveryReceiptListener(deliveryReceiptListener);
    }

    public void addGroupMembersListener(GroupMembersListener groupMembersListener) {
        GroupMembers.GroupMembersProvider groupMembersProvider = (GroupMembers.GroupMembersProvider) providerMap.get(GroupMembers.NAMESPACE);
        groupMembersProvider.addGroupMembersListener(groupMembersListener);
    }

    public void addInvitationListener(InvitationListener invitationListener) {
        Invitation.InvitationProvider invitationProvider = (Invitation.InvitationProvider) providerMap.get(Invitation.NAMESPACE);
        invitationProvider.addInvitationListener(invitationListener);
    }

    public void addGroupProfileListener(GroupProfileListener groupProfileListener) {
        GroupProfile.GroupProfileProvider groupProfileProvider = (GroupProfile.GroupProfileProvider) providerMap.get(GroupProfile.NAMESPACE);
        groupProfileProvider.addGroupProfileListener(groupProfileListener);
    }

    public void addGroupMessageListener(GroupMessageListener groupMessageListener) {
        GroupMessage.GroupMessageProvider groupMessageProvider = (GroupMessage.GroupMessageProvider) providerMap.get(GroupMessage.NAMESPACE);
        groupMessageProvider.addGroupMessageListener(groupMessageListener);
    }

    public void addCallListener(NewCallListener newCallListener) {
        MqttCall.MqttCallProvider mqttCallProvider = (MqttCall.MqttCallProvider) providerMap.get(MqttCall.NAMESPACE);
        mqttCallProvider.addCallListener(newCallListener);
    }

    public void addClientIdAndName(String clientId, String userName) {
        Invitation.InvitationProvider invitationProvider = (Invitation.InvitationProvider) providerMap.get(Invitation.NAMESPACE);
        invitationProvider.setClientIdAndName(clientId, userName);
        GroupMembers.GroupMembersProvider groupMembersProvider = (GroupMembers.GroupMembersProvider) providerMap.get(GroupMembers.NAMESPACE);
        groupMembersProvider.setSelfClientId(clientId);
    }
}
