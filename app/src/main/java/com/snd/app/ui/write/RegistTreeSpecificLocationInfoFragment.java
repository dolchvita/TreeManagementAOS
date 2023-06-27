package com.snd.app.ui.write;

import android.content.Context;
import android.os.Bundle;
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
import com.snd.app.databinding.RegistTreeSpecificLocationFrBinding;

public class RegistTreeSpecificLocationInfoFragment extends TMFragment {
    RegistTreeSpecificLocationFrBinding registTreeSpecificLocationFrBinding;
    RegistTreeSpecificLocationInfoViewModel specificLocationInfoVM;
    private SpinnerValueListener spinnerValueListener;


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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        registTreeSpecificLocationFrBinding= DataBindingUtil.inflate(inflater, R.layout.regist_tree_specific_location_fr, container, false);
        registTreeSpecificLocationFrBinding.setLifecycleOwner(this);
        specificLocationInfoVM=new ViewModelProvider(getActivity()).get(RegistTreeSpecificLocationInfoViewModel.class);
        registTreeSpecificLocationFrBinding.setSpecificLocationVM(specificLocationInfoVM);

        return registTreeSpecificLocationFrBinding.getRoot();
    }



    @Override
    public void onResume() {
        super.onResume();
        // 레이아웃이 참조가 안되고 있는 건 아닐까? -> 정답

        // 스피너 설정
        Spinner spinner=(Spinner) getView().findViewById(R.id.specificLocation_tr_state);
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
