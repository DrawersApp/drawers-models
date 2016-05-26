package com.drawers.dao.message;


import com.drawers.dao.utils.Singletons;

public class MapMessage implements BaseMessage {

    double Lat;
    double Long;

    public MapMessage(double lat, double aLong) {
        Lat = lat;
        Long = aLong;
    }

    public static MapMessage fromString(String json) {
        return Singletons.singletonsInstance.gson.fromJson(json, MapMessage.class);

    }

    public double getLat() {
        return Lat;
    }

    public double getLong() {
        return Long;
    }

    @Override
    public String toString() {
        return "MapMessage [" + "lat=" + Lat
                + ", long=" + Long + "]";
    }

    @Override
    public String toJsonString() {
        return Singletons.singletonsInstance.gson.toJson(this);
    }

}
