package com.drawers.dao.packets.listeners;

import com.drawers.dao.message.GroupMessageContainer;
import com.drawers.dao.packets.group.GroupMessage;

/**
 * Created by harshit on 14/5/16.
 */
public interface GroupMessageListener {
    void messageSendAck(GroupMessageContainer groupMessageContainer);
    public void receiveMessage(final GroupMessageContainer groupMessageContainer, final String topic);

}
