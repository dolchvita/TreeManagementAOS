package com.snd.app.ui.write;

import android.util.Log;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import com.snd.app.common.TMViewModel;
import com.snd.app.domain.tree.TreeStatusInfoDTO;

public class RegistTreeStatusInfoViewModel extends TMViewModel {

    public ObservableField<String> NFC=new ObservableField<>();
    public ObservableField<Double> DBH=new ObservableField<>();
    public ObservableField<Double> RCC=new ObservableField<>();
    public ObservableField<Double> height=new ObservableField<>();
    public ObservableField<Double> length=new ObservableField<>();
    public ObservableField<Double> width=new ObservableField<>();
    public ObservableField<Boolean> pest=new ObservableField<>();

    MutableLiveData<TreeStatusInfoDTO> treeStatusInfo = new MutableLiveData<>();
    private MyCallback myCallback;


    public void setTreeStatusInfo() {
        TreeStatusInfoDTO statusDTO=new TreeStatusInfoDTO();
    }

    // 등록 버튼
   public void regist(){
       Log.d(TAG, "** (뷰모델) 등록 버튼 누름 !**");
       myCallback.onCustomCallback();
   }


   // 콜백 세팅
    public void setCallback(MyCallback myCallback) {
        this.myCallback = myCallback;
    }



}
