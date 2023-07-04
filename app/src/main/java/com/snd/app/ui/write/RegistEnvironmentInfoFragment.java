package com.snd.app.ui.write;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.snd.app.R;
import com.snd.app.common.TMFragment;
import com.snd.app.databinding.RegistEnvironmentInfoFrBinding;
import com.snd.app.databinding.RegistEnvironmentInfoFrBindingImpl;

public class RegistEnvironmentInfoFragment extends TMFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RegistEnvironmentInfoFrBinding registEnvironmentInfoFrBinding= DataBindingUtil.inflate(inflater, R.layout.regist_environment_info_fr, container, false);
        registEnvironmentInfoFrBinding.setLifecycleOwner(this);
        RegistEnvironmentInfoViewModel registEnvironmentInfoVM=new ViewModelProvider(getActivity()).get(RegistEnvironmentInfoViewModel.class);
        registEnvironmentInfoFrBinding.setEnvironmentVM(registEnvironmentInfoVM);
        return registEnvironmentInfoFrBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        nextText();
    }



    public void nextText(){
        EditText edit1=getView().findViewById(R.id.grd_fr_width);
        EditText edit2=getView().findViewById(R.id.grd_fr_height);
        EditText edit3=getView().findViewById(R.id.environment_material);
        EditText edit4=getView().findViewById(R.id.boundary_stone);
        EditText edit5=getView().findViewById(R.id.road_width);
        EditText edit6=getView().findViewById(R.id.sidewalk_width);
        EditText edit7=getView().findViewById(R.id.packing_material);
        EditText edit8=getView().findViewById(R.id.soil_ph);
        EditText edit9=getView().findViewById(R.id.soil_density);

        nextFocus(edit1, edit2);
        nextFocus(edit2, edit3);
        nextFocus(edit3, edit4);
        nextFocus(edit4, edit5);
        nextFocus(edit5, edit6);
        nextFocus(edit6, edit7);
        nextFocus(edit7, edit8);
        nextFocus(edit8, edit9);

        edit9.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    // 키보드 숨기기
                    Log.d(TAG,"** 다음 버튼 누름 **");

                    InputMethodManager imm = (InputMethodManager) getView().getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
    }


    public void nextFocus(EditText editText1, EditText editText2){
        editText1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    editText2.requestFocus();
                    return true;
                }
                return false;
            }
        });
    }




}
