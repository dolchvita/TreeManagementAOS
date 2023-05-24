package com.snd.app.common;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

// 모든 액티비티가 상속받을 최상위 객체
public class TMActivity extends AppCompatActivity {

   protected String TAG=this.getClass().getName();
   protected String sndUrl="http://snd.synology.me:9955";

   // 이미지 권한
   private static final int REQUEST_IMAGE_CAPTURE = 1;
   protected int REQUEST_IMAGE_CODE = 0;

   // 사진 저장에 대한 권한
   private static final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 2;

   protected SharedPreferences sharedPreferences;
   protected SharedPreferences.Editor editor;

   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      requestPermissions();

      // 카메라 권한 담을 SP 객체

      sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
      editor = sharedPreferences.edit();
   }


   // 카메라 접근 권한
   protected void requestPermissions() {
      if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
              != PackageManager.PERMISSION_GRANTED ||
              ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                      != PackageManager.PERMISSION_GRANTED) {
         ActivityCompat.requestPermissions(this,
                 new String[]{android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                 REQUEST_IMAGE_CAPTURE);

      } else {
         // 이미 권한이 허용된 경우에 대한 처리
         REQUEST_IMAGE_CODE=1;
      }
   }


   @Override
   public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
      super.onRequestPermissionsResult(requestCode, permissions, grantResults);

      if (requestCode == REQUEST_IMAGE_CAPTURE) {
         if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // 권한이 허용된 경우
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("camera_permission_granted", true);
            editor.apply();
         } else {
            // 권한이 거부된 경우
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("camera_permission_granted", false);
            editor.apply();
         }
      }
   }



}
