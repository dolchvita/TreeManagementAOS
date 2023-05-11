package com.snd.app.data.user;

import dagger.Module;
import dagger.Provides;

@Module
public class UserRepositoryModule {

    @Provides
    UserRepository provideUserRepository(){
        return new UserRepositoryImpl();
    }

}
