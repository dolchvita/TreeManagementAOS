package com.snd.app;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import dagger.Component;

public class MainViewModel extends ViewModel {
    private String TAG=this.getClass().getName();

    // 1 fragment 상수
    public static final int FRAGMENT_HOME=1;
    public static final int FRAGMENT_MAP=2;
    public static final int FRAGMENT_COMPLAINT=3;
    public static final int FRAGMENT_RECENT=4;

    private MutableLiveData _tabClick;  // setter
    LiveData tabClick=getTabClcick();    // getter(결과)

    public MainViewModel() {

    }

    public void setTabClick(Integer value){
        // 클릭된 수로 속성 변경하기
        _tabClick.setValue(value);
    }

    public LiveData getTabClcick(){
        Log.d(TAG,_tabClick+"클릭 감지????  ");

        if (_tabClick==null){
            _tabClick=new MutableLiveData();
        }
        // 결과값 반환해주는 메서드
        return this._tabClick;
    }

}
