package com.drawers.dao;


import com.drawers.dao.message.BaseMessage;
import com.drawers.dao.utils.Singletons;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nishant.pathak on 01/05/16.
 */
public class QAResponseContainer implements BaseMessage {
    @SerializedName("qar")
    private List<QAResponse> qaResponses = new ArrayList<>(10);
    @SerializedName("m")
    private String more;

    public List<QAResponse> getQaResponses() {
        return qaResponses;
    }

    public void setQaResponses(List<QAResponse> qaResponses) {
        this.qaResponses = qaResponses;
    }

    public String getMore() {
        return more;
    }

    public void setMore(String more) {
        this.more = more;
    }

    public String toJsonString() {
        return Singletons.singletonsInstance.gson.toJson(this);
    }

    public static QAResponseContainer fromString(String json) {
        return Singletons.singletonsInstance.gson.fromJson(json, QAResponseContainer.class);
    }

    public QAResponseContainer(List<QAResponse> qaResponses, String more) {
        this.qaResponses = qaResponses;
        this.more = more;
    }

}
