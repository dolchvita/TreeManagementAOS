package com.snd.app.ui.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.snd.app.MainActivity;
import com.snd.app.R;
import com.snd.app.data.AppComponent;
import com.snd.app.data.AppModule;
import com.snd.app.data.DaggerAppComponent;
import com.snd.app.databinding.MainHomeFrBinding;


public class HomeFragment extends Fragment {
    MainHomeFrBinding homeFrBinding;
    //MainActivity mainActivity;
    HomeViewModel homeVM;
    SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AppComponent appComponent = DaggerAppComponent.builder().appModule(new AppModule(getContext())).build();
        homeVM=appComponent.homeViewModel();
        //프레그먼트가 사용할 xml 파일
        homeFrBinding=DataBindingUtil.inflate(inflater, R.layout.main_home_fr, container, false);
        homeFrBinding.setLifecycleOwner(this);
        homeFrBinding.setHomeVM(homeVM);    //홈뷰모델 연동
        return homeFrBinding.getRoot();
    }


}
