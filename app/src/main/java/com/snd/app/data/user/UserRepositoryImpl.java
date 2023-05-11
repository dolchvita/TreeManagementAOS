package com.snd.app.data.user;


import com.snd.app.domain.UserDTO;

// 이 객체는 싱글턴
public class UserRepositoryImpl implements UserRepository {

    @Override
    public UserDTO getUser() {
        return new UserDTO();
    }

}
