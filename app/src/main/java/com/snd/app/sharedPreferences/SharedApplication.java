package com.snd.app.sharedPreferences;

import android.app.Application;

import com.snd.app.data.AppComponent;
import com.snd.app.data.AppModule;
import com.snd.app.data.DaggerAppComponent;

// SharedPreference를 가져오기 위한 어플리케이션 객체
public class SharedApplication extends Application {

    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        AppModule appModule=new AppModule(this);
        appComponent= DaggerAppComponent.builder().appModule(appModule).build();

    }

    public AppComponent getAppComponent(){
        return appComponent;
    }

}
