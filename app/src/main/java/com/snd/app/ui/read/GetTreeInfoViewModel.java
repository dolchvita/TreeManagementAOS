package com.snd.app.ui.read;

import android.util.Log;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.snd.app.common.TMViewModel;
import com.snd.app.domain.tree.TreeBasicInfoDTO;
import com.snd.app.domain.tree.TreeLocationInfoDTO;

public class GetTreeInfoViewModel extends TMViewModel {
    private String TAG=this.getClass().getName();


    private MutableLiveData _back=new MutableLiveData<>();
    public LiveData back=getBack();



    public void setBack(){
        _back.setValue("click");
    }
    public LiveData getBack(){
        return _back;
    }


}
