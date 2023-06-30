package com.snd.app.ui.write;


import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.snd.app.common.TMViewModel;

public class RegistTreeSpecificLocationInfoViewModel extends TMViewModel {
    private MyCallback myCallback;
    private MutableLiveData _back=new MutableLiveData<>();
    public LiveData back=getBack();
    public ObservableField<String> idHex=new ObservableField<>();


    public void setBack(){
        _back.setValue("goBasic");
    }
    public LiveData getBack(){
        return _back;
    }

    // 등록 버튼
    public void regist(){
        myCallback.onCustomCallback();
    }

    // 콜백 세팅
    public void setCallback(MyCallback myCallback) {
        this.myCallback = myCallback;
    }

}
