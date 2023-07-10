package com.snd.app.ui.read;

import android.util.Log;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.snd.app.common.TMViewModel;
import com.snd.app.domain.tree.TreeIntegratedVO;

public class GetEnvironmentViewModel extends TMViewModel {
    public MutableLiveData<TreeIntegratedVO> environmentData;
    TreeIntegratedVO treeIntegratedVO;

    public GetEnvironmentViewModel() {
        environmentData=new MediatorLiveData<>();
        treeIntegratedVO=TreeIntegratedVO.getInstance();


        setEnvironmentData();
    }

    public void setEnvironmentData() {
        environmentData.setValue(treeIntegratedVO);
    }


}
