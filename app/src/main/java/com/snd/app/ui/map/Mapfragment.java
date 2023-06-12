package com.snd.app.ui.map;

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
import com.snd.app.common.TMViewModel;
import com.snd.app.data.LocationRepository;
import com.snd.app.databinding.MainMapFrBinding;

public class Mapfragment extends Fragment {
    private String TAG = this.getClass().getName();
    MainMapFrBinding mapFrBinding;
    MapViewModel mapVM;
    TMActivity tmActivity;

    protected double latitude;
    protected double longitude;

    public Mapfragment(TMActivity tmActivity) {
        this.tmActivity = tmActivity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapVM=new MapViewModel();

        // 위성 개수 추출
        LocationRepository locationRepository=new LocationRepository(getContext());
        locationRepository.setPermissionGranted(true);
        locationRepository.startTracking();

        locationRepository.getSatellites().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer satellitesCount) {
                Log.d(TAG, "현재 위성 개수: " + satellitesCount);
                mapVM._satellites.setValue(satellitesCount);

                if(satellitesCount>6){
                    //tmActivity.getLocation();

                    Log.d(TAG,"** 맵프레그먼트에서 확인 ** "+latitude);
                    Log.d(TAG,"** 맵프레그먼트에서 확인 ** "+longitude);
                    mapVM.latitude.set(""+latitude);
                    mapVM.longitude.set(""+longitude);

                }
            }
        });

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mapFrBinding= DataBindingUtil.inflate(inflater, R.layout.main_map_fr,container, false);
        mapFrBinding.setLifecycleOwner(this);
        mapFrBinding.setMapVM(mapVM);
        return mapFrBinding.getRoot();
    }



}
