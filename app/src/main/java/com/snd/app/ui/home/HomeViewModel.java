package com.snd.app.ui.home;

import android.util.Log;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.snd.app.data.AppComponent;
import com.snd.app.data.AppModule;
import com.snd.app.data.DaggerAppComponent;
import com.snd.app.domain.UserDTO;

public class HomeViewModel extends ViewModel {
    public static ViewModelProvider.Factory Factory;
    private String TAG= this.getClass().getName();

    // 사용자 정보를 가져와서 사용할 곳
    public ObservableField<String> company = new ObservableField<>();
    public ObservableField<String> name = new ObservableField<>();


    public HomeViewModel() {
        Log.d(TAG,"** 뷰모델 생성 **");


        // 컴포넌트 주입
        AppComponent appComponent = DaggerAppComponent.builder().appModule(new AppModule()).build();
      // user=appComponent.userRepositoryImpl().getUser();

        setUserInfo();
    }


    public void setUserInfo(){
        Log.d(TAG,"** 유저 인포 ** ");

        company.set("테스트");

    }


}
