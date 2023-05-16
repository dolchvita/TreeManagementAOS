package com.snd.app.data;

import androidx.lifecycle.ViewModel;

import com.snd.app.data.user.SharedPreferencesManager;
import com.snd.app.domain.UserDTO;
import com.snd.app.sharedPreferences.SharedApplication;
import com.snd.app.sharedPreferences.SharedModule;
import com.snd.app.ui.home.HomeViewModel;
import com.snd.app.ui.login.LoginActivity;

import javax.inject.Singleton;
import dagger.Component;

@Component(modules = {AppModule.class})
@Singleton
public interface AppComponent {

    HomeViewModel homeViewModel();      //의존성 주입 성공

    void inject(HomeViewModel viewModel);
    void inject(LoginActivity loginActivity);


    // 커스텀 어플리케이션
    SharedApplication sharedApplication();

    UserDTO userDTO();

    SharedPreferencesManager sharedPreferencesManager();


    @Component.Builder
    interface Builder {
        Builder appModule(AppModule appModule);
        AppComponent build();
    }


}
