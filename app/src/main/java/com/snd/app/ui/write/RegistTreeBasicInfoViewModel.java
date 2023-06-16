package com.snd.app.ui.write;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.snd.app.common.TMViewModel;
import com.snd.app.domain.tree.TreeBasicInfoDTO;

import java.util.ArrayList;
import java.util.List;


public class RegistTreeBasicInfoViewModel extends TMViewModel {
    private String TAG=this.getClass().getName();
    // 기본 정보
    public ObservableField<String> NFC=new ObservableField<>();
    public ObservableField<String> species=new ObservableField<>();
    public ObservableField<String> submitter=new ObservableField<>();
    public ObservableField<String> vendor=new ObservableField<>();
    // 위치 정보
    public ObservableField<String> latitude=new ObservableField<>();
    public ObservableField<String> longitude=new ObservableField<>();
    // 카메라 실행
    private MutableLiveData _camera;
    LiveData camera=getCamera();
    // 저장버튼
    private MyCallback myCallback;
    // 액티비티와 어댑터가 가져갈 리스트
    // 실제 사진이 담기는 리스트
    private MutableLiveData<List<Bitmap>> _listData = new MutableLiveData<>();
    public LiveData listData=getImageList();
    // 사진 개수 표현하기
    public MutableLiveData<String> _imgCount=new MutableLiveData<>();
    public LiveData imgCount=getImgCount();
    public int cnt=0;
    List<Bitmap> currentList;   // 실제 사진이 담겨있는 리스트
    // 뒤로가기
    private MutableLiveData _back=new MutableLiveData<>();
    public LiveData back=getBack();

    public void setBack(){
        _back.setValue("click");
    }
    public LiveData getBack(){
        return _back;
    }
    public ObservableField getLatitude(){
        return latitude;
    }
    public ObservableField getLongitude(){
        return longitude;
    }

    // 생성자
    public RegistTreeBasicInfoViewModel() {
        setImgCount();
    }
    public void setImgCount(){
        _imgCount.setValue(cnt+"/2");
    }
   public LiveData getImgCount(){
       return _imgCount;
   }

   // 사진 추가하는 메서드
    public void setImageList(Bitmap bitmap) {
        // 추가된 리스트를 이미지 리스트에 세팅
        // 이게 시행되어야 getImageList에 변화가 생긴다.
        currentList = _listData.getValue();

        if (currentList == null) {
            currentList = new ArrayList<>();
        }

        if(currentList.size()<2){
            currentList.add(bitmap);
            _listData.setValue(currentList);
            setImgCount();
        }else{
            Log.d(TAG, "2개 이상 초과됨");
        }
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
        // xml에서 바로 호출
        // RegistTreeBasicInfoActivity 메서드 호출

        myCallback.onCustomCallback();
    }

    // 콜백 객체를 받아서 액티비티로부터 호출 가능하게 함
    public void setCallback(MyCallback myCallback) {
        this.myCallback = myCallback;
    }

    public void viewModelMethod() {
        if (myCallback != null) {
            myCallback.onCustomCallback();
        }
    }

    // 카메라 실행시키는 메서드
    public void setCamera(){
        _camera.setValue("test");
    }

    public LiveData getCamera(){
        if(_camera==null){
            _camera=new MutableLiveData<String>();
        }
        return this._camera;
    }


    // 뒤로 가기페이지 -  추후 개발 예정
    public void pageBack(){
    }


}
