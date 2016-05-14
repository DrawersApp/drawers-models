package com.drawers.dao.utils;

import com.google.gson.Gson;

/**
 * Created by harshit on 13/5/16.
 */
public class Singletons {
    public final Gson gson = new Gson();
    public static final Singletons singletonsInstance = new Singletons();
}
