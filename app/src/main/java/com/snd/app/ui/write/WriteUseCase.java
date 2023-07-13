package com.snd.app.ui.write;

import android.util.Log;

import com.snd.app.data.AppComponent;
import com.snd.app.data.AppModule;
import com.snd.app.data.DaggerAppComponent;

import org.json.JSONObject;

import java.io.File;
import java.util.List;


public class WriteUseCase {
    private String TAG=this.getClass().getName();
    private RegisterRepository registerRepository;


    public WriteUseCase(RegisterRepository registerRepository) {
        this.registerRepository=registerRepository;
    }


    // 수목 기본정보 등록
    public void registerTreeBasicInfo(JSONObject treeBasicData, String Authorization){
        Log.d(TAG, "**메서드 날라옴 1** "+treeBasicData+", "+Authorization);
        registerRepository.registerTreeInfo2(treeBasicData, "/app/tree/registerBasicInfo", Authorization);
    }


    // 수목 기본정보 이미지등록
    public void registerTreeImage(List<File> files, String idHex, String Authorization){
        Log.d(TAG, "**메서드 날라옴 2** "+files+", "+idHex+", "+Authorization);
        registerRepository.registerTreeImage(files, idHex, Authorization);
    }


    // 수목 위치(기본)정보 등록
    public void registerLocationInfo(JSONObject treeLocationData, String Authorization){
        Log.d(TAG, "**메서드 날라옴 3** "+treeLocationData+", "+Authorization);
        registerRepository.registerTreeInfo2(treeLocationData, "/app/tree/registerLocationInfo", Authorization);

    }



}
