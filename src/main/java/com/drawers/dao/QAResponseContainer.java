package com.drawers.dao;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nishant.pathak on 01/05/16.
 */
public class QAResponseContainer {
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
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static QAResponseContainer fromString(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, QAResponseContainer.class);
    }

    public QAResponseContainer(List<QAResponse> qaResponses, String more) {
        this.qaResponses = qaResponses;
        this.more = more;
    }

}
