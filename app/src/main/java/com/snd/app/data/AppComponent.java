package com.snd.app.data;

import androidx.lifecycle.ViewModel;

import com.snd.app.sharedPreferences.SharedModule;
import com.snd.app.ui.home.HomeViewModel;
import com.snd.app.ui.login.LoginActivity;

import javax.inject.Singleton;
import dagger.Component;

@Component(modules = {AppModule.class, SharedModule.class})
@Singleton
public interface AppComponent {

    HomeViewModel homeViewModel();      //의존성 주입 성공

    void inject(HomeViewModel viewModel);

    @Component.Builder
    interface Builder {
        Builder appModule(AppModule appModule);
        Builder shareModule(SharedModule sharedModule);

        AppComponent build();
    }


}
