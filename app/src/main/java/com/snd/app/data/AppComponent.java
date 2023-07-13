package com.snd.app.data;


import com.snd.app.ui.home.HomeViewModel;
import com.snd.app.ui.write.RegistTreeBasicInfoViewModel;
import com.snd.app.ui.write.RegisterCallbackImpl;
import com.snd.app.ui.write.WriteUseCase;


import javax.inject.Singleton;
import dagger.Component;

@Component(modules = {AppModule.class})
@Singleton
public interface AppComponent {

    HomeViewModel homeViewModel();      //의존성 주입 성공

    WriteUseCase writeUseCase();

    RegisterCallbackImpl registerCallbackImpl();

    RegistTreeBasicInfoViewModel registTreeBasicInfoViewModel();


    @Component.Builder
    interface Builder {
        Builder appModule(AppModule appModule);
        AppComponent build();
    }



}