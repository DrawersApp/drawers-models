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

    private MetaData metaData = new MetaData();


    public static class MetaData implements BaseMessage, Serializable {
        /* preserve real name of the file */
        private String name = "";

        private long size = 0;

        MetaData() {}

        public MetaData(String name, long size) {
            this.name = name;
            this.size = size;
        }

        @Override
        public String toJsonString() {
            return Singletons.singletonsInstance.gson.toJson(this);
        }

        @Override
        public String toString() {
            return "MetaData{" +
                "name='" + name + '\'' +
                ", size=" + size +
                '}';
        }

        public String getName() {
            return name;
        }

        public long getSize() {
            return size;
        }
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public MediaMessage(String name, String path, String type, MetaData metaData) {
        this.name = name;
        this.path = path;
        this.type = type;
        this.state = State.INIT;
        this.metaData = metaData;
    }

    public MediaMessage(String name, String path, MetaData metaData) {
        this.name = name;
        this.path = path;
        this.state = State.INIT;
        this.metaData = metaData;
    }


    public MediaMessage(MediaMessage mm) {
        this.name = mm.getName();
        this.path = null;
        this.type = mm.getType();
        this.metaData = mm.getMetaData();
        this.state = State.INIT;
    }

    @Deprecated
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
            ", metaData=" + metaData +
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
