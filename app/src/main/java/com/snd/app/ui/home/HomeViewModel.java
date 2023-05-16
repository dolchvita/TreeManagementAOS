package com.snd.app.ui.home;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.snd.app.data.AppComponent;
import com.snd.app.data.AppModule;
import com.snd.app.data.DaggerAppComponent;
import com.snd.app.databinding.MainHomeFrBinding;
import com.snd.app.domain.UserDTO;
import com.snd.app.sharedPreferences.SharedApplication;
import com.snd.app.data.user.SharedPreferencesManager;
import com.snd.app.sharedPreferences.SharedModule;

import javax.inject.Inject;

public class HomeViewModel extends ViewModel {
    public static ViewModelProvider.Factory Factory;
    private String TAG= this.getClass().getName();

    // 사용자 정보를 가져와서 사용할 곳
    public ObservableField<String> company = new ObservableField<>();
    public ObservableField<String> name = new ObservableField<>();

    private SharedPreferences sharedPreferences;

    UserDTO userDTO;
   private MainHomeFrBinding homeFrBinding;

    private SharedPreferencesManager sharedPreferencesManager;

    @Inject
    public HomeViewModel(SharedApplication application, SharedPreferencesManager manager) {
        AppComponent appComponent=DaggerAppComponent.builder().appModule(new AppModule(new SharedApplication())).build();

        userDTO=appComponent.userDTO();

        this.sharedPreferencesManager = manager;

        // 결국....

        sharedPreferences = manager.getSharedPreferences();
        Log.d(TAG,"**뷰모델 확인에서디티오 확인 **"+userDTO);

        Log.d(TAG,"**뷰모델 확인에서디티오 회사 확인 **"+userDTO.getCompany());

        //

        setUserInfo();
    }


    public void setSharedPreferencesManager(SharedPreferencesManager manager) {
        // 여기서 객체를 매개변수로 주입하는 건 지양하기...

        sharedPreferences = manager.getSharedPreferences();
        Log.d(TAG,"**뷰모델 확인!! **"+sharedPreferences);

        String company1=manager.getUserInfo("company",null);
        String company2=sharedPreferences.getString("company",null);

        Log.d(TAG,"*회사이름 - 뷰모대ㅔㄹ*"+company1);
        Log.d(TAG,"*회사이름 - 뷰모대ㅔㄹ*"+company2);


        userDTO.setCompany(company1);
        Log.d(TAG,"** 디티오 확인 **"+userDTO);
        Log.d(TAG,"** 디티오 확인 **"+userDTO.getCompany());


        company.set(company1);
        company.set("dddd");
        setUserInfo();
    }


    // 바인딩 클래스와 연결하는 메서드
    public void setBinding(MainHomeFrBinding binding) {
        this.homeFrBinding = binding;
    }


    public void setUserInfo(){
        //Log.d(TAG,"** 유저 인포 ** ");

        Log.d(TAG,"** 유저 인포 ** "+userDTO.getCompany());
        company.set(userDTO.getCompany());
    }




}
