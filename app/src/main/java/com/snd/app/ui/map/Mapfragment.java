package com.snd.app.ui.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.snd.app.R;
import com.snd.app.common.TMViewModel;
import com.snd.app.databinding.MainMapFrBinding;

public class Mapfragment extends Fragment {
    private String TAG = this.getClass().getName();
    MainMapFrBinding mapFrBinding;
    MapViewModel mapVM;
    private TMViewModel tmVM;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tmVM=new TMViewModel();

        /*
        LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            return;
        }

         */

        //locationManager.registerGnssStatusCallback(gnssStatus);



    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mapFrBinding= DataBindingUtil.inflate(inflater, R.layout.main_map_fr,container, false);
        mapFrBinding.setLifecycleOwner(this);
        mapFrBinding.setTmVM(tmVM);
        return mapFrBinding.getRoot();
    }



}
