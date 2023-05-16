package com.snd.app.data.user;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.snd.app.data.AppComponent;
import com.snd.app.data.AppModule;
import com.snd.app.data.DaggerAppComponent;
import com.snd.app.domain.UserDTO;
import com.snd.app.sharedPreferences.SharedApplication;
import com.snd.app.ui.home.HomeViewModel;

import javax.inject.Inject;

// 정보를 저장하는 객체
public class SharedPreferencesManager {
    private String TAG=this.getClass().getName();

    //매니저 객체를 Static
    private static SharedPreferencesManager instance;
    private SharedPreferences sharedPreferences;

    @Inject
    UserDTO userDTO;
    @Inject
    HomeViewModel homeViewModel;

    // 쉐어드 객체 매개변수로  받기
    public SharedPreferencesManager(Context context) {
        Log.d(TAG, "** 쉐어드 객체 확인 - 생성자** "+sharedPreferences);

        AppComponent appComponent = DaggerAppComponent.builder().appModule(new AppModule(new SharedApplication())).build();
        // 주입할 유저 객체 사용
        userDTO=appComponent.userDTO();

    }


    public static synchronized SharedPreferencesManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferencesManager(context);
        }
        return instance;
    }


    // sharedUser라는 이름의 쉐어드 객체 반환
    // 현재 null
    public SharedPreferences getSharedPreferences(){
        Log.d(TAG, "** 쉐어드 객체 확인 - get 메서드 ** "+sharedPreferences);
        return sharedPreferences;
    }

    public void setSharedPreferences(SharedPreferences sharedPreferences){
        Log.d(TAG, "** Manager - set ** "+sharedPreferences);

        this.sharedPreferences=sharedPreferences;
    }


    // 정보 저장하기
    public void saveUserInfo(String key, String value) {
        Log.d(TAG, "** 쉐어드 객체 확인 - save 메서드 ** "+sharedPreferences);


        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();

        // 테스트 잘 통과됨

        Log.d(TAG, "**나왔나요!! ** "+ userDTO);


        userDTO.setCompany(sharedPreferences.getString(key,""));

        getUserInfo(key, value);
    }


    public UserDTO getUserDTO(){
        return userDTO;
    }


    // 정보 꺼내 읽기
    public String getUserInfo(String key, String defaultValue){
   //       Log.d(TAG, "**getInfo 에서 테스트 중 ** "+sharedPreferences.getString(key,""));
            Log.d(TAG, "** getInfo - UserDTO ** "+ userDTO.getCompany());

            // 널!!!!
        return sharedPreferences.getString(key, defaultValue);
    }

}
