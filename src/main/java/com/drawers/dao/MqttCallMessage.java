package com.drawers.dao;

import com.drawers.dao.message.BaseMessage;
import com.drawers.dao.utils.Singletons;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by nishant.pathak on 04/05/16.
 */
public class MqttCallMessage implements BaseMessage {

    @SerializedName("i")
    public String messageId;
    @SerializedName("m")
    public String message;
    @SerializedName("s")
    public String senderUid;
    @SerializedName("c")
    public ChatConstant.ChatType chatType;
    @SerializedName("t")
    public long time;



    public MqttCallMessage(String messageId, String message,
                           String senderUid, ChatConstant.ChatType chatType, long time) {
        this.messageId = messageId;
        this.message = message;
        this.senderUid = senderUid;
        this.chatType = chatType;
        this.time = time;
    }

    public static MqttCallMessage fromString(String json) {
        return Singletons.singletonsInstance.gson.fromJson(json, MqttCallMessage.class);
    }

    @Override
    public String toJsonString() {
        return Singletons.singletonsInstance.gson.toJson(this);
    }
}
