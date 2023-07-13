package com.snd.app.ui.write;

import android.Manifest;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.snd.app.R;
import com.snd.app.common.TMFragment;
import com.snd.app.data.LocationRepository;

public class MapLoadingFragment extends TMFragment {
    String idHex;
    Boolean callMethod1=false;
    Boolean callMethod2=false;
    Boolean callMethod3=false;
    LocationCallback locationCallback;
    LocationRepository locationRepository;
    FusedLocationProviderClient client;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        idHex=((RegistTreeInfoActivity)getActivity()).idHex;
        satellitesCount();

        return  inflater.inflate(R.layout.map_loading_fr, container, false);
    }



    public void satellitesCount(){
        // 위성 개수 추출
        locationRepository=new LocationRepository(getContext());
        locationRepository.setPermissionGranted(true);
        locationRepository.startTracking();
        locationRepository.getSatellites().observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer satellitesCount) {

                Log.d(TAG, "현재 위성 개수: " + satellitesCount);

                if (satellitesCount>1){
                    //findViewById(R.id.loading_layout_box).setVisibility(View.GONE);
                    if(!callMethod1){
                        getActivity().findViewById(R.id.write_cancel).setBackgroundColor(getResources().getColor(R.color.cocoa_brown));   // 저장 버튼 활성화
                        ((RegistTreeInfoActivity) getActivity()).setClick();
                        callMethod1=true;
                    }
                    if(!callMethod3){
                        checkLocationAccuracy();
                    }
                }
            }
        });
    }



    // 오차 범위 설정하는 메서드 -> 마지막 위치 대신 가져오기
    public void checkLocationAccuracy() {
        Log.d(TAG,"** checkLocationAccuracy 함수 호출 **");

        Activity activity = getActivity();
        if (activity != null) {
            client = LocationServices.getFusedLocationProviderClient(activity);
            // 나머지 코드...
        }
        //client = LocationServices.getFusedLocationProviderClient(getActivity());
        // 권한 확인
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        client.requestLocationUpdates(new LocationRequest(), locationCallback= new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    Log.d(TAG, "** locationResult 없음** ");
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location.getAccuracy() <= 15) {  // 오차 범위가 30미터 이내인 경우
                        Log.d(TAG, "Location with good accuracy: " + location.getLatitude() + ", " + location.getLongitude());

                        if(!callMethod2){
                            Double latitude = location.getLatitude();
                            Double longitude = location.getLongitude();
                            Log.d(TAG, "** 위도11: " + latitude + ", 경도11 : " + longitude);

                            saveLocation(latitude, longitude);

                            ((RegistTreeInfoActivity)getActivity()).saveGpsLocation();

                            callMethod2=true;
                            callMethod3=true;
                        }
                        //getTreeLocation();
                    }
                }
            }
        }, null);
    }



    // 위치 좌표 최초 저장
    public void saveLocation(Double latitude, Double longitude){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("latitude", String.valueOf(latitude));
        editor.putString("longitude", String.valueOf(longitude));
        editor.apply();
    }




    @Override
    public void onPause() {
        super.onPause();
        if (locationCallback != null) {
            LocationServices.getFusedLocationProviderClient(getActivity()).removeLocationUpdates(locationCallback);
        }
        locationRepository.getSatellites().removeObservers(this);
        if (client != null && locationCallback != null) {
            client.removeLocationUpdates(locationCallback);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


}
