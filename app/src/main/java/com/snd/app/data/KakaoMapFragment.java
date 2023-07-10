package com.snd.app.data;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.snd.app.R;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;

public class KakaoMapFragment extends Fragment implements MapView.POIItemEventListener {
    protected String TAG = this.getClass().getName();
    public MapView mapView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.kakaomap_fragment, container, false);

        // KakaoMap 뷰 초기화
        mapView = new MapView(requireContext());
        mapView.setPOIItemEventListener(this);

        // treeBasic_kakao_map 레이아웃에 있는 RelativeLayout 가져오기
        RelativeLayout treeBasicKakaoMapLayout = view.findViewById(R.id.kakao_map);
        // 기존에 있는 모든 뷰들을 제거
        treeBasicKakaoMapLayout.removeAllViews();
        // 새로운 MapView를 추가
        treeBasicKakaoMapLayout.addView(mapView);

        return view;
    }


    public void addMarkers(Double latitude, Double longitude, String idHex){
        Log.d(TAG, "** addMarkers 호출 -**"+latitude+", "+longitude);

        Log.d(TAG, "** addMarkers 호출 -** 맵뷰 실체 "+ mapView);

        //mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
        if(mapView==null){
            Log.d(TAG, "** addMarkers에서 맵뷰 null **");

            return;
        }
        // 중심점 변경
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude), true);

        // 기존 마커 모두 제거
        mapView.removeAllPOIItems();

        ArrayList<MapPOIItem> markerArr = new ArrayList<MapPOIItem>();
        MapPOIItem marker = new MapPOIItem();
        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude));
        marker.setItemName(idHex);
        markerArr.add(marker);

        // 이벤트 리스너 등록
        mapView.setPOIItemEventListener(this);
        mapView.addPOIItems(markerArr.toArray(new MapPOIItem[markerArr.size()]));
    }



    @Override
    public void onResume() {
        super.onResume();
        initMapView();
        mapView.onResume();
    }



    public void initMapView(){
       // mapView=new MapView(getContext());
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



    @Override
    public void onDestroyView() {
        // MapView 제거 및 리소스 해제
        ViewGroup mapViewContainer = requireView().findViewById(R.id.kakao_map);
        mapViewContainer.removeAllViews();
        mapView.removeAllPOIItems();
        mapView.setPOIItemEventListener(null);
        mapView.setCurrentLocationEventListener(null);
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
        mapView.setCurrentLocationRadius(0);
        mapView.setZoomLevel(-2, true);
        mapView = null;

        super.onDestroyView();
    }



    // 리스너 상속된 메서드들
    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
    }
    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
    }
    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {
    }
    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {
    }


}