package com.akarpovich.interior.utils;

import com.google.gson.Gson;

import java.nio.charset.Charset;

public class ConversionUtils {

    private static final Gson gson = new Gson();
    private static final Charset UTF8 = Charset.forName("UTF-8");

    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    public static <T> T fromJson(String json, Class<T> aClass) {
        return gson.fromJson(json, aClass);
    }

    public static byte[] toUTF8Bytes(String data) {
        return data.getBytes(UTF8);
    }
    public static byte[] toUTF8Json(Object data) {
        return toJson(data).getBytes(UTF8);
    }
}
