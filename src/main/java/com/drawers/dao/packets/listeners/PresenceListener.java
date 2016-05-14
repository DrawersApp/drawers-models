package com.drawers.dao.packets.listeners;

import com.drawers.dao.packets.Presence;

/**
 * Created by harshit on 13/5/16.
 */
public interface PresenceListener {
    public void notifyPresence(Presence.PresenceStatus presenceStatus, String userId, String topic);
}
