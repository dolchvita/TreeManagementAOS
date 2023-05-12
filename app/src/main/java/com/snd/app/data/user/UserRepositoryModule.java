package com.snd.app.data.user;

import com.snd.app.domain.UserDTO;
import com.snd.app.ui.home.HomeViewModel;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class UserRepositoryModule {

    private UserDTO userDTO;

    //생성자
    public UserRepositoryModule(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    @Provides
    UserDTO provideUserDTO() {
        return userDTO;
    }

/*
    @Provides
    UserRepositoryImpl provideUserRepositoryImpl(){
        return new UserRepositoryImpl();
    }
*/
    /*
    @Provides
    HomeViewModel provideHomeViewModel(UserRepository userRepository){
        return new HomeViewModel(userRepository);
    }
     */


}
