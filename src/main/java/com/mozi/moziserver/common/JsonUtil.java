package com.mozi.moziserver.common;

import com.google.gson.Gson;

public class JsonUtil {

    private static final Gson gson = new Gson();

    public static String toJson(Object src) {
        return gson.toJson(src);
    }
}