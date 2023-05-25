package com.snd.app.ui.write;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.snd.app.common.LocationViewModel;
import com.snd.app.domain.tree.TreeBasicInfoDTO;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;


public class RegistTreeBasicInfoViewModel extends LocationViewModel {
    private String TAG=this.getClass().getName();

    public ObservableField<String> NFC=new ObservableField<>();
    public ObservableField<String> species=new ObservableField<>();
    public ObservableField<String> submitter=new ObservableField<>();
    public ObservableField<String> vendor=new ObservableField<>();

    public ObservableField<String> latitude=new ObservableField<>();
    public ObservableField<String> longitude=new ObservableField<>();

    private MutableLiveData _camera;  // setter
    LiveData camera=getCamera();    // getter(결과)

    private Callback callback;

    // 액티비티와 어댑터가 가져갈 리스트
    // 실제 사진이 담기는 리스트
    private MutableLiveData<List<Bitmap>> _listData = new MutableLiveData<>();
    public LiveData listData=getImageList();

    public void setImageList(Bitmap bitmap) {
        // 추가된 리스트를 이미지 리스트에 세팅
        // 이게 시행되어야 getImageList에 변화가 생긴다.

        List<Bitmap> currentList = _listData.getValue();
        if (currentList == null) {
            currentList = new ArrayList<>();
        }
        currentList.add(bitmap);
        _listData.setValue(currentList);
    }


    public MutableLiveData<List<Bitmap>> getImageList() {
        return _listData;
    }

    // 데이터바인딩시 참조할 변수 매핑
    public void setTextViewModel(TreeBasicInfoDTO treeBasicInfoDTO){
        NFC.set(treeBasicInfoDTO.getNFC());
        species.set(treeBasicInfoDTO.getSpecies());
        submitter.set(treeBasicInfoDTO.getSubmitter());
        vendor.set(treeBasicInfoDTO.getVendor());
    }

    public void regist(){
        // RegistTreeBasicInfoActivity 메서드 호출
        callback.onCallback();
    }

    // 콜백 객체를 받아서 액티비티로부터 호출 가능하게 함
    public void setCallback(Callback callback) {
        this.callback = callback;
    }
    public void viewModelMethod() {
        if (callback != null) {
            callback.onCallback();
        }
    }

    public void setCamera(){
        _camera.setValue("test");
    }

    public LiveData getCamera(){
        if(_camera==null){
            _camera=new MutableLiveData<String>();
        }
        return this._camera;
    }


}
