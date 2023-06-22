package com.snd.app.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.snd.app.MainActivity;
import com.snd.app.R;
import com.snd.app.common.TMActivity;
import com.snd.app.data.AppModule;
import com.snd.app.domain.UserDTO;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends TMActivity {
    private String TAG=this.getClass().getName();

    EditText t_id;
    EditText t_pass;
    String sndUrl="http://snd.synology.me:9955";
    String token;
    String id;
    String password;

    // 1 로그인 정보
    JSONObject loginData=new JSONObject();

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
            Log.d(TAG, "** 보낼 데이터 모습 ** "+loginData);
        });

    } //onCreate


    public void loginRequest() {
        id=t_id.getText().toString();
        password=t_pass.getText().toString();

        // 입력 데이터 보내기
        try {
            loginData.put("id", id);
            loginData.put("password", password);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.POST, sndUrl+"/app/login", loginData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "** 로그인 응답 ** "+response);

                        try {
                            token = response.get("data").toString();
                            validationCheckRequest();
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

        //jsonObjectRequest.setShouldCache(false);
        AppModule.requestQueue.add(request1);
    }



    // 2 유효성 검증
    public void validationCheckRequest(){
        Log.d(TAG, "** 넘어온 토큰 ** "+token);

        StringRequest request2=new StringRequest(Request.Method.GET, sndUrl+"/app/validationCheck", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //응답
                Log.d(TAG, "** 유효성 결과 ** "+response);
                if(response.equals("success")){
                    getUserInfoByUserId(t_id.getText().toString(), token);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // 에러
                Log.d(TAG, "** 유효성 오류남 **"+error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        AppModule.requestQueue.add(request2);
    }



    // 3 회원 정보 가져오기
   public void getUserInfoByUserId(String id, String token){
       Log.d(TAG, "** 넘어온 아이디 ** "+id);

        // 요청시 보내는 데이터가 없으므로 null 입력
       JsonObjectRequest request3=new JsonObjectRequest(Request.Method.GET, sndUrl + "/app/user/info/"+id, null,
               new Response.Listener<JSONObject>() {
                   @Override
                   public void onResponse(JSONObject response) {
                       Log.d(TAG, "** 회원 정보 응답 ** "+response.toString());
                       
                       // 매핑
                       Gson gson=new Gson();
                       UserDTO user = null;
                       try {
                           user = gson.fromJson(response.get("data").toString(), UserDTO.class);

                           Log.d(TAG, "**로그인 화면에서 유저 확인 ** "+user);

                           saveUserInfo(user);

                       } catch (JSONException e) {
                           throw new RuntimeException(e);
                       }
                       Log.d(TAG, "** 추출한 회원 이름 ** "+user.getName());
                   }
               },
               new Response.ErrorListener() {
                   @Override
                   public void onErrorResponse(VolleyError error) {
                       Log.d(TAG, "** 회원 정보 오류남 ** "+error);
                   }
               }){
           @Override
           public Map<String, String> getHeaders() throws AuthFailureError {
               Map<String, String> headers = new HashMap<>();
               headers.put("Authorization", "Bearer " + token);
               return headers;
           }
       };
       AppModule.requestQueue.add(request3);
       startActivity();
   }


   // 4 회원 정보 저장하기
   public void saveUserInfo(UserDTO user){

       // 로그인시 생성한 PS 객체를 매개변수로 넘겨줌
       Log.d(TAG, "**로그인 액티비티 쉐어드 객체 확인 ** "+sharedPreferences);

       editor.putString("company",user.getCompany());
       editor.putString("name",user.getName());
       editor.putString("id",id);

       editor.putString("Authorization", "Bearer "+token);
       editor.apply();

       // 위의 코드를 모듈화 해보기
       Log.d(TAG, "** 저장된 SP 객체의 회사명 ** "+sharedPreferences.getString("company",null));

    }

    public void startActivity(){
        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}