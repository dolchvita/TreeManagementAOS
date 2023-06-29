package com.snd.app.ui.read;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.snd.app.R;
import com.snd.app.common.TMActivity;
import com.snd.app.data.KakaoMapFragment;
import com.snd.app.databinding.ReadActBinding;
import com.snd.app.domain.tree.TreeBasicInfoDTO;
import com.snd.app.domain.tree.TreeLocationInfoDTO;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class GetTreeInfoActivity extends TMActivity implements AdapterView.OnItemSelectedListener{
    private String idHex;
    private KakaoMapFragment kakaoMapFragment;
    GetTreeBasicInfoViewModel getTreeBasicInfoVM;
    // 프레그먼트들
    GetTreeBasicInfoFragment getTreeBasicInfoFr;
    GetTreeSpecificLocationFragment getTreeSpecificLocationFr;
    GetTreeStatusFragment getTreeStatusFr;
    GetEnvironmentFragment getEnvironmentFr;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        idHex=getIntent().getStringExtra("IDHEX");
        ReadActBinding readActBinding= DataBindingUtil.setContentView(this, R.layout.read_act);
        readActBinding.setLifecycleOwner(this);
        GetTreeInfoViewModel getTreeInfoVM=new GetTreeInfoViewModel();
        readActBinding.setGetTreeInfoVM(getTreeInfoVM);
        getTreeBasicInfoVM=new ViewModelProvider(this).get(GetTreeBasicInfoViewModel.class);

        getTreeBasicInfoFr=new GetTreeBasicInfoFragment();
        getTreeSpecificLocationFr=new GetTreeSpecificLocationFragment();
        getTreeStatusFr=new GetTreeStatusFragment();
        getEnvironmentFr=new GetEnvironmentFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.read_content, getTreeBasicInfoFr).commit();
        setKakaoMapFragment(R.id.readTreeBasic_map_layout);
        getTreeInfoByNFCtagId();


        // 스피너 설정
        Spinner spinner = findViewById(R.id.read_sipnner);
        //String[] menuItems = getResources().getStringArray(R.array.read_act_menu);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.read_act_menu, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

    }


    public void setKakaoMapFragment(int viewId){
        kakaoMapFragment = new KakaoMapFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(viewId, kakaoMapFragment)
                .commit();
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

        // 비동기 방식 사용하기 !!! - ** 네트워크 요청 이후 작업할 것 **
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

                        editor.putString("species", data.getString("species"));
                        editor.putString("latitude", data.getString("latitude"));
                        editor.putString("longitude", data.getString("longitude"));
                        editor.apply();

                        // 디티오 세팅 순서 바꿔보기
                        try {
                            setTreeBasicInfoDTO();
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }

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


    /* --------------------------------------------------------
        프레그먼트 설정 관련
    -------------------------------------------------------- */
    public void switchFragment(Fragment frName){
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.read_content, frName);
        transaction.commit();
    }



    public void setTreeBasicInfoDTO() throws JsonProcessingException {
        // 기본 정보 세팅하기
        TreeBasicInfoDTO treeBasicInfoDTO=new TreeBasicInfoDTO();
        treeBasicInfoDTO.setNFC(idHex);
        treeBasicInfoDTO.setSpecies(sharedPreferences.getString("species",null));
        treeBasicInfoDTO.setSubmitter(sharedPreferences.getString("id",null));
        treeBasicInfoDTO.setVendor(sharedPreferences.getString("company",null));

        // 위치 정보 세팅하기
        TreeLocationInfoDTO treeLocationInfoDTO=new TreeLocationInfoDTO();
        String str1=sharedPreferences.getString("latitude", null);
        String str2=sharedPreferences.getString("longitude", null);
        double latitude=Double.parseDouble(str1);
        double longitude=Double.parseDouble(str2);
        treeLocationInfoDTO.setLatitude(latitude);
        treeLocationInfoDTO.setLongitude(longitude);

        Log.d(TAG, "** 확인요 ** "+sharedPreferences.getString("species", null));
        Log.d(TAG, "** 확인2 ** "+sharedPreferences.getString("latitude", null));
        // 매핑된 DTO 넘겨줌
        getTreeBasicInfoVM.setTextViewModel(treeBasicInfoDTO, treeLocationInfoDTO);
    }



    /* --------------------------------------------------------
            스피너 설정 관련
        -------------------------------------------------------- */
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
