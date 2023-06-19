package com.snd.app.ui.write;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
public class RegistTreeStatusInfoActivity extends TMActivity implements MyCallback, AdapterView.OnItemSelectedListener {
    RegistTreeStatusInfoActBinding treeStatusInfoBinding;
    RegistTreeStatusInfoViewModel treeStatusInfoVM;
    TreeStatusInfoDTO statusInfoDTO;
    String NFC;
    Spinner spinner;
    Boolean flag;
    private static final String DEFAULT_VALUE = "0";


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
        statusInfoDTO=new TreeStatusInfoDTO();
        // 스피너 설정
        spinner=findViewById(R.id.treeStatus_tr_state);
        ArrayAdapter adapter=ArrayAdapter.createFromResource(this, R.array.treeStatus_pest,  android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
    }


    @Override
    public void onCustomCallback() {
        //팝업 창 띄우기
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistTreeStatusInfoActivity.this);
        builder.setTitle("수목 상태 정보 입력이 완료되었습니다.");
        builder.setMessage("이어서 환경 정보 등록하시겠습니까?");
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
            treeStatusData.put("creation", statusInfoDTO.getCreation());
            treeStatusData.put("nfc", NFC);
            treeStatusData.put("dbh", statusInfoDTO.getDBH());
            treeStatusData.put("rcc", statusInfoDTO.getRCC());
            treeStatusData.put("height", statusInfoDTO.getHeight());
            treeStatusData.put("length", statusInfoDTO.getLength());
            treeStatusData.put("width", statusInfoDTO.getWidth());
            treeStatusData.put("pest", flag);
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


    private String getInputText(AppCompatEditText editText) {
        return TextUtils.isEmpty(editText.getText()) ? DEFAULT_VALUE : String.valueOf(editText.getText());
    }

   public void setStatusInfoDTO(){
       LocalDate currentDate = LocalDate.now();
       Log.d(TAG, "** 현재 날짜 추출 **"+currentDate);

       String dbh = getInputText(findViewById(R.id.treeStatus_scarlet_diam));
       String rcc = getInputText(findViewById(R.id.treeStatus_tr_height));
       String height = getInputText(findViewById(R.id.treeStatus_crw_height));
       String length = getInputText(findViewById(R.id.treeStatus_crw_diam));
       String width = getInputText(findViewById(R.id.treeStatus_pest_dmg_state));

       statusInfoDTO.setDBH(Double.parseDouble(dbh));
       statusInfoDTO.setRCC(Double.parseDouble(rcc));
       statusInfoDTO.setHeight(Double.parseDouble(height));
       statusInfoDTO.setLength(Double.parseDouble(length));
       statusInfoDTO.setWidth(Double.parseDouble(width));
       statusInfoDTO.setCreation(currentDate);
   }


    @Override
    protected void onResume() {
        super.onResume();
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Log.d(TAG, "** 선택된 아이템의 결과 ! **" + position);

        if(position==0){
            flag=false;
        } else {
            flag=true;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.d(TAG, "** onNothingSelected 호출됨 **" );
    }

}
