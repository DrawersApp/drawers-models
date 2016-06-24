package com.drawers.dao.message;

import com.drawers.dao.ChatConstant;
import com.drawers.dao.utils.Singletons;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nishant.pathak on 19/06/16.
 */
public class GroupMessageContainer implements BaseMessage {

    @SerializedName("i")
    private String messageId;
    @SerializedName("m")
    private String message;
    @SerializedName("s")
    private String senderUid;

    public String getMessageId() {
        return messageId;
    }

    public String getMessage() {
        return message;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public ChatConstant.ChatType getChatType() {
        return chatType;
    }

    public GroupMessageContainer(String messageId, String message, String senderUid, ChatConstant.ChatType chatType) {
        this.messageId = messageId;
        this.message = message;
        this.senderUid = senderUid;
        this.chatType = chatType;
    }

    @SerializedName("c")
    public ChatConstant.ChatType chatType;

    @Override
    public String toJsonString() {
        return Singletons.singletonsInstance.gson.toJson(this);
    }

    public static GroupMessageContainer fromString(String json) {
        return Singletons.singletonsInstance.gson.fromJson(json, GroupMessageContainer.class);
    }



}
