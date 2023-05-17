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
import com.snd.app.databinding.WriteTreeBasicInfoActBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class RegistTreeBasicInfoActivity extends TMActivity {
    private String TAG=this.getClass().getName();

    WriteTreeBasicInfoActBinding treeBasicInfoActBinding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        treeBasicInfoActBinding= DataBindingUtil.setContentView(this, R.layout.write_tree_basic_info_act);
        treeBasicInfoActBinding.setLifecycleOwner(this);

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
