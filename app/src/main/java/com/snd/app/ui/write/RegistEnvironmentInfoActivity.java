package com.snd.app.ui.write;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.snd.app.R;
import com.snd.app.common.TMActivity;
import com.snd.app.databinding.RegistEnvironmentInfoActBinding;
import com.snd.app.domain.tree.TreeEnvironmentInfoDTO;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class RegistEnvironmentInfoActivity extends TMActivity implements MyCallback{
    RegistEnvironmentInfoActBinding environmentInfoActBinding;
    RegistEnvironmentInfoViewModel environmentInfoVM;
    TreeEnvironmentInfoDTO treeEnvironmentInfoDTO;
    String NFC;
    private MapView mapView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        environmentInfoActBinding=DataBindingUtil.setContentView(this, R.layout.regist_environment_info_act);
        environmentInfoActBinding.setLifecycleOwner(this);
        environmentInfoVM=new RegistEnvironmentInfoViewModel();
        environmentInfoActBinding.setEnvironmentVM(environmentInfoVM);
        treeEnvironmentInfoDTO=new TreeEnvironmentInfoDTO();
        // 콜백 연결
        environmentInfoVM.setCallback(this);
        NFC=getIntent().getStringExtra("NFC");
        environmentInfoVM.idHex.set(NFC);

        // 카카오맵
        mapView=new MapView(this);
        environmentInfoActBinding.environmentKakaoMap.addView(mapView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mapView != null) {
            mapView.onResume();
            initMapView();
        }
    }

    @Override
    public void onCustomCallback() {
        Log.d(TAG, "** 저장 버튼 누름요 **");
        //팝업 창 띄우기
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistEnvironmentInfoActivity.this);
        builder.setTitle("수목 상태 정보 입력이 완료되었습니다.");
        builder.setMessage("이어서 환경 정보 등록하시겠습니까?");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 확인 버튼을 눌렀을 때
                registerTreeEnvironmentInfo();
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 취소 버튼을 눌렀을 때
                registerTreeEnvironmentInfo();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


   public AlertDialog alertChek (){
       AlertDialog.Builder builder = new AlertDialog.Builder(RegistEnvironmentInfoActivity.this);
       builder.setTitle("등록하시겠습니까?");
       builder.setMessage("확인을 누르면 저장이 완료됩니다");
       builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
               // 확인 버튼을 눌렀을 때
           }
       });
       builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
               // 취소 버튼을 눌렀을 때
           }
       });
       AlertDialog dialog = builder.create();
       //dialog.show();

       return dialog;
   }


    public void registerTreeEnvironmentInfo(){
        setEnvironmentInfoDTO();

        OkHttpClient client = new OkHttpClient();
        JSONObject environmentData=new JSONObject();
        try {
            // 입력 데이터 보내기
            environmentData.put("nfc", NFC);
            environmentData.put("frameHorizontal",  treeEnvironmentInfoDTO.getFrameHorizontal());
            environmentData.put("frameVertical",  treeEnvironmentInfoDTO.getFrameVertical());
            environmentData.put("frameMaterial",  treeEnvironmentInfoDTO.getFrameMaterial());
            environmentData.put("boundaryStone",  treeEnvironmentInfoDTO.getBoundaryStone());
            environmentData.put("roadWidth",  treeEnvironmentInfoDTO.getRoadWidth());
            environmentData.put("sidewalkWidth",  treeEnvironmentInfoDTO.getSidewalkWidth());
            environmentData.put("packingMaterial",  treeEnvironmentInfoDTO.getPackingMaterial());
            environmentData.put("soilPH",  treeEnvironmentInfoDTO.getSoilPH());
            environmentData.put("soilDensity",  treeEnvironmentInfoDTO.getSoilDensity());
            environmentData.put("submitter",   sharedPreferences.getString("id", null));
            environmentData.put("vendor",  sharedPreferences.getString("company", null));
            Log.d(TAG, "** 보낼 데이터 모습 ! ** "+environmentData);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody=RequestBody.create(MediaType.parse("application/json"), environmentData.toString());
        String url = sndUrl+"/app/tree/registerEnvironmentInfo";
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
                    Log.d(TAG,"** 환경 등록 성공 / 응답 **"+responseData);

                }else{
                    String responseData = response.body().string();
                    Log.d(TAG,"** 환경 등록 실패 / 응답 **"+responseData);
                }
            }
            @Override
            public void onFailure(Call call, IOException e) {
                // 여기에 요청이 실패했을 때 실행될 코드를 작성하세요.
                Log.d(TAG,"** 환경 등록 오류남 **");
            }
        });
        Toast.makeText(this, "등록되었습니다", Toast.LENGTH_SHORT).show();
    }


    // 입력값 받기
    public void setEnvironmentInfoDTO(){
        String frameHorizontal=getInputText(findViewById(R.id.grd_fr_width));
        String frameVertical=getInputText(findViewById(R.id.grd_fr_height));
        String frameMaterial=getInputText(findViewById(R.id.environment_material));
        String boundaryStone=getInputText(findViewById(R.id.boundary_stone));
        String roadWidth=getInputText(findViewById(R.id.road_width));
        String sidewalkWidth=getInputText(findViewById(R.id.sidewalk_width));
        String packingMaterial=getInputText(findViewById(R.id.packing_material));
        String soilPH=getInputText(findViewById(R.id.soil_ph));
        String soilDensity=getInputText(findViewById(R.id.soil_density));

        treeEnvironmentInfoDTO.setFrameHorizontal(Double.parseDouble(frameHorizontal));
        treeEnvironmentInfoDTO.setFrameVertical(Double.parseDouble(frameVertical));
        treeEnvironmentInfoDTO.setFrameMaterial(frameMaterial);
        treeEnvironmentInfoDTO.setBoundaryStone(Double.parseDouble(boundaryStone));
        treeEnvironmentInfoDTO.setRoadWidth(Double.parseDouble(roadWidth));
        treeEnvironmentInfoDTO.setSidewalkWidth(Double.parseDouble(sidewalkWidth));
        treeEnvironmentInfoDTO.setPackingMaterial(packingMaterial);
        treeEnvironmentInfoDTO.setSoilPH(Double.parseDouble(soilPH));
        treeEnvironmentInfoDTO.setSoilDensity(Double.parseDouble(soilDensity));
    }


    public void initMapView(){
        // 초기 세팅하기
        mapView.setCurrentLocationEventListener(new MapView.CurrentLocationEventListener() {
            @Override
            public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {
                mapView.setMapCenterPoint(mapPoint, true);
            }
            @Override
            public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {
            }
            @Override
            public void onCurrentLocationUpdateFailed(MapView mapView) {
            }
            @Override
            public void onCurrentLocationUpdateCancelled(MapView mapView) {
            }
        });

        // 중심점 변경
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.53737528, 127.00557633), true);

    }


}
