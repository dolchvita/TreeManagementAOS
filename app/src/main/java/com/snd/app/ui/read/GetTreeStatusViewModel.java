package com.snd.app.ui.read;

import android.util.Log;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.snd.app.common.TMViewModel;
import com.snd.app.domain.tree.TreeIntegratedVO;

public class GetTreeStatusViewModel extends TMViewModel {
    public MutableLiveData<TreeIntegratedVO> stautsData;
    TreeIntegratedVO treeIntegratedVO;


    public GetTreeStatusViewModel() {
        stautsData=new MediatorLiveData<>();
        treeIntegratedVO=TreeIntegratedVO.getInstance();

        setStautsData();
    }

    public void setStautsData() {
        Log.d(TAG, "** 잘 넘어오나 확인 좀 ** "+treeIntegratedVO.getNFC());
        stautsData.setValue(treeIntegratedVO);
    }


}
