package com.drawers.dao;

import com.google.gson.annotations.SerializedName;

public  class MqttChatMessage {

    @SerializedName("i")
    public String messageId;
    @SerializedName("m")
    public String message;
    @SerializedName("s")
    public String senderUid;

    @SerializedName("c")
    public ChatConstant.ChatType chatType;
    @SerializedName("d")
    public boolean deliveryReceipt;

    public MqttChatMessage(String messageId, String message, String senderUid, ChatConstant.ChatType chatType, boolean deliveryReceipt) {
        this.messageId = messageId;
        this.message = message;
        this.senderUid = senderUid;
        this.chatType = chatType;
        this.deliveryReceipt = deliveryReceipt;
    }

}
