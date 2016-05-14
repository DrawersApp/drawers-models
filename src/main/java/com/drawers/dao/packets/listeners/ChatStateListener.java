package com.drawers.dao.packets.listeners;

import com.drawers.dao.packets.ChatState;

/**
 * Created by harshit on 13/5/16.
 */
public interface ChatStateListener {
    public void notifyActiveListener(ChatState.ChatStateValues state, String chatId);
}
