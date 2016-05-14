package com.drawers.dao.packets.listeners;

import com.drawers.dao.mqttinterface.PublisherImpl;
import com.drawers.dao.packets.group.GroupMembers;

/**
 * Created by harshit on 13/5/16.
 */
public interface GroupMembersListener {
    public void groupMemberAdded(GroupMembers.GroupMembersList groupMembersList, String topic, PublisherImpl publisher);
}
