package com.snd.app.data.user;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;

// 정보를 저장하는 객체
public class SharedPreferencesManager {
    private SharedPreferences sharedPreferences;

    @Inject
    public SharedPreferencesManager(Context context) {
        sharedPreferences= context.getSharedPreferences("sharedUser", Activity.MODE_PRIVATE);
    }

    // 정보 저장하기
    public void saveUserInfo(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(key, value);
        editor.apply();
    }

    // 정보 꺼내 읽기
    public String getUserInfo(String key, String defaultValue){
        return sharedPreferences.getString(key, defaultValue);
    }


}
