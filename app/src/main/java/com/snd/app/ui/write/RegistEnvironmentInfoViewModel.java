package com.snd.app.ui.write;

import androidx.databinding.ObservableField;

import com.snd.app.common.TMViewModel;

public class RegistEnvironmentInfoViewModel extends TMViewModel {
    private MyCallback myCallback;
    public ObservableField<String> idHex=new ObservableField<>();

    // 등록 버튼
    public void regist(){
        myCallback.onCustomCallback();
    }

    // 콜백 세팅
    public void setCallback(MyCallback myCallback) {
        this.myCallback = myCallback;
    }

}
