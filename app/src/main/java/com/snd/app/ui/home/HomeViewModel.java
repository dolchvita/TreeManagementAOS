package com.snd.app.ui.home;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.snd.app.data.user.UserRepository;
import com.snd.app.domain.UserDTO;

import javax.inject.Inject;

public class HomeViewModel extends ViewModel {

    // 사용자 정보를 가져와서 사용할 곳
    public ObservableField<String> company = new ObservableField<>();
    public ObservableField<String> name = new ObservableField<>();


    private UserRepository userRepository;
    UserDTO user;

    @Inject
    public HomeViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;

        user=userRepository.getUser();
        init();
    }

    public void init(){
        company.set(user.getCompany());
        name.set(user.getName());

    }


}
