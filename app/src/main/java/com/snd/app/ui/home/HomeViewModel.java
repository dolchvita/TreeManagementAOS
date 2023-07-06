package com.snd.app.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.snd.app.ui.tree.TreeActivity;
import com.snd.app.ui.write.RegistTreeBasicInfoFragment;
import com.snd.app.ui.write.RegistTreeInfoActivity;

import javax.inject.Inject;

public class HomeViewModel extends ViewModel {
    public static ViewModelProvider.Factory Factory;
    private String TAG= this.getClass().getName();
    SharedPreferences sharedPreferences;
    private MutableLiveData<String> _company;
    private MutableLiveData<String> _name;


    @Inject
    public HomeViewModel(Context context) {
        // 공통의 객체 꺼내기
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        _company=new MutableLiveData<>();
        _name=new MutableLiveData<>();

        setUserInfo();
    }

    public void setUserInfo() {
        _company.setValue(sharedPreferences.getString("company", null));
        _name.setValue(sharedPreferences.getString("name", null));
    }

    public LiveData<String> getCompany() {
        return _company;
    }

    public LiveData<String> getName() {
        return _name;
    }



    // 수목 등록
    public void onTextViewClicked(View view) {
        Log.d(TAG,"** 넘어오는 뷰의 정체는? ** "+view);

        // 액티비티 변경을 위한 Intent 생성
        Intent intent = new Intent(view.getContext(), RegistTreeInfoActivity.class);
        // 액티비티 변경 로직
        //intent.putExtra("actName", "RegistTreeInfoActivity");
        //intent.putExtra("actVersion", "write");
        view.getContext().startActivity(intent);
    }

    // 수목 확인
    public void onCheckViewClicked(View view) {
        Log.d(TAG,"** 넘어오는 뷰의 정체는? ** "+view);
        // 액티비티 변경을 위한 Intent 생성
        Intent intent = new Intent(view.getContext(), TreeActivity.class);
        // 액티비티 변경 로직
        intent.putExtra("actName", "GetTreeInfoActivity");
        intent.putExtra("actVersion", "read");
        view.getContext().startActivity(intent);
    }

}
