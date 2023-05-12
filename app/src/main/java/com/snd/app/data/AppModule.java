package com.snd.app.data;

import com.android.volley.RequestQueue;
import com.snd.app.domain.UserDTO;
import com.snd.app.ui.home.HomeViewModel;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    // 서버와 통신시에 데이터를 전달해줄 객체 (로그인시 사용)
    public static RequestQueue requestQueue;

   @Provides
   HomeViewModel provideHomeViewModel(){
       return new HomeViewModel();
   };


}
