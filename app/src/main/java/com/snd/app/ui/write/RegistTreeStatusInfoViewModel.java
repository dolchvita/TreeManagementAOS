package com.snd.app.ui.write;

import android.util.Log;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.snd.app.common.TMViewModel;
import com.snd.app.domain.tree.TreeStatusInfoDTO;

public class RegistTreeStatusInfoViewModel extends TMViewModel {
    private MyCallback myCallback;
    private MutableLiveData _back=new MutableLiveData<>();
    public LiveData back=getBack();
    public ObservableField<String> idHex=new ObservableField<>();

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
        _back.setValue("click");
    }
    public LiveData getBack(){
        return _back;
    }

}
