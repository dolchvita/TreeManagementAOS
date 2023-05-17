package com.snd.app.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.snd.app.MainViewModel;
import com.snd.app.R;
import com.snd.app.data.AppComponent;
import com.snd.app.data.AppModule;
import com.snd.app.data.DaggerAppComponent;
import com.snd.app.databinding.MainHomeFrBinding;
import com.snd.app.domain.UserDTO;
import com.snd.app.sharedPreferences.SharedApplication;

import javax.inject.Inject;

public class HomeFragment extends Fragment {

    // 바인딩 객체
    MainHomeFrBinding homeFrBinding;

    @Inject
    MainViewModel mainVM;
    HomeViewModel homeVM;
    @Inject
    UserDTO userDTO;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //프레그먼트가 사용할 xml 파일
        homeFrBinding= DataBindingUtil.inflate(inflater, R.layout.main_home_fr, container, false);
        homeFrBinding.setLifecycleOwner(this);

        // 앱 컴포넌트 - 의존성 주입으로 뷰 모델과 연동
        AppComponent appComponent = DaggerAppComponent.builder().appModule(new AppModule(new SharedApplication())).build();
        // 모듈 2개 이상 추가

        homeVM=appComponent.homeViewModel();
        homeFrBinding.setHomeVM(homeVM);    //홈뷰모델 연동

        return homeFrBinding.getRoot();
    }




}
