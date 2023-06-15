package com.snd.app.ui.map;


import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.snd.app.common.TMViewModel;
import com.snd.app.domain.tree.TreeLocationInfoDTO;

import java.util.ArrayList;

public class MapViewModel extends TMViewModel {

    // 위성 개수 세기 - 이건 액티비티가 아니라 뷰모델이 가지고 있기
    public MutableLiveData<Integer> _satellites = new MutableLiveData<>();
    // 위치 정보 - 변하는 변수임을 감지 !
    public ObservableField<String> _latitude=new ObservableField<>();
    public ObservableField<String>  _longitude=new ObservableField<>();

    public LiveData getSatellites(){
        return _satellites;
    }
    public ObservableField getLatitude(){
        return _latitude;
    }
    public ObservableField getLongitude(){
        return _longitude;
    }

}
