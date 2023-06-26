package com.snd.app.ui.write;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.snd.app.R;
import com.snd.app.common.TMActivity;
import com.snd.app.databinding.WriteActBinding;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

public class RegistTreeInfoActivity extends TMActivity implements MyCallback, MapView.POIItemEventListener{
    WriteActBinding writeActBinding;
    RegistTreeInfoViewModel treeInfoVM;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        writeActBinding= DataBindingUtil.setContentView(this, R.layout.write_act);
        writeActBinding.setLifecycleOwner(this);
        treeInfoVM=new RegistTreeInfoViewModel();
        writeActBinding.setTreeInfoVM(treeInfoVM);

    }



    // 리스너 메소드들
    @Override
    public void onCustomCallback() {
    }
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
