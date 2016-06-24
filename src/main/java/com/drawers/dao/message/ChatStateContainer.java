package com.drawers.dao.message;

import com.drawers.dao.packets.ChatState;
import com.drawers.dao.utils.Singletons;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nishant.pathak on 19/06/16.
 */
public class ChatStateContainer implements BaseMessage {
    public ChatStateContainer(String id, ChatState.ChatStateValues chatStateValues) {
        this.id = id;
        this.chatStateValues = chatStateValues;
    }

    @SerializedName("i")
    private String id; // sender id - expected to find who is typing.
    @SerializedName("c")
    private ChatState.ChatStateValues chatStateValues;

    @Override
    public String toJsonString() {
        return null;
    }

    public String getId() {
        return id;
    }

    public ChatState.ChatStateValues getChatStateValues() {
        return chatStateValues;
    }

    public static ChatStateContainer fromString(String json) {
        return Singletons.singletonsInstance.gson.fromJson(json, ChatStateContainer.class);
    }

}
