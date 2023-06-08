package com.snd.app.common;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;

// 최대한 적은 양의 로직을 갖고 있기
public class LocationActivity extends TMActivity{

    // 사용자가 허용했는지 확인하는 변수
   protected static final int PERMISSION_REQUEST_CODE = 1;

    private LocationManager locationManager;
    private LocationListener locationListener;
    protected  double latitude;
    protected double longitude;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"** LocationActivity 생성 **");
        //locationPermission();
    }





   // 권한에 대한 결과 (자동 호출)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG,"** LocationActivity- onRequestPermissionsResult 코드 확인 ** "+requestCode);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //getLocation();
                //startLocationUpdates();
                // 다음을 위한 코드가 없음
            } else {
                // 실행부가 왜 여기로 쳐 들어오냐  -> requestCode가 1로 들어오지 않은 것.
                Toast.makeText(this, "위치 권한이 거부되어 앱을 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


    private void startLocationUpdates(){
        Log.d(TAG,"** LocationActivity- 메서드 startLocationUpdates 호출 **");
        //getLocation();
    }


    // 마지막 위치 정보 가져오기
    protected void getLocation() {
        Log.d(TAG,"** LocationActivity- getLocation 함수 호출 **");
        //LocationRequest locationRequest = new LocationRequest();
        //locationRequest.setInterval(10000); // 위치 업데이트 간격 (밀리초)
        //locationRequest.setFastestInterval(5000);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // 위치 권한이 없는 경우 권한 요청 로직을 추가해야 합니다.
            return;
        }

        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastKnownLocation != null) {
            latitude = lastKnownLocation.getLatitude();
            longitude = lastKnownLocation.getLongitude();

            // 위치 정보를 사용하여 필요한 작업을 수행하세요.
            // 예: 위도와 경도를 로그에 출력
            Log.d(TAG, "Latitude: " + latitude + ", Longitude: " + longitude);
        } else {
            // 위치 정보를 가져오지 못한 경우
            // 필요한 처리를 수행하세요.
        }
    }


    // 위치 좌표값 받아오는 메서드
    private void initializeGPS() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // GPS 위치 업데이트 요청 등의 작업을 수행합니다.
        // 예시로는 GPS 위치 업데이트를 1초마다 요청하도록 설정한 것입니다.
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000, 0, locationListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }


    // 앱 종료 시 GPS 사용 해제
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }


}
