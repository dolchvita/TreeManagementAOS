package com.snd.app.ui.read;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.snd.app.common.TMViewModel;
import com.snd.app.domain.tree.TreeIntegratedVO;


public class GetTreeSpecificLocationViewModel extends TMViewModel {

    // 기본 정보
    public MutableLiveData<TreeIntegratedVO> locationData;
    // 공유되어야 하는 객체.
    TreeIntegratedVO treeIntegratedVO;

    public GetTreeSpecificLocationViewModel() {
        locationData=new MutableLiveData<>();
        treeIntegratedVO=TreeIntegratedVO.getInstance();

        setUserInfo(treeIntegratedVO);
    }


    public void setUserInfo(TreeIntegratedVO treeIntegratedVO) {
        Log.d(TAG, "휴                   "+treeIntegratedVO);
        locationData.setValue(treeIntegratedVO);
    }


    
}