package com.snd.app.ui.read;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.snd.app.common.TMViewModel;
import com.snd.app.ui.write.MyCallback;

public class GetTreeInfoViewModel extends TMViewModel {
    private String TAG=this.getClass().getName();
    private MyCallback myCallback;

    public void setCallback(MyCallback myCallback) {
        this.myCallback = myCallback;
    }

    // 수정 버튼
    public void modify(){
        myCallback.onCustomCallback();
    }

    private MutableLiveData _back=new MutableLiveData<>();
    public LiveData back=getBack();

    public void setBack(){
        _back.setValue("click");
    }
    public LiveData getBack(){
        return _back;
    }


}
