package com.snd.app.ui.read;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.snd.app.common.TMViewModel;
import com.snd.app.domain.tree.TreeIntegratedVO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GetTreeBasicInfoViewModel extends TMViewModel {

    // 기본 정보
    public ObservableField<String> NFC=new ObservableField<>();
    public ObservableField<String> species=new ObservableField<>();
    public ObservableField<String> submitter=new ObservableField<>();
    public ObservableField<String> vendor=new ObservableField<>();
    public ObservableField<String> latitude=new ObservableField<>();
    public ObservableField<String> longitude=new ObservableField<>();
    public ObservableField<String> basicInserted=new ObservableField<>();

    private MutableLiveData _gps_btn=new MutableLiveData();
    public LiveData gps_btn=getGPS();

    // 카메라 실행
    private MutableLiveData _camera;
    LiveData camera=getCamera();

    // 실제 사진이 담기는 리스트
    private MutableLiveData<List<Bitmap>> _listData = new MutableLiveData<>();
    public LiveData listData=getImageList();

    public void setGPS(){
        _gps_btn.setValue("click");
    }
    public LiveData getGPS(){
        return _gps_btn;
    }

    List<Bitmap> currentList;   // 실제 사진이 담겨있는 리스트

    // 데이터바인딩시 참조할 변수 매핑
    public void setTextViewModel(TreeIntegratedVO treeIntegratedVO){
        Log.d(TAG, "휴                   "+treeIntegratedVO);

        NFC.set(treeIntegratedVO.getNFC());
        species.set(treeIntegratedVO.getSpecies());
        submitter.set(treeIntegratedVO.getBasicSubmitter());
        vendor.set(treeIntegratedVO.getBasicVendor());
        latitude.set(String.valueOf(treeIntegratedVO.getLatitude()));
        longitude.set(String.valueOf(treeIntegratedVO.getLongitude()));
        basicInserted.set(getBasicInserted(treeIntegratedVO.getBasicInserted()));
    }


    public String getBasicInserted(LocalDateTime now) {
        Log.d(TAG, "** 전달받은 시간 ** "+now);
        String formatDateTime=null;
        if (now != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            formatDateTime = now.format(formatter);
        }else {
            formatDateTime="";
        }
        return formatDateTime;
    }


    public MutableLiveData<List<Bitmap>> getImageList() {
        return _listData;
    }


    public void setCamera(){
        _camera.setValue("click");
    }


    public LiveData getCamera(){
        if(_camera==null){
            _camera=new MutableLiveData<String>();
        }
        return this._camera;
    }

}