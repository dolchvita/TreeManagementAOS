package com.snd.app;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;

import com.snd.app.common.TMActivity;
import com.snd.app.databinding.MainActBinding;
import com.snd.app.domain.tree.TreeTotalDTO;
import com.snd.app.ui.home.HomeFragment;
import com.snd.app.ui.map.MapViewModel;
import com.snd.app.ui.map.Mapfragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends TMActivity {
    MainActBinding mainActBinding;
    @Inject
    MainViewModel mainVM;
    private String TAG=this.getClass().getName();
    private HomeFragment homeFragment;
    private Mapfragment mapFragment;

    MapViewModel mapVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActBinding=DataBindingUtil.setContentView(this, R.layout.main_act);
        mainActBinding.setLifecycleOwner(this);
        mainVM=new ViewModelProvider(this).get(MainViewModel.class);    // ViewModel 가져오기
        mainActBinding.setMainVM(mainVM);
        mainVM=mainActBinding.getMainVM();  // 뷰모델 연동
        // 화면에 보일 프레그먼트
        homeFragment=new HomeFragment();
        mapFragment=new Mapfragment(this);

        // 처음 화면을 메인으로 갖추는 것
        getSupportFragmentManager().beginTransaction().replace(R.id.content, new HomeFragment()).commit();


        mainVM.tabClick.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                // 여기서 UI 업데이트 처리
                Log.d(TAG,"클릭 감지");
                // 매개변수로 넘어오는 객체는 뷰모델이 설정한 상수의 값! - 세팅된 것이 넘어옴
                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();

                // 메인 화면을 4가지의 프레그먼트로 분할
                if(o.equals(1)){
                    transaction.replace(R.id.content, homeFragment);

                } else if (o.equals(2)) {
                    transaction.replace(R.id.content, mapFragment);
                    getTreeInfoListAll();
                }
                transaction.commit();
            }
        });

        getLocation();
    }


    // 등록한 모든 수목정보 가져오기
    public void getTreeInfoListAll(){
        String url=sndUrl+"/app/tree/getTreeInfoListAll";

        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url(url)
                .addHeader("Authorization", sharedPreferences.getString("Authorization", null))
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d(TAG,"** 수목 정보 가져오기 실패 **");
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){
                    String responseData = response.body().string();
                    Log.d(TAG,"** 조회 성공 / 응답 **"+responseData);

                    ArrayList<TreeTotalDTO> treeInfoList= new ArrayList<>();


                    try {
                        JSONObject json = new JSONObject(responseData);
                        JSONArray data=json.getJSONArray("data");
                        Log.d(TAG,"** data 추출 **"+data.toString());

                        for(int i=0; i<data.length(); i++){
                            JSONObject treeInfo=data.getJSONObject(i);
                            TreeTotalDTO dto = new TreeTotalDTO();
                            dto.setLatitude(Double.parseDouble(treeInfo.getString("latitude")));
                            dto.setLongitude(Double.parseDouble(treeInfo.getString("longitude")));
                            dto.setSpecies(treeInfo.getString("species"));

                            treeInfoList.add(dto);
                        }
                        mapFragment.setLoctionList(treeInfoList);

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }else {
                    String responseData = response.body().string();
                    Log.d(TAG,"** 조회 실패 / 원인 **"+responseData);
                }
            }
        });

    }


}