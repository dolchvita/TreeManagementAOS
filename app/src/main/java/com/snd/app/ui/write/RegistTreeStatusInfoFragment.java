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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.snd.app.R;
import com.snd.app.common.TMFragment;
import com.snd.app.data.SpinnerValueListener;
import com.snd.app.databinding.RegistTreeStatusInfoFrBinding;


public class RegistTreeStatusInfoFragment extends TMFragment {
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
        Log.d(TAG, "** 프레그먼트 탄생함**");

        RegistTreeStatusInfoFrBinding registTreeStatusInfoFrBinding= DataBindingUtil.inflate(inflater, R.layout.regist_tree_status_info_fr, container, false);
        registTreeStatusInfoFrBinding.setLifecycleOwner(this);
        RegistTreeStatusInfoViewModel treeStatusInfoVM=new ViewModelProvider(getActivity()).get(RegistTreeStatusInfoViewModel.class);
        registTreeStatusInfoFrBinding.setTreeStatusInfoVM(treeStatusInfoVM);
        return registTreeStatusInfoFrBinding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();

        nextText();

        //스피너 설정
        Spinner spinner=(Spinner) getView().findViewById(R.id.treeStatus_tr_state);
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



    public void nextText(){
        EditText edit1=getView().findViewById(R.id.treeStatus_scarlet_diam);
        EditText edit2=getView().findViewById(R.id.treeStatus_tr_height);
        EditText edit3=getView().findViewById(R.id.treeStatus_crw_height);
        EditText edit4=getView().findViewById(R.id.treeStatus_crw_diam);
        EditText edit5=getView().findViewById(R.id.treeStatus_pest_dmg_state);

        nextFocus(edit1, edit2);
        nextFocus(edit2, edit3);
        nextFocus(edit3, edit4);
        nextFocus(edit4, edit5);

        edit5.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Log.d(TAG, "완료버튼 누름"+actionId);

                    // 키보드 숨기기
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
