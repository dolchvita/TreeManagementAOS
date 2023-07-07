package com.snd.app.ui.read;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.snd.app.common.TMViewModel;
import com.snd.app.domain.tree.TreeIntegratedVO;


public class GetTreeSpecificLocationViewModel extends TMViewModel {

    // 기본 정보
    private MutableLiveData<String> _nfc;
    // 공유되어야 하는 객체.
    TreeIntegratedVO treeIntegratedVO;

    public GetTreeSpecificLocationViewModel() {
        _nfc=new MutableLiveData<>();

        treeIntegratedVO=new TreeIntegratedVO();
        treeIntegratedVO.setNFC("tq");
        setUserInfo(treeIntegratedVO);
    }


    public void setUserInfo(TreeIntegratedVO treeIntegratedVO) {
        _nfc.setValue(treeIntegratedVO.getNFC());
    }


    public LiveData<String> getNfc() {
        return _nfc;
    }

}
