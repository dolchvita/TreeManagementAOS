package com.snd.app.common;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TMViewModel extends ViewModel {
   public String TAG = this.getClass().getName();
    // 위성 개수 세기 - 이건 액티비티가 아니라 뷰모델이 가지고 있기
    private MutableLiveData<Integer> satelliteCount;
    private MutableLiveData<String> accuracy;


    public LiveData<Integer> getSatelliteCount() {
        if (satelliteCount == null) {
            satelliteCount = new MutableLiveData<>();
        }
        return satelliteCount;
    }

    public void setSatelliteCount(int count) {
        if (satelliteCount == null) {
            satelliteCount = new MutableLiveData<>();
        }
        satelliteCount.setValue(count);
    }

    public LiveData<String> getAccuracy() {
        if (accuracy == null) {
            accuracy = new MutableLiveData<>();
        }
        return accuracy;
    }

    public void setAccuracy(String str) {
        if (accuracy == null) {
            accuracy = new MutableLiveData<>();
        }
        accuracy.setValue(str);
    }



}
