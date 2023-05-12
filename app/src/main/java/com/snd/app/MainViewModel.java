package com.snd.app;

import android.util.Log;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class MainViewModel extends ViewModel {
    private String TAG=this.getClass().getName();

    // 1 fragment 상수
    public static final int FRAGMENT_HOME=1;
    public static final int FRAGMENT_MAP=2;
    public static final int FRAGMENT_COMPLAINT=3;
    public static final int FRAGMENT_RECENT=4;

    private MutableLiveData<Integer> _tabClick;  // setter
    LiveData tabClick=getTabClcick();    // getter(결과)

   ObservableField<Integer> currentFragment=new ObservableField<Integer>(FRAGMENT_HOME);

    public void setTabClick(Integer value){
        // 클릭된 수로 속성 변경하기
        _tabClick.setValue(value);
        currentFragment.set(value);
    }

    public LiveData getTabClcick(){
        // 여기서 아래 변수가 null로 뜬다..
        Log.d(TAG,_tabClick+"클릭 감지????");

        if (_tabClick==null){
            _tabClick=new MutableLiveData<Integer>();
        }
        // 결과값 반환해주는 메서드
        return this._tabClick;
    }


}
