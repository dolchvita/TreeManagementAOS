package com.snd.app.ui.write;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.snd.app.R;
import com.snd.app.common.TMActivity;
import com.snd.app.data.AppModule;
import com.snd.app.databinding.RegistTreeStatusInfoActBinding;
import com.snd.app.domain.tree.TreeStatusInfoDTO;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

// 수목 상태 정보 등록
public class RegistTreeStatusInfoActivity extends TMActivity implements MyCallback{

    RegistTreeStatusInfoActBinding treeStatusInfoBinding;
    RegistTreeStatusInfoViewModel treeStatusInfoVM;
    TreeStatusInfoDTO statusInfoDTO;
    String NFC;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        treeStatusInfoBinding= DataBindingUtil.setContentView(this, R.layout.regist_tree_status_info_act);
        treeStatusInfoBinding.setLifecycleOwner(this);
        treeStatusInfoVM=new RegistTreeStatusInfoViewModel();
        treeStatusInfoBinding.setTreeStatusInfoVM(treeStatusInfoVM);
        // 콜백 연결
        treeStatusInfoVM.setCallback(this);

        // NFC 코드 추출
        NFC=getIntent().getStringExtra("NFC");
    }


    @Override
    public void onCustomCallback() {
        //팝업 창 띄우기
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistTreeStatusInfoActivity.this);
        builder.setTitle("추가 입력");
        builder.setMessage("수목 환경 정보를 등록하시겠습니까?");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 확인 버튼을 눌렀을 때
                //Intent intent=new Intent(RegistTreeStatusInfoActivity.this, null);
                //startActivity(intent);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 취소 버튼을 눌렀을 때
                registerTreeInfo();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void registerTreeInfo(){
        setStatusInfoDTO();

        OkHttpClient client = new OkHttpClient();
        JSONObject treeStatusData=new JSONObject();
        try {
            // 입력 데이터 보내기
            treeStatusData.put("creation", null);
            treeStatusData.put("nfc", NFC);
            treeStatusData.put("dbh", statusInfoDTO.getDBH());
            treeStatusData.put("rcc", statusInfoDTO.getRCC());
            treeStatusData.put("height", statusInfoDTO.getHeight());
            treeStatusData.put("length", statusInfoDTO.getLength());
            treeStatusData.put("width", statusInfoDTO.getWidth());
            treeStatusData.put("pest", statusInfoDTO.isPest());
            treeStatusData.put("submitter", sharedPreferences.getString("id", null));
            treeStatusData.put("vendor", sharedPreferences.getString("company", null));
            treeStatusData.put("modified", null);
            treeStatusData.put("inserted", null);

            Log.d(TAG, "** 보낼 데이터 모습 ! ** "+treeStatusData);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody=RequestBody.create(MediaType.parse("application/json"), treeStatusData.toString());
        String url = sndUrl+"/app/tree/registerStatusInfo";

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", sharedPreferences.getString("Authorization",null))
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) throws IOException {
                if (response.isSuccessful()){
                    String responseData = response.body().string();
                    Log.d(TAG,"** 상태 등록 성공 / 응답 **"+responseData);
                }else{
                    String responseData = response.body().string();
                    Log.d(TAG,"** 상태 등록 실패 / 응답 **"+responseData);
                }
            }
            @Override
            public void onFailure(Call call, IOException e) {
                // 여기에 요청이 실패했을 때 실행될 코드를 작성하세요.
                Log.d(TAG,"** 상태 등록 오류남 **");
            }
        });
    }


   public void setStatusInfoDTO(){
       LocalDate currentDate = LocalDate.now();
       Log.d(TAG, "** 현재 날짜 추출 **"+currentDate);

       // 레이아웃 매핑
       AppCompatEditText editText1=findViewById(R.id.treeStatus_scarlet_diam);
       String dbh= String.valueOf(editText1.getText());
       AppCompatEditText editText2=findViewById(R.id.treeStatus_tr_height);
       String rcc= String.valueOf(editText2.getText());
       AppCompatEditText editText3=findViewById(R.id.treeStatus_crw_height);
       String height= String.valueOf(editText3.getText());
       AppCompatEditText editText4=findViewById(R.id.treeStatus_crw_diam);
       String length= String.valueOf(editText4.getText());
       AppCompatEditText editText5=findViewById(R.id.treeStatus_pest_dmg_state);
       String width= String.valueOf(editText5.getText());
       AppCompatEditText editText6=findViewById(R.id.treeStatus_tr_state);
       //String pest= String.valueOf(editText6.getText());

       statusInfoDTO=new TreeStatusInfoDTO();
       statusInfoDTO.setDBH(Double.parseDouble(dbh));
       statusInfoDTO.setRCC(Double.parseDouble(rcc));
       statusInfoDTO.setHeight(Double.parseDouble(height));
       statusInfoDTO.setLength(Double.parseDouble(length));
       statusInfoDTO.setWidth(Double.parseDouble(width));
       statusInfoDTO.setPest(true);
       statusInfoDTO.setCreation(currentDate);
   }


}
