package com.snd.app.ui.read;

import androidx.databinding.ObservableField;

import com.snd.app.common.LocationViewModel;
import com.snd.app.domain.tree.TreeBasicInfoDTO;

public class GetTreeInfoViewModel extends LocationViewModel {
    private String TAG=this.getClass().getName();
    // 기본 정보
    public ObservableField<String> NFC=new ObservableField<>();
    public ObservableField<String> species=new ObservableField<>();
    public ObservableField<String> submitter=new ObservableField<>();
    public ObservableField<String> vendor=new ObservableField<>();


    // 데이터바인딩시 참조할 변수 매핑
    public void setTextViewModel(TreeBasicInfoDTO treeBasicInfoDTO){
        NFC.set(treeBasicInfoDTO.getNFC());
        species.set(treeBasicInfoDTO.getSpecies());
        submitter.set(treeBasicInfoDTO.getSubmitter());
        vendor.set(treeBasicInfoDTO.getVendor());
    }

}
