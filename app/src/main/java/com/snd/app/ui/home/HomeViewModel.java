package com.snd.app.ui.home;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.snd.app.data.AppComponent;
import com.snd.app.data.AppModule;
import com.snd.app.data.DaggerAppComponent;
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

    SharedPreferencesManager sharedPreferencesManager;

    @Inject
    Context context;

    @Inject
    public HomeViewModel(Application application) {
        this.context = context;
        Log.d(TAG,"** 뷰모델 생성 **");


        /*-------------------------------------------*/
        DaggerAppComponent.builder().shareModule(new SharedModule((Application) context.getApplicationContext())).build().inject(this);
        /*-------------------------------------------*/



        //sharedPreferencesManager=new SharedPreferencesManager(context);
        //String dd=sharedPreferencesManager.getUserInfo("company",null);

        Log.d(TAG,"** 문제 있?**");

        //setUserInfo();
    }


    public void setUserInfo(){
        Log.d(TAG,"** 유저 인포 ** ");


        //String test=sharedPreferencesManager.getUserInfo("company","");
        //Log.d(TAG,"** 캄패니 나와라 ** "+test);

        company.set("테스트");

    }


}
