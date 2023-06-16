package com.snd.app.ui.write;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import com.snd.app.R;
import com.snd.app.common.TMActivity;
import com.snd.app.databinding.RegistTreeStatusInfoActBinding;
import com.snd.app.domain.tree.TreeStatusInfoDTO;

// 수목 상태 정보 등록
public class RegistTreeStatusInfoActivity extends TMActivity implements MyCallback{

    RegistTreeStatusInfoActBinding treeStatusInfoBinding;
    RegistTreeStatusInfoViewModel treeStatusInfoVM;
    TreeStatusInfoDTO statusInfoDTO;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        treeStatusInfoBinding= DataBindingUtil.setContentView(this, R.layout.regist_tree_status_info_act);
        treeStatusInfoBinding.setLifecycleOwner(this);
        treeStatusInfoVM=new RegistTreeStatusInfoViewModel();
        treeStatusInfoBinding.setTreeStatusInfoVM(treeStatusInfoVM);
        // 콜백 연결
        treeStatusInfoVM.setCallback(this);
    }


    @Override
    public void onCustomCallback() {
        // 레이아웃 매핑
        AppCompatEditText editText1=findViewById(R.id.treeStatus_scarlet_diam);
        String dbh= String.valueOf(editText1.getText());
        AppCompatEditText editText2=findViewById(R.id.treeStatus_tr_height);
        String rcc= String.valueOf(editText1.getText());
        AppCompatEditText editText3=findViewById(R.id.treeStatus_crw_height);
        String height= String.valueOf(editText1.getText());
        AppCompatEditText editText4=findViewById(R.id.treeStatus_crw_diam);
        String length= String.valueOf(editText1.getText());
        AppCompatEditText editText5=findViewById(R.id.treeStatus_pest_dmg_state);
        String width= String.valueOf(editText1.getText());
        AppCompatEditText editText6=findViewById(R.id.treeStatus_tr_state);
        String pest= String.valueOf(editText1.getText());

        statusInfoDTO=new TreeStatusInfoDTO();
        statusInfoDTO.setDBH(Double.parseDouble(dbh));
        statusInfoDTO.setRCC(Double.parseDouble(rcc));
        statusInfoDTO.setHeight(Double.parseDouble(height));
        statusInfoDTO.setLength(Double.parseDouble(length));
        statusInfoDTO.setWidth(Double.parseDouble(width));
        statusInfoDTO.setPest(true);

        Log.d(TAG, "**수목 상태 정보 확인 !! **");
        Log.d(TAG, "** 내용 확인 **");

    }



}
