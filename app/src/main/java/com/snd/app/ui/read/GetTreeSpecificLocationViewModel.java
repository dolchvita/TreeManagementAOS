package com.snd.app.ui.read;

import androidx.databinding.ObservableField;

import com.snd.app.common.TMViewModel;
import com.snd.app.domain.tree.TreeIntegratedVO;


public class GetTreeSpecificLocationViewModel extends TMViewModel {

    // 기본 정보
    public ObservableField<String> NFC=new ObservableField<>();
    public ObservableField<Boolean> sidewalk=new ObservableField<>();
    public ObservableField<Double> distance=new ObservableField<>();
    public ObservableField<Integer> carriageway=new ObservableField<>();


    // 데이터바인딩시 참조할 변수 매핑
    public void setTextViewModel(TreeIntegratedVO treeIntegratedVO){
        NFC.set(treeIntegratedVO.getNFC());
        sidewalk.set(treeIntegratedVO.getSidewalk());
        distance.set(treeIntegratedVO.getDistance());
        carriageway.set(treeIntegratedVO.getCarriageway());
    }


}
