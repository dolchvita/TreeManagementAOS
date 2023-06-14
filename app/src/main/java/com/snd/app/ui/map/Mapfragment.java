package com.snd.app.ui.map;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.snd.app.R;
import com.snd.app.common.TMActivity;
import com.snd.app.data.LocationRepository;
import com.snd.app.databinding.MainMapFrBinding;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

public class Mapfragment extends Fragment {
    private String TAG = this.getClass().getName();
    MainMapFrBinding mapFrBinding;
    MapViewModel mapVM;
    TMActivity tmActivity;

    protected double latitude;
    protected double longitude;

    private MapView mapView;


    // 생성자
    public Mapfragment(TMActivity tmActivity) {
        this.tmActivity = tmActivity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"** 맵 프레그먼트 생성 **");

        mapVM=new MapViewModel();

        getLocationRepository();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mapFrBinding= DataBindingUtil.inflate(inflater, R.layout.main_map_fr,container, false);
        mapFrBinding.setLifecycleOwner(this);
        mapFrBinding.setMapVM(mapVM);
        return mapFrBinding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();
        initMapView();
        mapView.onResume();
    }


    public void initMapView(){
        mapView=new MapView(getContext());
        mapView.setDaumMapApiKey("3c68f3d9c6ea0dee252cdc4da2349b3f");
        mapFrBinding.kakaoMap.addView(mapView);

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

        // 현재 위치 표시 활성화 - 트래킹 모드 !
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
    }


    // 위성 개수 추출 및 좌표값 실시간 렌더링
    public void getLocationRepository(){
        LocationRepository locationRepository=new LocationRepository(getContext());
        locationRepository.setPermissionGranted(true);
        locationRepository.startTracking();

        // 여기서 감지를 하면 맵 버튼을 누를 때마다 리셋이 됨  --> 추후 수정 예정
        locationRepository.getSatellites().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer satellitesCount) {
                Log.d(TAG, "현재 위성 개수: " + satellitesCount);
                mapVM._satellites.setValue(satellitesCount);

                tmActivity.getLocation();
                latitude=tmActivity.latitude;
                longitude=tmActivity.longitude;

                Log.d(TAG, "현재 위성 개수: " + latitude);
                Log.d(TAG, "현재 위성 개수: " + longitude);

                // 데이터 렌더링 하기
                mapVM._latitude.set(""+latitude);
                mapVM._longitude.set(""+longitude);
            }
        });
    }


}