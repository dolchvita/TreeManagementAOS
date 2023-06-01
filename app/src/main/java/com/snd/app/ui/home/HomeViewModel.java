package com.snd.app.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.snd.app.ui.tree.TreeActivity;
import javax.inject.Inject;

public class HomeViewModel extends ViewModel {
    public static ViewModelProvider.Factory Factory;
    private String TAG= this.getClass().getName();
    // 사용자 정보를 가져와서 사용할 곳
    public ObservableField<String> company = new ObservableField<>();
    public ObservableField<String> name = new ObservableField<>();
    SharedPreferences sharedPreferences;

    @Inject
    public HomeViewModel(Context context) {
        // 공통의 객체 꺼내기
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        setUserInfo();
    }

    public void setUserInfo(){
        company.set(sharedPreferences.getString("company", null));
    }

    // 화면 변경
    public void onTextViewClicked(View view) {
        // 액티비티 변경을 위한 Intent 생성
        Intent intent = new Intent(view.getContext(), TreeActivity.class);
        // 액티비티 변경 로직
        view.getContext().startActivity(intent);
    }


}
