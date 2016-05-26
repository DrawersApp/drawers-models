package com.drawers.dao.message;

public enum CallType {
    Incoming,
    Outgoing,
    MissedOutGoing,
    MissedIncoming,
    Rejected,
    FallBackP2P // failure
}
