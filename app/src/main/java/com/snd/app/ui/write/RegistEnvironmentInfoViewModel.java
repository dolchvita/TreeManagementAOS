package com.snd.app.ui.write;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.snd.app.common.TMViewModel;

public class RegistEnvironmentInfoViewModel extends TMViewModel {
    private MyCallback myCallback;
    public ObservableField<String> idHex=new ObservableField<>();
    private MutableLiveData _back=new MutableLiveData<>();
    public LiveData back=getBack();

    // 등록 버튼
    public void regist(){
        myCallback.onCustomCallback();
    }

    // 콜백 세팅
    public void setCallback(MyCallback myCallback) {
        this.myCallback = myCallback;
    }

    // 뒤로 가기
    public void setBack(){
        // 여기서 문자를 다르게 주고 상황을 정의해보자

        _back.setValue("click");
    }

    public LiveData getBack(){
        return _back;
    }

}
