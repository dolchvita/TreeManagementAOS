package com.snd.app.ui.write;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;

import com.snd.app.R;
import com.snd.app.common.TMActivity;
import com.snd.app.databinding.RegistTreeSpecificLocationActBinding;
import com.snd.app.domain.tree.TreeSpecificLocationInfoDTO;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class RegistTreeSpecificLocationInfoActivity extends TMActivity implements MyCallback, AdapterView.OnItemSelectedListener{
    RegistTreeSpecificLocationActBinding registTreeSpecificLocationActBinding;
    RegistTreeSpecificLocationInfoViewModel specificLocationInfoVM;
    TreeSpecificLocationInfoDTO treeSpecificLocationInfoDTO;
    int num;
    String NFC;
    Boolean sidewalk;
    Spinner spinner;
    private MapView mapView;
    private Bundle mapViewSavedState;

    // 위도와 경도를 어떻게 전달할까?
    double latitude;
    double longitude;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registTreeSpecificLocationActBinding= DataBindingUtil.setContentView(this, R.layout.regist_tree_specific_location_act);
        registTreeSpecificLocationActBinding.setLifecycleOwner(this);
        specificLocationInfoVM=new RegistTreeSpecificLocationInfoViewModel();
        registTreeSpecificLocationActBinding.setSpecificLocationVM(specificLocationInfoVM);
        treeSpecificLocationInfoDTO=new TreeSpecificLocationInfoDTO();
        NFC=getIntent().getStringExtra("NFC");
        specificLocationInfoVM.idHex.set(NFC);
        latitude=getIntent().getDoubleExtra("latitude", latitude);
        longitude=getIntent().getDoubleExtra("longitude", longitude);
        Log.d(TAG, "** 넘어온 좌표값 **"+latitude+longitude);
        // 콜백 연결
        specificLocationInfoVM.setCallback(this);
        // 스피너 설정
        spinner=findViewById(R.id.specificLocation_tr_state);
        ArrayAdapter adapter=ArrayAdapter.createFromResource(this, R.array.treeStatus_pest,  android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

        // 다른 방법 적용할 예정 !
        //MapViewEventListenerImpl mapViewEventListener=new MapViewEventListenerImpl();
        mapView = new MapView(this);
        //mapView.setMapViewEventListener(mapViewEventListener);
        //registTreeSpecificLocationActBinding.specificLocationKakaoMap.addView(mapView);
        Log.d(TAG, "** 있는지 먼저 확인 **"+mapView);


        // 뒤로 가기
        specificLocationInfoVM.back.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegistTreeSpecificLocationInfoActivity.this);
                builder.setTitle("나가시겠습니까?");
                builder.setMessage("입력 중인 내용은 저장되지 않습니다.");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 확인 버튼을 눌렀을 때
                        finish();
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        //mapFragment=new KakaoMapFagment();
        // MapFragment를 추가하는 방법 예시
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //fragmentTransaction.add(R.id.specificLocation_kakao_map, new KakaoMapFagment());
        fragmentTransaction.commit();


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
        // 줌 레벨 변경
        mapView.setZoomLevel(1, true);
        double thisLatitude=latitude;
        double thisLongitude=longitude;

        // 중심점 변경
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude), true);
        addMarkers(thisLatitude, thisLongitude);
    }


    public void addMarkers(Double thisLatitude, Double thisLongitude){
        // 기존 마커 모두 제거
        mapView.removeAllPOIItems();

        ArrayList<MapPOIItem> markerArr = new ArrayList<MapPOIItem>();
        MapPOIItem marker = new MapPOIItem();
        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(thisLatitude, thisLongitude));
        marker.setItemName(NFC);
        markerArr.add(marker);
        // 이벤트 리스너 등록
        // mapView.setPOIItemEventListener(this);
        mapView.addPOIItems(markerArr.toArray(new MapPOIItem[markerArr.size()]));
    }


    // 맵뷰 전달하기
    public MapView getMapView() {
        return mapView;
    }


    @Override
    public void onCustomCallback() {
        //팝업 창 띄우기
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistTreeSpecificLocationInfoActivity.this);
        builder.setTitle("수목 기본 정보 입력이 완료되었습니다.");
        builder.setMessage("이어서 상태 정보를 등록하시겠습니까?");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                num=which;
                registerSpecificLocationInfo();
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                num=which;
                registerSpecificLocationInfo();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }




   public void registerSpecificLocationInfo() {
       setTreeSpecificLocationInfoDTO();

       JSONObject treeSpecificLocationData=new JSONObject();
       OkHttpClient client = new OkHttpClient();
       try {
           treeSpecificLocationData.put("nfc", NFC);
           treeSpecificLocationData.put("sidewalk", sidewalk);
           treeSpecificLocationData.put("distance", treeSpecificLocationInfoDTO.getDistance());
           treeSpecificLocationData.put("carriageway", treeSpecificLocationInfoDTO.getCarriageway());
           Log.d(TAG, "** 보낼 데이터 모습 ! ** "+treeSpecificLocationData);
       } catch (JSONException e){
           e.printStackTrace();
       }
       RequestBody requestBody=RequestBody.create(MediaType.parse("application/json"), treeSpecificLocationData.toString());
       String url = sndUrl+"/app/tree/registerSpecificLocationInfo";
       Request request = new Request.Builder()
               .url(url)
               .addHeader("Authorization", sharedPreferences.getString("Authorization",null))
               .patch(requestBody)
               .build();
       client.newCall(request).enqueue(new Callback() {
           @Override
           public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) throws IOException {
               if (response.isSuccessful()){
                   String responseData = response.body().string();
                   Log.d(TAG,"** 위치 상세 등록 성공 / 응답 **"+responseData);

                   if(num==-1){
                       // 확인 버튼을 눌렀을 때
                       Intent intent=new Intent(RegistTreeSpecificLocationInfoActivity.this, RegistTreeStatusInfoActivity.class);
                       intent.putExtra("NFC",  NFC);
                       intent.putExtra("latitude",  latitude);
                       intent.putExtra("longitude",  longitude);
                       startActivity(intent);
                   }else {
                       // 취소 버튼을 눌렀을 때
                       Intent intent=new Intent(RegistTreeSpecificLocationInfoActivity.this, RegistEnvironmentInfoActivity.class);
                       startActivity(intent);
                   }
               }else{
                   String responseData = response.body().string();
                   Log.d(TAG,"** 위치 상세 등록 실패 / 응답 **"+responseData);
               }
           }
           @Override
           public void onFailure(Call call, IOException e) {
               // 여기에 요청이 실패했을 때 실행될 코드를 작성하세요.
               Log.d(TAG,"** 위치 상세 오류남 **");
           }
       });
       Toast.makeText(this, "등록되었습니다", Toast.LENGTH_SHORT).show();
   }


  public void setTreeSpecificLocationInfoDTO(){
      String carriageway=getInputText(findViewById(R.id.specificLocation_carriageway));
      String distance=getInputText(findViewById(R.id.specificLocation_distance));
      treeSpecificLocationInfoDTO.setCarriageway(Integer.parseInt(carriageway));
      treeSpecificLocationInfoDTO.setDistance(Double.parseDouble(distance));
    }


    @Override
    protected void onResume() {
        super.onResume();
        spinner.setOnItemSelectedListener(this);
        if(mapView != null) {
            mapView.onResume();
            initMapView();
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position==0){
            sidewalk=false;
        } else {
            sidewalk=true;
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }


}
