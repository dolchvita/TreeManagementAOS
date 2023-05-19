package com.snd.app.common;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;

public class LocationActivity extends TMActivity{

    // 사용자가 허용했는지 확인하는 변수
    private int PERMISSION_REQUEST_CODE = 1;

    private LocationManager locationManager;
    private LocationListener locationListener;
    double latitude;
    double longitude;

    private FusedLocationProviderClient fusedLocationClient;


    public LocationActivity() {
       int code =checkLocationPermission();
       Log.d(TAG, "** 코드 확인 **"+code);

       if(code==1){
           getLocation();
       }
    }

    // 사용자에게 권한 부여
    public int checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // 권한이 허용되지 않은 경우 권한 요청
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_CODE);
        } else {
            // 권한이 이미 허용된 경우
            // GPS 사용에 필요한 초기화 작업을 수행할 수 있습니다.
            //initializeGPS();
        }
        return PERMISSION_REQUEST_CODE;
    }



    // 마지막 위치 정보 가져오기
    private void getLocation() {
        Log.d(TAG,"** 로케이션 함수 호출 **");
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
            Log.d("Location", "Latitude: " + latitude + ", Longitude: " + longitude);
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
