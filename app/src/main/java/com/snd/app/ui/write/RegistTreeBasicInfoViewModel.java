package com.snd.app.ui.write;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.snd.app.common.LocationViewModel;
import com.snd.app.domain.tree.TreeBasicInfoDTO;

import java.util.ArrayList;

import kotlin.Unit;

public class RegistTreeBasicInfoViewModel extends LocationViewModel {
    private String TAG=this.getClass().getName();

    public ObservableField<String> NFC=new ObservableField<>();
    public ObservableField<String> species=new ObservableField<>();
    public ObservableField<String> submitter=new ObservableField<>();
    public ObservableField<String> vendor=new ObservableField<>();

    public ObservableField<String> latitude=new ObservableField<>();
    public ObservableField<String> longitude=new ObservableField<>();

    private MutableLiveData _addPhoto;  // setter
    LiveData addPhoto=getAddPhoto();    // getter(결과)

    private Callback callback;

   private  MutableLiveData<ArrayList> _imageList;
   LiveData<ArrayList> imageList;


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


    public void setAddPhoto(){
        _addPhoto.setValue("test");
    }

    public LiveData getAddPhoto(){
        if(_addPhoto==null){
            _addPhoto=new MutableLiveData<String>();
        }
        return this._addPhoto;
    }



}