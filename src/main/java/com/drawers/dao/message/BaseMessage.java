package com.drawers.dao.message;


import java.io.Serializable;

public interface BaseMessage extends Serializable {
    String toString();

    String toJsonString();
}
