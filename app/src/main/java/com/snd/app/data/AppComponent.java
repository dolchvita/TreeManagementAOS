package com.snd.app.data;

import android.content.SharedPreferences;

import androidx.lifecycle.ViewModel;

import com.snd.app.data.user.SharedPreferencesManager;
import com.snd.app.domain.UserDTO;
import com.snd.app.sharedPreferences.SharedApplication;
import com.snd.app.sharedPreferences.SharedModule;
import com.snd.app.ui.home.HomeViewModel;
import com.snd.app.ui.login.LoginActivity;
import com.snd.app.ui.tree.TreeActivity;
import com.snd.app.ui.write.RegistTreeBasicInfoActivity;

import javax.inject.Singleton;
import dagger.Component;

@Component(modules = {AppModule.class})
@Singleton
public interface AppComponent {

    HomeViewModel homeViewModel();      //의존성 주입 성공

    // 커스텀 어플리케이션
    UserDTO userDTO();

    SharedPreferencesManager sharedPreferencesManager();

    void inject(HomeViewModel viewModel);
    void inject(LoginActivity loginActivity);
    void inject(RegistTreeBasicInfoActivity registTreeBasicInfoActivity);


    @Component.Builder
    interface Builder {
        Builder appModule(AppModule appModule);
        AppComponent build();
    }


}
