package com.snd.app.ui.read;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.snd.app.R;
import com.snd.app.common.TMActivity;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetTreeInfoActivity extends TMActivity {
    private String idHex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_tree_info_act);
        idHex=getIntent().getStringExtra("IDHEX");
    }


    //수목 통합 정보 가져오기
    public void getTreeInfoByNFCtagId(){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(sndUrl+"/app/tree/getTreeInfo/")
                .addHeader("Authorization", sharedPreferences.getString("Authorization", null))
                .build();
        try {
            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();

            // 응답 결과를 처리합니다.
            Log.d(TAG,"** 응답 코드: " + response.code());
            Log.d(TAG,"** 응답 본문: " + responseBody);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}