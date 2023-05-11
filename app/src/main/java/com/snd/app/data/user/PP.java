package com.snd.app.data.user;

import android.content.Context;
import android.content.SharedPreferences;

public class PP {
    private static final String PREF_NAME="PP";
    private SharedPreferences sharedPreferences;

    public SharedPreferences getSharedPreferences(){
        return sharedPreferences;
    }

    // 유저 정보는 제이슨으로 한 번에 세팅하기
    public static String getUserInfo(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("userInfo", "");
    }
    public static void setUserInfo(Context context, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("userInfo", value).apply();
    }



}
