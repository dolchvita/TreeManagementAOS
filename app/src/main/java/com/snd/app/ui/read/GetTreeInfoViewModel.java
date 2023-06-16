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
    // 기본 정보
    public ObservableField<String> NFC=new ObservableField<>();
    public ObservableField<String> species=new ObservableField<>();
    public ObservableField<String> submitter=new ObservableField<>();
    public ObservableField<String> vendor=new ObservableField<>();
    public ObservableField<String> latitude=new ObservableField<>();
    public ObservableField<String> longitude=new ObservableField<>();

    private MutableLiveData _back=new MutableLiveData<>();
    public LiveData back=getBack();

    // 데이터바인딩시 참조할 변수 매핑
    public void setTextViewModel(TreeBasicInfoDTO treeBasicInfoDTO, TreeLocationInfoDTO treeLocationInfoDTO){
        NFC.set(treeBasicInfoDTO.getNFC());
        species.set(treeBasicInfoDTO.getSpecies());
        submitter.set(treeBasicInfoDTO.getSubmitter());
        vendor.set(treeBasicInfoDTO.getVendor());

        Log.d(TAG,"**  **"+treeLocationInfoDTO.getLatitude());
        latitude.set(String.valueOf(treeLocationInfoDTO.getLatitude()));
        longitude.set(String.valueOf(treeLocationInfoDTO.getLongitude()));
    }


    public void setBack(){
        _back.setValue("click");
    }
    public LiveData getBack(){
        return _back;
    }


}
