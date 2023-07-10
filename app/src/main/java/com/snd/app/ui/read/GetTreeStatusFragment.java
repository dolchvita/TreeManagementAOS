package com.snd.app.ui.read;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.snd.app.R;
import com.snd.app.common.TMFragment;
import com.snd.app.data.SpinnerValueListener;
import com.snd.app.databinding.GetTreeStatusInfoFrBinding;
import com.snd.app.databinding.RegistTreeStatusInfoFrBinding;
import com.snd.app.ui.write.RegistTreeStatusInfoViewModel;

public class GetTreeStatusFragment extends TMFragment {
    private SpinnerValueListener spinnerValueListener;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        GetTreeStatusInfoFrBinding getTreeStatusInfoFrBinding=DataBindingUtil.inflate(inflater, R.layout.get_tree_status_info_fr, container, false);
        getTreeStatusInfoFrBinding.setLifecycleOwner(this);
        GetTreeStatusViewModel getTreeStatusVM=new ViewModelProvider(this).get(GetTreeStatusViewModel.class);
        getTreeStatusInfoFrBinding.setTreeStatusInfoVM(getTreeStatusVM);
        return getTreeStatusInfoFrBinding.getRoot();
    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // 액티비티가 인터페이스를 구현하고 있는지 확인
        if (context instanceof SpinnerValueListener) {
            spinnerValueListener = (SpinnerValueListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement SpinnerValueListener");
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        // 스피너 설정
        Spinner spinner=(Spinner) getView().findViewById(R.id.get_treeStatus_tr_state);
        ArrayAdapter adapter=ArrayAdapter.createFromResource(getContext(), R.array.treeStatus_pest,  android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 선택된 값 가져오기
                String selectedValue = spinner.getSelectedItem().toString();
                // 액티비티로 선택된 값 전달
                spinnerValueListener.onSpinnerValueChanged(selectedValue);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 아무것도 선택되지 않았을 때 처리

            }
        });
    }


}
