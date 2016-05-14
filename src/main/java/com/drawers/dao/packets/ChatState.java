package com.drawers.dao.packets;


import com.drawers.dao.mqttinterface.PublisherImpl;
import com.drawers.dao.packets.listeners.ChatStateListener;
import com.drawers.dao.utils.Singletons;
import com.google.gson.annotations.SerializedName;


import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;


/**
 * Created by harshit on 14/2/16.
 */
public class ChatState extends MqttStanaza {
    public static final String NAMESPACE = "/o/c";
    private final ChatStateContainer chatStateContainer;
    private static Map<String, ChatStateValues> chatStates = new HashMap<>();

    public ChatState(ChatStateValues chatStateValues, String uid, int QOS, String selfClientId) {
        this.chatStateContainer = new ChatStateContainer(selfClientId, chatStateValues);
        this.uid = uid;
        this.QOS = QOS;
    }

    private final String uid;

    private final int QOS;


    @Override
    public String getChannel() {
        return uid + NAMESPACE;
    }

    @Override
    public String getMessage() {
        return Singletons.singletonsInstance.gson.toJson(chatStateContainer);
    }


    @Override
    public void sendStanza(PublisherImpl publisher) {
        if (!updateChatState(uid, chatStateContainer.chatStateValues)) {
            return;
        }
        MqttMessage mqttMessage = new MqttMessage(getMessage().getBytes());
        mqttMessage.setQos(QOS);
        mqttMessage.setRetained(false);
        publisher.publish(getChannel(), mqttMessage, null, null);
    }


    public enum ChatStateValues {
        ACTIVE,       // Used to differentiate between read and received.
        COMPOSING,    // Typing some message.
        PAUSED,       // Paused after some typing.
        INACTVE;      // Inactive. Left the window.

        static HashSet<ChatStateValues> values = new HashSet<>();
        static {
            Collections.addAll(values, ChatStateValues.values());
        }
    }

    public static class ChatStateProvider extends MqttProvider {

        private static final String TAG = ChatStateProvider.class.getSimpleName();

        private Set<ChatStateListener> chatStateListeners = new CopyOnWriteArraySet<>();

        public void addChatStateListener(ChatStateListener chatStateListener) {
            chatStateListeners.add(chatStateListener);
        }

        @Override
        public void clearListener() {
            chatStateListeners.clear();
        }

        @Override
        public void processStanza(String topic, String mqttStanaza, PublisherImpl publisher) {
            ChatStateContainer chatStateContainer = fromString(mqttStanaza);

            if (!ChatStateValues.values.contains(chatStateContainer.chatStateValues)) {
                return;
            }
            // Show the typing receipt. Just replicate with current logic.
            chatStateJidMapping.put(chatStateContainer.id, chatStateContainer.chatStateValues);
            for (ChatStateListener chatStateListener : chatStateListeners) {
                chatStateListener.notifyActiveListener(chatStateContainer.chatStateValues, chatStateContainer.id);
            }
            stateChanged(chatStateContainer.id, chatStateContainer.chatStateValues);

        }
    }

    // Need it to avoid same state.
    private static synchronized boolean updateChatState(String uid, ChatStateValues newState) {
        if (!chatStates.containsKey(uid)) {
            chatStates.put(uid, newState);
            return true;
        }
        ChatStateValues lastChatState = chatStates.get(uid);
        if (lastChatState != newState) {
            chatStates.put(uid, newState);
            return true;
        }
        return false;
    }

    private class ChatStateContainer {
        public ChatStateContainer(String id, ChatStateValues chatStateValues) {
            this.id = id;
            this.chatStateValues = chatStateValues;
        }

        @SerializedName("i")
        private String id; // sender id - expected to find who is typing.
        @SerializedName("c")
        private ChatStateValues chatStateValues;
    }

    public static ChatStateContainer fromString(String json) {
        return Singletons.singletonsInstance.gson.fromJson(json, ChatStateContainer.class);
    }

    // Listeners for ui
    public static void addChatStateListener(ChatStateListenerUI chatStateListenerUI) {
        chatStateListenerUIs.add(chatStateListenerUI);
    }

    public static void removeChatStateListener(ChatStateListenerUI chatStateListenerUI) {
        if (chatStateListenerUIs.contains(chatStateListenerUI)) {
            chatStateListenerUIs.remove(chatStateListenerUI);
        }
    }


    public interface ChatStateListenerUI {
        void listenToStateChanges(String uid, ChatStateValues chatStateValues);
    }
    private static Set<ChatStateListenerUI> chatStateListenerUIs = new HashSet<>();

    public static void stateChanged(String uid, ChatStateValues chatStateValues) {
        for (ChatStateListenerUI chatStateListenerUI : chatStateListenerUIs) {
            chatStateListenerUI.listenToStateChanges(uid, chatStateValues);
        }
    }

    // Read receipt logic.
    private static Map<String, ChatStateValues> chatStateJidMapping = new HashMap<>();

    public static boolean isUserActive(String jid) {
        if (!chatStateJidMapping.containsKey(jid)) {
            return false;
        }
        ChatStateValues chatState = chatStateJidMapping.get(jid);
        return !(chatState == ChatStateValues.INACTVE);
    }

}
