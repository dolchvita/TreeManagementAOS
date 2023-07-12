package com.snd.app.ui.write;

import android.util.Log;

import org.json.JSONObject;


public class WriteUseCase {
    private String TAG=this.getClass().getName();
    private RegisterRepository registerRepository;


    public WriteUseCase(RegisterRepository registerRepository) {
        // 생성자 주입
        this.registerRepository = registerRepository;
    }


    public void registerTreeBasicInfo(JSONObject treeBasicData, String Authorization){
        Log.d(TAG, "**메서드 날라옴** "+treeBasicData+", "+Authorization);
        registerRepository.registerTreeInfo2(treeBasicData, "/app/tree/registerBasicInfo", Authorization);
    }


}
