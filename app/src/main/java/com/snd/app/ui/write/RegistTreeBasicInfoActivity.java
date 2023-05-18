package com.snd.app.ui.write;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.snd.app.R;
import com.snd.app.common.TMActivity;
import com.snd.app.data.AppComponent;
import com.snd.app.data.AppModule;
import com.snd.app.data.DaggerAppComponent;
import com.snd.app.data.user.SharedPreferencesManager;
import com.snd.app.databinding.RegistTreeBasicInfoActBinding;
import com.snd.app.domain.tree.TreeBasicInfoDTO;
import com.snd.app.sharedPreferences.SharedApplication;
import com.snd.app.ui.tree.TreeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

public class RegistTreeBasicInfoActivity extends TMActivity {
    RegistTreeBasicInfoActBinding registTreeBasicInfoActBinding;

    @Inject
    SharedPreferencesManager sharedPreferencesManager;
    String idHex;

    RegistTreeBasicInfoViewModel registTreeBasicInfoViewModel;
    TreeBasicInfoDTO treeBasicInfoDTO;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        registTreeBasicInfoActBinding= DataBindingUtil.setContentView(this, R.layout.regist_tree_basic_info_act);
        registTreeBasicInfoActBinding.setLifecycleOwner(this);

        // 의존성 주입하기
        AppComponent appComponent= DaggerAppComponent.builder().appModule(new AppModule(new SharedApplication())).build();
        appComponent.inject(this);

        sharedPreferencesManager=appComponent.sharedPreferencesManager();
        registTreeBasicInfoViewModel=new RegistTreeBasicInfoViewModel();

        registTreeBasicInfoActBinding.setTreeBasicInfoVM(registTreeBasicInfoViewModel);
        setTreeBasicInfoDTO();

        Log.d(TAG,"** 나니? **");
    }


    public void setTreeBasicInfoDTO(){
        // 디티오 현재 null
        Log.d(TAG, "디티오 확인"+treeBasicInfoDTO);
        //treeBasicInfoDTO.setNFC("1234");
        //treeBasicInfoDTO.setSpecies("나무");
        //treeBasicInfoDTO.setSubmitter(sharedPreferencesManager.getUserInfo("name",null));
        //treeBasicInfoDTO.setVendor(sharedPreferencesManager.getUserInfo("company",null));

        registTreeBasicInfoViewModel.setTextViewModel(treeBasicInfoDTO);

    }


    // 수목 기본정보 등록
    public void registerTreeBasicInfo(String idHex){

        JSONObject TreeBasicInfo=new JSONObject();
        // DTO 객체 매핑해서 넘기기!!

        JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.POST, sndUrl+"/app/login", TreeBasicInfo,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "** 로그인 응답 ** "+response);

                        try {
                            String token = response.get("data").toString();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // 에러 처리
                        Log.d(TAG, "** 오류남 **"+error);
                        Toast.makeText(getApplicationContext(),"로그인 정보가 올바르지 않습니다", Toast.LENGTH_SHORT).show();
                    }
                });
    }



}
