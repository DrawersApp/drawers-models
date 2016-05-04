package com.drawers.dao;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nishant.pathak on 04/05/16.
 */
public class MqttCallMessage {

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

}
