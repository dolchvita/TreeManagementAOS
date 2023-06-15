package com.snd.app.data;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.snd.app.domain.UserDTO;
import com.snd.app.ui.home.HomeViewModel;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    // 서버와 통신시에 데이터를 전달해줄 객체 (로그인시 사용)
    public static RequestQueue requestQueue;
    private Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    // HomeFragment 와 연결되는 뷰모델 관리
   @Provides
   HomeViewModel provideHomeViewModel(){
       //return new HomeViewModel(application,SharedPreferencesManager.getInstance(application));
       return new HomeViewModel(context);
   };


    @Provides
    @Singleton
    UserDTO provideUserDTO(){
        return new UserDTO();
    }


}
