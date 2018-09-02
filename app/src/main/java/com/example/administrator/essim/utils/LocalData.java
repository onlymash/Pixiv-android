package com.example.administrator.essim.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

public class LocalData {

    private static final String FILE_NAME = "local_dns";

    public static SharedPreferences getDnsDataSet(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp;
    }

    public static void saveDns(Context context, String value) {
        SharedPreferences sp = getDnsDataSet(context);
        String position = String.valueOf(sp.getAll().size());

        SharedPreferences.Editor editor = sp.edit();
        editor.putString(position, value);
        editor.apply();
    }

    public static Map<String, ?> getAllDns(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.getAll();
    }

    public static void clearDns(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }
}
