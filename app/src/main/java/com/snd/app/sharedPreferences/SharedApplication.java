package com.snd.app.sharedPreferences;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.snd.app.data.AppComponent;
import com.snd.app.data.AppModule;
import com.snd.app.data.DaggerAppComponent;
import com.snd.app.data.user.SharedPreferencesManager;

// SharedPreference를 가져오기 위한 어플리케이션 객체
    // 이 객체를 반환하면 되는 것 아닌가?
public class SharedApplication extends Application {
    private String TAG=this.getClass().getName();

    private static AppComponent appComponent;
    //private SharedPreferences sharedPreferences;
    SharedPreferencesManager sharedPreferencesManager;

    @Override
    public void onCreate() {
        super.onCreate();

        // 모듈이 있는 이유는?
        AppModule appModule=new AppModule(this);
        appComponent= DaggerAppComponent.builder().appModule(appModule).build();

    }


    // 어플리케이션을 통해서 생성한 쉐어드프리퍼런스를 반환 !!
    public AppComponent getAppComponent(){
        return appComponent;
    }

}
