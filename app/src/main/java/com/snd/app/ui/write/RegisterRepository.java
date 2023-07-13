package com.snd.app.ui.write;


import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.snd.app.data.AppComponent;
import com.snd.app.data.AppModule;
import com.snd.app.data.DaggerAppComponent;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;



// 서버와 통신하여 데이터를 저장할 Repository
public class RegisterRepository{
    protected String TAG = this.getClass().getName();
    protected String sndUrl = "http://snd.synology.me:9955";

    RegisterCallbackImpl registerCallback;


    public RegisterRepository() {
        AppComponent appComponent = DaggerAppComponent.builder().appModule(new AppModule(null)).build();
        registerCallback=appComponent.registerCallbackImpl();
    }


    // 수목 기본 정보 등록하기
    public void registerTreeInfo2(JSONObject postData, String postUrl, String Authorization){
        Log.d(TAG, "** registerTreeInfo2 호출 **");
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), postData.toString());
        String url = sndUrl+postUrl;
        // 요청 생성
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", Authorization)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) throws IOException {
                if (response.isSuccessful()){
                    String responseData = response.body().string();
                    Log.d(TAG,"** 성공 / 응답 **"+responseData);

                    //registerTreeLocationInfo();

                    registerCallback.responseSuccessful();



                }else{
                    String responseData = response.body().string();
                    Log.d(TAG,"** 실패 / 응답 **"+responseData);

                    registerCallback.responseFailure();

                    /*
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "이미 등록된 칩입니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                     */
                }
            }
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG,"** 오류남 **");
            }
        });
    }



    // 1-3) 사진 리스트 등록
    public void registerTreeImage(List<File> files, String idHex, String Authorization){
        OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        if (files.size()>0){
            for (int i=0; i<files.size(); i++){
                builder.addFormDataPart("image"+(i+1), files.get(i).getName(), RequestBody.create(MediaType.parse("multipart/form-data"), files.get(i)));
            }
        }
        builder.addFormDataPart("tagId", idHex );

        // 업로드할 URL을 생성합니다.
        String url = sndUrl+"/app/tree/registerTreeImage"; // 실제 업로드할 서버의 URL로 변경해야 합니다.

        // 요청 생성
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", Authorization)
                .post(builder.build())
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) throws IOException {
                Log.d(TAG,"** 사진 등록 성공 **"+response);
            }
            @Override
            public void onFailure(Call call, IOException e) {
                // 여기에 요청이 실패했을 때 실행될 코드를 작성하세요.
                Log.d(TAG,"** 사진 오류남 **");
            }
        });
    }



}