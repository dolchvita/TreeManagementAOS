package com.snd.app.data.user;

import android.content.Context;
import android.content.SharedPreferences;


public class TMSharedPreferences {

    private static final String PREF_NAME="PP";
    private static TMSharedPreferences instance;
    private SharedPreferences sharedPreferences;

    private TMSharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // 싱글턴
    public static synchronized TMSharedPreferences getInstance(Context context) {
        if (instance == null) {
            instance = new TMSharedPreferences(context);
        }
        return instance;
    }





   public SharedPreferences getSharedPreferences(){
        return sharedPreferences;
   }



}
