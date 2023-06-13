package com.snd.app.ui.map;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.snd.app.common.TMViewModel;

public class MapViewModel extends TMViewModel {

    // 위성 개수 세기 - 이건 액티비티가 아니라 뷰모델이 가지고 있기
    public MutableLiveData<Integer> _satellites = new MutableLiveData<>();
    // 위치 정보
    public MutableLiveData<String> latitude=new MutableLiveData<>();
    public MutableLiveData<String> longitude=new MutableLiveData<>();


    public LiveData getSatellites(){
        return _satellites;
    }

    public LiveData getLatitude(){
        return latitude;
    }

    public LiveData getLongitude(){
        return longitude;
    }



}
