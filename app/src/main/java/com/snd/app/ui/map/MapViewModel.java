package com.snd.app.ui.map;


import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.snd.app.common.TMViewModel;

public class MapViewModel extends TMViewModel {

    // 위성 개수 세기 - 이건 액티비티가 아니라 뷰모델이 가지고 있기

    public MutableLiveData<Integer> _satellites = new MutableLiveData<>();

    // 위치 정보
    public ObservableField<String> latitude=new ObservableField<>();
    public ObservableField<String> longitude=new ObservableField<>();

    public LiveData getSatellites(){
        return _satellites;
    }

    public ObservableField getLatitude(){
        return latitude;
    }

    public ObservableField getLongitude(){
        return longitude;
    }





}
