package com.snd.app.sharedPreferences;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
@Module
public class SharedModule {
    private Application application;

    public SharedModule(Application application) {

        //생성자 주입으로 초기화
        this.application=application;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return application;
    }

    @Provides
    @Singleton
    public Context provideSharedPreferences() {
        return application;
    }
}
