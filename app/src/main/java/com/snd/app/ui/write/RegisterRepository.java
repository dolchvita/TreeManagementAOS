package com.snd.app.ui.write;


import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;



// 서버와 통신하여 데이터를 저장할 Repository
public class RegisterRepository{
    protected String TAG = this.getClass().getName();
    protected String sndUrl = "http://snd.synology.me:9955";



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

                }else{
                    String responseData = response.body().string();
                    Log.d(TAG,"** 실패 / 응답 **"+responseData);


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



}
