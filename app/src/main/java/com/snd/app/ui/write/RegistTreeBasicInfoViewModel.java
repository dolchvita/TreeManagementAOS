package com.snd.app.ui.write;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.snd.app.domain.tree.TreeBasicInfoDTO;

public class RegistTreeBasicInfoViewModel extends ViewModel {
    private String TAG=this.getClass().getName();

    public ObservableField<String> NFC=new ObservableField<>();
    public ObservableField<String> species=new ObservableField<>();
    public ObservableField<String> submitter=new ObservableField<>();
    public ObservableField<String> vendor=new ObservableField<>();

   private Callback callback;

    // 데이터바인딩시 참조할 변수 매핑
    public void setTextViewModel(TreeBasicInfoDTO treeBasicInfoDTO){
        NFC.set(treeBasicInfoDTO.getNFC());
        species.set(treeBasicInfoDTO.getSpecies());
        submitter.set(treeBasicInfoDTO.getSubmitter());
        vendor.set(treeBasicInfoDTO.getVendor());
    }

    public void regist(){
        // RegistTreeBasicInfoActivity 메서드 호출
        callback.onCallback();
    }


    public void setCallback(Callback callback) {
        this.callback = callback;
    }
    public void viewModelMethod() {
        if (callback != null) {
            callback.onCallback();
        }
    }

}
