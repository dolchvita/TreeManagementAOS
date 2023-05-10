package com.snd.app.data.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.snd.app.domain.UserDTO;

public class UserSession {

    private static PreferenceManager instance;
    private SharedPreferences sharedPreferences;

    public UserSession(Context context) {
        sharedPreferences=context.getApplicationContext().getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
    }

    public static synchronized PreferenceManager getInstance(Context context) {
        if (instance == null) {
            instance = new PreferenceManager(context);
        }
        return instance;
    }

    /*
    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences=getSharedPreferences("UserPrefs",Context.MODE_PRIVATE);

        SharedPreferences.Editor editor=sharedPreferences.edit();

        UserDTO userDTO=new UserDTO();

        Gson gson=new Gson();

        editor.putString("id", userDTO.getId());
        editor.putString("password", userDTO.getPassword());
        editor.putString("name", userDTO.getName());
        editor.putString("company", userDTO.getCompany());
        editor.putString("position", userDTO.getPosition());
        editor.putString("phone", userDTO.getPhone());
        editor.putString("email", userDTO.getEmail());
        editor.putString("authority", userDTO.getAuthority());
        //editor.put("inserted", userDTO.getInserted());   일단 보류
        editor.putBoolean("certification", userDTO.isCertification());

        editor.apply();     // 변경 사항 저장
    }*/


    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }


}
