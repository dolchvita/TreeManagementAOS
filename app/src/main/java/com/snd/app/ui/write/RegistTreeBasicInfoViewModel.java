package com.snd.app.ui.write;

import android.graphics.Bitmap;
import android.os.Looper;
import android.util.Log;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.snd.app.common.TMViewModel;
import com.snd.app.data.AppComponent;
import com.snd.app.data.AppModule;
import com.snd.app.data.DaggerAppComponent;
import com.snd.app.domain.tree.TreeBasicInfoDTO;
import com.snd.app.domain.tree.TreeLocationInfoDTO;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;


public class RegistTreeBasicInfoViewModel extends TMViewModel{
    public TreeBasicInfoDTO treeBasicInfoDTO;
    public String speciesName;


    // 기본 정보
    public ObservableField<String> NFC=new ObservableField<>();
    public ObservableField<String> submitter=new ObservableField<>();
    public ObservableField<String> species=new ObservableField<>();

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
    List<File> currentFileList=new ArrayList<>();   // 실제 사진이 담겨있는 리스트

    protected static final String DEFAULT_VALUE = "0";

    WriteUseCase writeUseCase;
    public String Authorization;

    public MutableLiveData<String> _resultLiveData = new MutableLiveData<>();
    public LiveData resultLiveData=getResultLiveData();

    AppComponent appComponent = DaggerAppComponent.builder().appModule(new AppModule(null)).build();

    // 뒤로가기
    private MutableLiveData _back=new MutableLiveData<>();
    public LiveData back=getBack();


    public void setResultLiveData(){
        _resultLiveData.setValue("click");
    }


    public LiveData getResultLiveData(){
        return _resultLiveData;
    }


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


    // 화면에 표시되는 메서드
    public void setTextViewModel(TreeBasicInfoDTO treeBasicInfoDTO,  TreeLocationInfoDTO treeLocationInfoDTO){
        this.treeBasicInfoDTO=treeBasicInfoDTO;
        NFC.set(treeBasicInfoDTO.getNFC());
        submitter.set(treeBasicInfoDTO.getSubmitter());
        vendor.set(treeBasicInfoDTO.getVendor());
        latitude.set(""+treeLocationInfoDTO.getLatitude());
        longitude.set(""+treeLocationInfoDTO.getLongitude());
    }



    // 수목명을 수동으로 입력받는다.
    public String processUserInput(String userInput) {
        Log.d(TAG, "processUserInput"+userInput);
        Log.d(TAG, "processUserInput"+treeBasicInfoDTO);

        treeBasicInfoDTO.setSpecies(userInput);
        speciesName=userInput;
        return userInput;
    }



    // 1-1) 수목 기본정보
    public JSONObject mappingTreeBasicInfo(){
        JSONObject treeBasicData=new JSONObject();
        try {
            // 입력 데이터 보내기
            treeBasicData.put("nfc", NFC.get());

            // 어떻게 가져오지? -> 액티비티에서 보낼때,,
            if(treeBasicInfoDTO==null){
                treeBasicData.put("species", speciesName);
                Log.d(TAG, " 이름 "+speciesName);
            }else {
                Log.d(TAG, "뭐하니"+treeBasicInfoDTO);
                treeBasicData.put("species", treeBasicInfoDTO.getSpecies());
            }

            treeBasicData.put("submitter",submitter.get());
            treeBasicData.put("vendor", vendor.get());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return treeBasicData;
    }



    public void register(){
        writeUseCase=appComponent.writeUseCase();
        writeUseCase.registerTreeBasicInfo(mappingTreeBasicInfo(), Authorization);
    }



    // 이것은 비동기로 바로 불러진다는 것을 명심하자.
    public void responseTest() {
        writeUseCase=appComponent.writeUseCase();
        Log.d(TAG, "** 제발제발 !!! **");
        Log.d(TAG, ""+NFC.get()+Authorization);

        writeUseCase.registerTreeImage(mappingTreeImage(), NFC.get(), Authorization);
    }



    // 이미지 담기
    public List<File> mappingTreeImage(){
        Log.d(TAG, "** mappingTreeImage ** "+currentFileList);
        return currentFileList;
    }




    // 콜백 객체를 받아서 액티비티로부터 호출 가능하게 함
    public void setCallback(MyCallback myCallback) {
        this.myCallback = myCallback;
    }


    // 카메라 실행시키는 메서드
    public void setCamera(){
        _camera.setValue("click");
    }


    public LiveData getCamera(){
        if(_camera==null){
            _camera=new MutableLiveData<String>();
        }
        return this._camera;
    }



    @Override
    protected void onCleared() {
        super.onCleared();

        // 콜백을 해제합니다.
        myCallback = null;

        // LiveData, ObservableField 객체들을 해제합니다.
        _camera = null;
        _listData = null;
        _imgCount = null;
        _resultLiveData = null;
        _back = null;

        // 비트맵과 파일 리스트를 해제합니다.
        if(currentList != null) {
            for(Bitmap bitmap: currentList) {
                bitmap.recycle();
            }
            currentList.clear();
            currentList = null;
        }
        if(currentFileList != null) {
            currentFileList.clear();
            currentFileList = null;
        }

        // Use case를 해제합니다.
        writeUseCase = null;
    }


}