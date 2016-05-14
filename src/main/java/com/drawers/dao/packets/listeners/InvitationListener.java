package com.drawers.dao.packets.listeners;

import com.drawers.dao.mqttinterface.PublisherImpl;
import com.drawers.dao.packets.group.Invitation;

/**
 * Created by harshit on 14/5/16.
 */
public interface InvitationListener {
    public void invited(Invitation.InvitationInfo invitationInfo, PublisherImpl publisher);
}
