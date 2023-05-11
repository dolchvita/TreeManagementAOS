package com.snd.app.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.snd.app.MainActivity;
import com.snd.app.MainViewModel;
import com.snd.app.R;
import com.snd.app.databinding.MainHomeFrBinding;

import javax.inject.Inject;

public class HomeFragment extends Fragment {

    // 바인딩 객체
    MainHomeFrBinding homeFrBinding;

    @Inject
    MainViewModel mainVM;
    @Inject
    HomeViewModel homeVM;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //프레그먼트가 사용할 xml 파일
        homeFrBinding= DataBindingUtil.inflate(inflater, R.layout.main_home_fr, container, false);

        homeVM=new ViewModelProvider(this).get(HomeViewModel.class);
        homeFrBinding.setHomeVM(homeVM);
        homeVM=homeFrBinding.getHomeVM();   // 홈뷰모델 연동

        homeFrBinding.setLifecycleOwner(this);

        return homeFrBinding.getRoot();
    }


}
