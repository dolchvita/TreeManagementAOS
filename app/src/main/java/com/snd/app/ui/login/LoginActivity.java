package com.snd.app.ui.login;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.snd.app.MainActivity;
import com.snd.app.MainViewModel;
import com.snd.app.R;
import com.snd.app.data.AppModule;
import com.snd.app.data.JsonWebTokenUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class LoginActivity extends AppCompatActivity {
    private String TAG=this.getClass().getName();

    EditText t_id;
    EditText t_pass;
    String sndUrl="http://snd.synology.me:9955";
    StringRequest request;
    JSONObject jsonData=new JSONObject();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_login);

        t_id=findViewById(R.id.id_input);
        t_pass=findViewById(R.id.pass_input);

        Button bt_login=findViewById(R.id.bt_login);

        // requestQueue 생성
        if(AppModule.requestQueue == null)
            AppModule.requestQueue = Volley.newRequestQueue(getApplicationContext());

        // 로그인 버튼
        bt_login.setOnClickListener((v) ->{
            loginRequest();
            Log.d(TAG, "** 보낼 데이터 모습 ** "+jsonData);
        });

    } //onCreate


    public void loginRequest() {

        // 입력 데이터 보내기
        try {
            jsonData.put("id", t_id.getText().toString());
            jsonData.put("password", t_pass.getText().toString());

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, sndUrl+"/app/login", jsonData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "** 응답 **"+response);

                        // 로그인 성공시 화면 전환
                        try {
                            if(response.get("result").toString().equals("success")){
                                startActivity();
                            }

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        /* 유효성 체크
                        try {
                            validationCheckRequest(response.get("data").toString());

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }*/
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // 에러 처리
                        Log.d(TAG, "** 오류남 **"+error);
                    }
                });

        //jsonObjectRequest.setShouldCache(false);
        AppModule.requestQueue.add(jsonObjectRequest);
    }


   public void startActivity(){
        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);
   }


   // 로그인 성공시 유효성 검증
   public void validationCheckRequest(String tonken){
        Log.d(TAG, "** 요청받은 토큰 ** "+tonken);

        StringRequest request1=new StringRequest(Request.Method.GET, sndUrl+"/app/validationCheck", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //응답
               Log.d(TAG, "** 유효성 결과 ** "+response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // 에러
                Log.d(TAG, "** 유효성 오류남 **"+error);
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> tokenData=new HashMap<String, String>();
                tokenData.put("value", "Bearer "+tonken);
                return tokenData;
            }
        };
       AppModule.requestQueue.add(request1);
   }


}