package com.drawers.dao.packets.listeners;

import com.drawers.dao.packets.group.GroupProfile;

/**
 * Created by harshit on 14/5/16.
 */
public interface GroupProfileListener {
    public void groupProfileChanged(GroupProfile.GroupProfileInfo groupProfile, String topic);
}
