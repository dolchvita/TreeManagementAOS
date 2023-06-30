package com.snd.app.ui.read;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.snd.app.R;
import com.snd.app.common.TMActivity;
import com.snd.app.data.KakaoMapFragment;
import com.snd.app.data.LocalDateTimeAdapter;
import com.snd.app.databinding.ReadActBinding;
import com.snd.app.domain.tree.TreeIntegratedVO;
import com.snd.app.ui.write.MyCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class GetTreeInfoActivity extends TMActivity implements AdapterView.OnItemSelectedListener, MyCallback {
    private String idHex;
    private KakaoMapFragment kakaoMapFragment;
    GetTreeBasicInfoViewModel getTreeBasicInfoVM;

    // 프레그먼트들
    GetTreeBasicInfoFragment getTreeBasicInfoFr;
    GetTreeSpecificLocationFragment getTreeSpecificLocationFr;
    GetTreeStatusFragment getTreeStatusFr;
    GetEnvironmentFragment getEnvironmentFr;

    TreeIntegratedVO treeIntegratedVO;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        idHex=getIntent().getStringExtra("IDHEX");
        ReadActBinding readActBinding= DataBindingUtil.setContentView(this, R.layout.read_act);
        readActBinding.setLifecycleOwner(this);
        GetTreeInfoViewModel getTreeInfoVM=new GetTreeInfoViewModel();
        readActBinding.setGetTreeInfoVM(getTreeInfoVM);
        getTreeBasicInfoVM=new ViewModelProvider(this).get(GetTreeBasicInfoViewModel.class);

        getTreeInfoVM.setCallback(this);

        getTreeBasicInfoFr=new GetTreeBasicInfoFragment();
        getTreeSpecificLocationFr=new GetTreeSpecificLocationFragment();
        getTreeStatusFr=new GetTreeStatusFragment();
        getEnvironmentFr=new GetEnvironmentFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.read_content, getTreeBasicInfoFr).commit();
        setKakaoMapFragment(R.id.readTreeBasic_map_layout);
        getTreeInfoByNFCtagId();


        // 스피너 설정
        Spinner spinner = findViewById(R.id.read_sipnner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.read_act_menu, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

    }


    @Override
    public void onCustomCallback() {
        Log.d(TAG, "**수정 버튼 **");

        //팝업 창 띄우기
        AlertDialog.Builder builder = new AlertDialog.Builder(GetTreeInfoActivity.this);
        builder.setTitle("입력하신 내용으로 슈정하시겠습니까?");
        builder.setMessage("");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //mappingDTO();
                // 확인 버튼을 눌렀을 때
                modifyTreeBasicInfo();

            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //registerTreeInfo();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }



    public void modifyTreeBasicInfo(){
        JSONObject treeBasicData=new JSONObject();
        try {
            // 입력 데이터 보내기
            treeBasicData.put("nfc", idHex);
            treeBasicData.put("species", getInputText(findViewById(R.id.scarlet_diam)));
            treeBasicData.put("submitter", treeIntegratedVO.getBasicSubmitter());
            treeBasicData.put("vendor", treeIntegratedVO.getBasicVendor());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        putTreeInfo(treeBasicData, "/app/tree/modifyBasicInfo");
    }



    public void putTreeInfo(JSONObject putData, String postUrl){
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), putData.toString());
        String url = sndUrl+postUrl;
        // 요청 생성
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", sharedPreferences.getString("Authorization", null))
                .put(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) throws IOException {
                if (response.isSuccessful()){
                    String responseData = response.body().string();
                    Log.d(TAG,"** 성공 / 응답 **"+responseData);

                    GetTreeInfoActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (response.isSuccessful()) {


                            }
                        }
                    });

                }else{
                    String responseData = response.body().string();
                    Log.d(TAG,"** 실패 / 응답 **"+responseData);
                }
            }
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG,"** 오류남 **");
            }
        });
        Toast.makeText(this, "등록되었습니다", Toast.LENGTH_SHORT).show();
    }



    public void setKakaoMapFragment(int viewId){
        kakaoMapFragment = new KakaoMapFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(viewId, kakaoMapFragment)
                .commit();
    }


    // 수목에 대한 모든 정보 가져오는 메서드 !
    public void getTreeInfoByNFCtagId(){
        String url = sndUrl+"/app/tree/getTreeInfo/"+idHex;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", sharedPreferences.getString("Authorization", null))
                .build();
        // 비동기 방식 사용하기 !!! - ** 네트워크 요청 이후 작업할 것 **
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) throws IOException {
                if (response.isSuccessful()){
                    String responseData = response.body().string();
                    Log.d(TAG,"** 위치 성공 / 응답 **"+responseData);

                    treeIntegratedVO=new TreeIntegratedVO();

                    try {
                        // 제이슨 데이터로 변환 후 추출
                        JSONObject json=new JSONObject(responseData);
                        JSONObject data=json.getJSONObject("data");
                        Log.d(TAG,"** data 추출 **"+data.toString());

                        treeIntegratedVO.setNFC(data.getString("nfc"));

                        // 기본 정보
                        treeIntegratedVO.setSpecies(data.getString("species"));
                        treeIntegratedVO.setBasicSubmitter(data.getString("basicSubmitter"));
                        treeIntegratedVO.setBasicVendor(data.getString("basicVendor"));
                        treeIntegratedVO.setLatitude(Double.parseDouble(data.getString("latitude")));
                        treeIntegratedVO.setLongitude(Double.parseDouble(data.getString("longitude")));
                        getTreeBasicInfoVM.setTextViewModel(treeIntegratedVO);
                        LocalDateTime basicInserted=deserialize(data.getString("basicInserted"));
                        treeIntegratedVO.setBasicInserted(basicInserted);

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


    public LocalDateTime deserialize(String inserted){
        String[] strArr = inserted.replace("[", "").replace("]", "").split(",");
        int[] intArr = new int[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            intArr[i] = Integer.parseInt(strArr[i]);
        }
        LocalDateTimeAdapter localDateTimeAdapter=new LocalDateTimeAdapter();
        return localDateTimeAdapter.test(intArr);
    }



    /* -------------------------- 프레그먼트 설정 관련 --------------------------------- */

    public void switchFragment(Fragment frName){
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.read_content, frName);
        transaction.commit();
    }



    /* ------------------------------ 스피너 설정 관련 ------------------------------- */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "** 선택된 메뉴 값 ** "+position);

       if (position==1){
           switchFragment(getTreeBasicInfoFr);
       } else if (position == 2) {
           switchFragment(getTreeSpecificLocationFr);
       } else if (position == 3) {
           switchFragment(getTreeStatusFr);
       } else if (position == 4) {
           switchFragment(getEnvironmentFr);

       }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.d(TAG, "** 언제 호출되나요 ** ");
    }


}
