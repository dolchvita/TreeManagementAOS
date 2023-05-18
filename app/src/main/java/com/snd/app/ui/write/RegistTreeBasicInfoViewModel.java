package com.snd.app.ui.write;

import android.util.Log;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.snd.app.domain.tree.TreeBasicInfoDTO;


public class RegistTreeBasicInfoViewModel extends ViewModel {
    private String TAG=this.getClass().getName();


    public ObservableField<String> NFC=new ObservableField<>();
    public ObservableField<String> species=new ObservableField<>();
    public ObservableField<String> submitter=new ObservableField<>();
    public ObservableField<String> vendor=new ObservableField<>();


// 2 세팅된 디티오 받아서 변수 대입?
    public void setTextViewModel(TreeBasicInfoDTO treeBasicInfoDTO){
        // 여기가 null
        NFC.set("test");
        species.set("test");
        submitter.set("test");
        vendor.set("test");
    }



}
