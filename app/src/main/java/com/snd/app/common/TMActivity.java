package com.snd.app.common;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.GnssStatus;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;

import com.snd.app.data.GpsStatusHelper;

// 모든 액티비티가 상속받을 최상위 객체
public class TMActivity extends AppCompatActivity {
   protected String TAG = this.getClass().getName();
   protected String sndUrl = "http://snd.synology.me:9955";
   protected SharedPreferences sharedPreferences;
   protected SharedPreferences.Editor editor;
   // 이미지 권한
   private static final int REQUEST_IMAGE_CAPTURE = 1;
   protected int REQUEST_IMAGE_CODE = 0;
   // 위치 권한
   protected static final int REQUEST_LOCATION_PERMISSION = 1;
   private LocationManager locationManager;
   private LocationListener locationListener;
   protected double latitude;
   protected double longitude;
   Boolean isGranted = false;

   TMViewModel tmVM;
   // 위성 개수 세기 - 이건 액티비티가 아니라 뷰모델이 가지고 있기
   private GnssStatus.Callback gnssCallback;

   private GpsStatusHelper gpsStatusHelper;

   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      Log.d(TAG, "** TMActivity 생성 **");
      sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
      editor = sharedPreferences.edit();
      tmVM = new TMViewModel();
      Log.d(TAG, "** TMActivity 생성 ** " + tmVM);

      // 매니저 초기화
      locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
      // 리스너 미리 초기화
      locationListener=new LocationListener() {
         @Override
         public void onLocationChanged(@NonNull Location location) {
            Log.d(TAG, "** onLocationChanged 호출 **");
         }

         @Override
         public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d(TAG, "onStatusChanged: " + provider);
         }

         @Override
         public void onProviderEnabled(String provider) {
            Log.d(TAG, "onProviderEnabled: " + provider);
         }

         @Override
         public void onProviderDisabled(String provider) {
            Log.d(TAG, "onProviderDisabled: " + provider);
         }
      };

      requestPermissions();
   }


   // 1 제일 먼저 동작하는 메서드 !
   protected void requestPermissions() {
      // 1 카메라 권한
      // 2 사진 저장할 수 있는 권한
      // 3 위치 권한
      if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
              != PackageManager.PERMISSION_GRANTED ||
              ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                      != PackageManager.PERMISSION_GRANTED ||
              ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                      != PackageManager.PERMISSION_GRANTED
      ) {
         ActivityCompat.requestPermissions(this,
                 new String[]{android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                         Manifest.permission.ACCESS_FINE_LOCATION},
                 REQUEST_IMAGE_CAPTURE);
      } else {
         // 이미 권한이 허용된 경우에 대한 처리
         REQUEST_IMAGE_CODE = 1;
         locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

         // 위치 권한이 허용된 경우
         //startLocationUpdates();
      }
   }


   // 2 권한 요청에 대한 결과
   @Override
   public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
      super.onRequestPermissionsResult(requestCode, permissions, grantResults);
      Log.d(TAG, "** onRequestPermissionsResult 호출 **");

      // 카메라 허용
      if (requestCode == REQUEST_IMAGE_CAPTURE) {
         if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            editor.putBoolean("camera_permission_granted", true);
            editor.apply();
         } else {
            editor.putBoolean("camera_permission_granted", false);
            editor.apply();
         }
      }
      // 위치 허용
      if (requestCode == REQUEST_LOCATION_PERMISSION) {
         if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "** onRequestPermissionsResult - 위치 허용 **");

         } else {
            // 실행부가 왜 여기로 쳐 들어오냐  -> requestCode가 1로 들어오지 않은 것.
            Toast.makeText(this, "위치 권한이 거부되어 앱을 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
            finish();
         }
      }
   }



   public void  locationPermission(){
      if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
              != PackageManager.PERMISSION_GRANTED) {
         // 권한이 허용되지 않은 경우 권한 요청
         ActivityCompat.requestPermissions(this,
                 new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                 REQUEST_LOCATION_PERMISSION);
      } else {
         // 권한이 이미 허용된 경우
         // GPS 사용에 필요한 초기화 작업을 수행할 수 있습니다.
         Log.d(TAG,"** LocationActivity - 권한 허용됨 **");
      }
   }


   // 마지막 위치 정보 가져오기
   protected void getLocation() {
      Log.d(TAG,"** getLocation 함수 호출 **");
      LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

      // 위치 권한이 없는 경우 권한 요청 로직을 추가해야 합니다.
      if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
              != PackageManager.PERMISSION_GRANTED) {
         return;
      }

      Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

      gpsStatusHelper=new GpsStatusHelper(locationManager, this);
      Log.d(TAG,"** getLocation 함수 - gpsStatusHelper ** "+gpsStatusHelper);

      if (lastKnownLocation != null) {
         latitude = lastKnownLocation.getLatitude();
         longitude = lastKnownLocation.getLongitude();
         Log.d(TAG, "** 위도: " + latitude + ", 경도 : " + longitude);

      } else {
         // 위치 정보를 가져오지 못한 경우
      }

   }


   // 앱 종료 시 GPS 사용 해제
   @Override
   protected void onDestroy() {
      Log.d(TAG, "** on Destroy 호출 **");
      super.onDestroy();
      if (locationManager != null) {
         locationManager.removeUpdates(locationListener);
      }
   }


}