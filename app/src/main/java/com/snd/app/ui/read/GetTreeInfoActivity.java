package com.snd.app.ui.read;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.snd.app.R;
import com.snd.app.common.TMActivity;
import com.snd.app.databinding.GetTreeInfoActBinding;
import com.snd.app.domain.tree.TreeBasicInfoDTO;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class GetTreeInfoActivity extends TMActivity {
    private String idHex;
    TreeBasicInfoDTO treeBasicInfoDTO;
    GetTreeInfoViewModel getTreeInfoVM;
    GetTreeInfoActBinding getTreeInfoActBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_tree_info_act);
        idHex=getIntent().getStringExtra("IDHEX");
        getTreeInfoActBinding= DataBindingUtil.setContentView(this, R.layout.get_tree_info_act);
        getTreeInfoActBinding.setLifecycleOwner(this);
        getTreeInfoVM=new GetTreeInfoViewModel();
        getTreeInfoActBinding.setGetTreeInfoVM(getTreeInfoVM);

        getTreeInfoByNFCtagId();

        try {
            setTreeBasicInfoDTO();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // 사진 이미지 담기
    }


    //수목 통합 정보 가져오기
    public void getTreeInfoByNFCtagId(){
        Log.d(TAG,"** getTreeInfoByNFCtagId 호출됨 - 1 **");
        String url = sndUrl+"/app/tree/getTreeInfo/"+idHex;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", sharedPreferences.getString("Authorization", null))
                .build();

        // 비동기 방식 사용하기 !!!
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) throws IOException {
                if (response.isSuccessful()){
                    String responseData = response.body().string();
                    Log.d(TAG,"** 위치 성공 / 응답 **"+responseData);

                    try {
                        // 제이슨 데이터로 변환 후 추출
                        JSONObject json=new JSONObject(responseData);
                        JSONObject data=json.getJSONObject("data");
                        Log.d(TAG,"** data 추출 **"+data.toString());
                        Log.d(TAG,"** data 추출 **"+ data.getString("species"));

                        editor.putString("species", data.getString("species"));
                        editor.apply();


                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }else{
                    String responseData = response.body().string();
                    Log.d(TAG,"** 위치 실패 / 응답 **"+responseData);
                }
            }
            @Override
            public void onFailure(Call call, IOException e) {
                // 여기에 요청이 실패했을 때 실행될 코드를 작성하세요.
                Log.d(TAG,"** 위치 등록 오류남 **");
            }
        });
    }


    public void setTreeBasicInfoDTO() throws JsonProcessingException {
        treeBasicInfoDTO=new TreeBasicInfoDTO();
        treeBasicInfoDTO.setNFC(idHex);
        treeBasicInfoDTO.setSpecies(sharedPreferences.getString("species",null));
        treeBasicInfoDTO.setSubmitter(sharedPreferences.getString("id",null));
        treeBasicInfoDTO.setVendor(sharedPreferences.getString("company",null));
        // 매핑된 DTO 넘겨줌
        getTreeInfoVM.setTextViewModel(treeBasicInfoDTO);
    }


}