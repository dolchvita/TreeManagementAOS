package com.snd.app.data.user;

import com.snd.app.domain.UserDTO;

import dagger.Module;
import dagger.Provides;

// 메서드 제공이나 객체 생서
@Module
public class UserRepositoryModule {

    // 멤버변수
    private UserDTO userDTO;


    // 생성자
    public UserRepositoryModule(UserDTO userDTO) {
        this.userDTO = userDTO;
    }


    @Provides
    UserDTO provideUserDTO() {
        return userDTO;
    }


}
