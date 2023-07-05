package com.snd.app.ui.map;

import android.content.Intent;
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
import com.snd.app.domain.tree.TreeTotalDTO;
import com.snd.app.ui.read.GetTreeInfoActivity;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;

public class Mapfragment extends Fragment implements MapView.POIItemEventListener{
    private String TAG = this.getClass().getName();
    MainMapFrBinding mapFrBinding;
    MapViewModel mapVM;
    protected double latitude;
    protected double longitude;
    // 카카오맵
    private MapView mapView;
    public ArrayList<TreeTotalDTO> treeInfoList=new ArrayList<>();
    TMActivity tmActivity;
    private Boolean trackingMode=true;

    // 생성자
    public Mapfragment(TMActivity tmActivity) {
        this.tmActivity = tmActivity;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapVM=new MapViewModel();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mapFrBinding= DataBindingUtil.inflate(inflater, R.layout.main_map_fr, container, false);
        mapFrBinding.setLifecycleOwner(this);
        mapFrBinding.setMapVM(mapVM);
        return mapFrBinding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();
        initMapView();
        mapView.onResume();
        getLocationRepository();
        addMarkers();
    }


    public void initMapView(){
        mapView=new MapView(getContext());
        mapFrBinding.mainKakaoMap.addView(mapView);

        // 초기 세팅하기
        mapView.setCurrentLocationEventListener(new MapView.CurrentLocationEventListener() {
            @Override
            public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {
                mapView.setMapCenterPoint(mapPoint, true);
                //mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
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
                mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);

                Log.d(TAG, "현재 위성 개수: " + satellitesCount);
                mapVM._satellites.setValue(satellitesCount);

                if(satellitesCount>6){
                    latitude=tmActivity.latitude;
                    longitude=tmActivity.longitude;

                    Log.d(TAG, "현재 위성 개수: " + latitude);
                    Log.d(TAG, "현재 위성 개수: " + longitude);

                    // 데이터 렌더링 하기
                    mapVM._latitude.set(""+latitude);
                    mapVM._longitude.set(""+longitude);

                    Log.d(TAG, "** 현재 위치 가져올 수 있나333 ** "+latitude+longitude);
                }
            }
        });
    }


    public void setLocationList(ArrayList<TreeTotalDTO> treeInfoList){
        this.treeInfoList=treeInfoList;
        // 이 두 디티오를 하나의 디티오로 합칠 수 있는 방법!
        addMarkers();
    }


    public void addMarkers(){
        // 기존 마커 모두 제거
        mapView.removeAllPOIItems();

        ArrayList<MapPOIItem> markerArr = new ArrayList<MapPOIItem>();
        for (TreeTotalDTO data : treeInfoList) {
            MapPOIItem marker = new MapPOIItem();
            Log.d(TAG, "** 맵프레그먼트 - 받은 리스트의 위도 **"+data.getLatitude());
            marker.setMapPoint(MapPoint.mapPointWithGeoCoord(data.getLatitude(), data.getLongitude()));
            marker.setItemName((data.getNFC()));
            markerArr.add(marker);
        }
        // 이벤트 리스너 등록
        mapView.setPOIItemEventListener(this);
        mapView.addPOIItems(markerArr.toArray(new MapPOIItem[markerArr.size()]));
    }


    // 마커 클릭시 이벤트 처리
    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
        // 마커 클릭
        String itemName = mapPOIItem.getItemName();
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {
        // 말풍선 클릭
        Intent intent = new Intent(getContext(), GetTreeInfoActivity.class);
        intent.putExtra("IDHEX", mapPOIItem.getItemName());
        startActivity(intent);
    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null) {
            mapView.onPause();
        }
    }


}