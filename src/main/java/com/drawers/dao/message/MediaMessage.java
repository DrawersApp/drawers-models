package com.drawers.dao.message;

import com.drawers.dao.utils.Singletons;

import java.io.Serializable;

public class MediaMessage implements BaseMessage, Serializable {

    private boolean contentUploaded;
    /* remote cloudinary name */
    private String name;
    /* remote cloudinary media type */
    private String type;
    /* upload or download status */
    private State state;
    /* local path */
    private String path;

    /* preserve real name of the file */
    private String rName;

    public MediaMessage(String name, String path, String type, String realName) {
        this.name = name;
        this.path = path;
        this.type = type;
        this.state = State.INIT;
        this.rName = realName;
    }

    public MediaMessage(String name, String path, String realName) {
        this.name = name;
        this.path = path;
        this.state = State.INIT;
        this.rName = realName;
    }


    public MediaMessage(MediaMessage mm) {
        this.name = mm.getName();
        this.path = null;
        this.type = mm.getType();
        this.rName = mm.getRealName();
        this.state = State.INIT;
    }

    public MediaMessage(String name, String path) {
        this.name = name;
        this.path = path;
        this.state = State.INIT;
    }

    public static MediaMessage fromString(String json) {
        return Singletons.singletonsInstance.gson.fromJson(json, MediaMessage.class);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getContentUploaded() {
        return contentUploaded;
    }

    public void setContentUploaded(boolean contentUploaded) {
        this.contentUploaded = contentUploaded;
    }

    public String getDisplayName() {
        return name != null && name.startsWith("rawFiles/") ? name.substring("rawFiles/".length(), name.length() - 36) : name;
    }

    public String getRealName() {
        return rName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public String toJsonString() {
        return Singletons.singletonsInstance.gson.toJson(this);
    }

    @Override
    public String toString() {
        return "MediaMessage{" +
            "contentUploaded=" + contentUploaded +
            ", name='" + name + '\'' +
            ", type='" + type + '\'' +
            ", state=" + state +
            ", path='" + path + '\'' +
            ", rName='" + rName + '\'' +
            '}';
    }

    public enum State {
        INIT,
        DOWNLOADING,
        UPLOADING,
        UPLOADED,
        SUCCESS,   // Send Successfully
        FAILURE
    }
}
