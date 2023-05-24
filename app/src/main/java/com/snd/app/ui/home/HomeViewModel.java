package com.snd.app.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

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
import com.snd.app.ui.tree.TreeActivity;

import javax.inject.Inject;

public class HomeViewModel extends ViewModel {
    public static ViewModelProvider.Factory Factory;
    private String TAG= this.getClass().getName();

    // 사용자 정보를 가져와서 사용할 곳
    public ObservableField<String> company = new ObservableField<>();
    public ObservableField<String> name = new ObservableField<>();

    private SharedPreferences sharedPreferences;
    private MainHomeFrBinding homeFrBinding;
    //private SharedPreferencesManager sharedPreferencesManager;

    @Inject
    UserDTO userDTO;

    // NFC 태그 관련


    @Inject
    public HomeViewModel(Context context, SharedPreferencesManager manager) {
        AppComponent appComponent=DaggerAppComponent.builder().appModule(new AppModule(new SharedApplication())).build();
        appComponent.inject(this);
        //this.sharedPreferencesManager = manager;

        // 결국....
        //sharedPreferences = appComponent.sharedPreferences();

        // 매니저에서 디티오 꺼내기
        UserDTO userDTO=manager.getUserDTO();
        Log.d(TAG,"**뷰모델 확인에서디티오 확인 **"+userDTO);
        Log.d(TAG,"**뷰모델 확인에서디티오 회사 확인 **"+userDTO.getCompany());

        setUserInfo(userDTO);
    }


    public void setUserInfo(UserDTO userDTO){
        //Log.d(TAG,"** 유저 인포 ** ");

        Log.d(TAG,"** 유저 인포 ** "+userDTO.getCompany());
        //company.set("뭐죵");
        company.set(userDTO.getCompany());
    }

    // 화면 변경
    public void onTextViewClicked(View view) {
        // 액티비티 변경을 위한 Intent 생성
        Intent intent = new Intent(view.getContext(), TreeActivity.class);

        // 액티비티 변경 로직
        view.getContext().startActivity(intent);
    }


}
