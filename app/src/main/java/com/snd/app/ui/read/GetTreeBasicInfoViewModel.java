package com.snd.app.ui.read;

import android.util.Log;

import androidx.databinding.ObservableField;

import com.snd.app.common.TMViewModel;
import com.snd.app.domain.tree.TreeIntegratedVO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GetTreeBasicInfoViewModel extends TMViewModel {

    // 기본 정보
    public ObservableField<String> NFC=new ObservableField<>();
    public ObservableField<String> species=new ObservableField<>();
    public ObservableField<String> submitter=new ObservableField<>();
    public ObservableField<String> vendor=new ObservableField<>();
    public ObservableField<String> latitude=new ObservableField<>();
    public ObservableField<String> longitude=new ObservableField<>();
    public ObservableField<String> basicInserted=new ObservableField<>();


    // 데이터바인딩시 참조할 변수 매핑
    public void setTextViewModel(TreeIntegratedVO treeIntegratedVO){
        NFC.set(treeIntegratedVO.getNFC());
        species.set(treeIntegratedVO.getSpecies());
        submitter.set(treeIntegratedVO.getBasicSubmitter());
        vendor.set(treeIntegratedVO.getBasicVendor());
        latitude.set(String.valueOf(treeIntegratedVO.getLatitude()));
        longitude.set(String.valueOf(treeIntegratedVO.getLongitude()));
        basicInserted.set(getBasicInserted(treeIntegratedVO.getBasicInserted()));
    }


    public String getBasicInserted(LocalDateTime now) {
        Log.d(TAG, "** 전달받은 시간 ** "+now);
        String formatDateTime=null;
        if (now != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            formatDateTime = now.format(formatter);
        }else {
            formatDateTime="";
        }
        return formatDateTime;
    }




}
