package com.snd.app.ui.write;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.snd.app.R;
import com.snd.app.common.TMFragment;
import com.snd.app.data.AppComponent;
import com.snd.app.data.AppModule;
import com.snd.app.data.DaggerAppComponent;
import com.snd.app.data.SpinnerValueListener;
import com.snd.app.databinding.RegistTreeBasicInfoFrBinding;
import com.snd.app.domain.tree.TreeBasicInfoDTO;
import com.snd.app.domain.tree.TreeLocationInfoDTO;
import com.snd.app.ui.tree.PhotoAdapter;
import com.snd.app.ui.tree.SpaceItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;





public class RegistTreeBasicInfoFragment extends TMFragment {
    RegistTreeBasicInfoFrBinding treeBasicInfoActBinding;
    RegistTreeBasicInfoViewModel treeBasicInfoVM;

    private RecyclerView recyclerView;
    public PhotoAdapter photoAdapter;
    public Boolean flag=true;   // 사진 지울시 확인 버튼 감지용

    private static final String DEFAULT_VALUE = "0";
    SharedPreferences sharedPreferences;
    String Authorization;
    String idHex;

    private SpinnerValueListener spinnerValueListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        treeBasicInfoActBinding= DataBindingUtil.inflate(inflater, R.layout.regist_tree_basic_info_fr, container, false);
        treeBasicInfoActBinding.setLifecycleOwner(this);
        // 뷰모델 연결
        treeBasicInfoVM=new ViewModelProvider(getActivity()).get(RegistTreeBasicInfoViewModel.class);
        treeBasicInfoActBinding.setTreeBasicInfoVM(treeBasicInfoVM);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

         Authorization=sharedPreferences.getString("Authorization", null);
         idHex=sharedPreferences.getString("idHex",null);

        recyclerView = treeBasicInfoActBinding.basicRvImage;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.addItemDecoration(new SpaceItemDecoration(20));
        photoAdapter=new PhotoAdapter();
        recyclerView.setAdapter(photoAdapter);

        try {
            setTreeBasicInfoDTO();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        treeBasicInfoVM.listData.observe(getActivity(), new Observer<List<Bitmap>>() {
            @Override
            public void onChanged(List<Bitmap> bitmaps) {
                // 5-3) 변경 감지
                photoAdapter.setImageList(bitmaps);
                Log.d(TAG, "개수 확인"+photoAdapter.getItemCount());
            }
        });

        photoAdapter.tabClick.observe(getActivity(), new Observer() {
            @Override
            public void onChanged(Object o) {
                showAlertDialog();
            }
        });

        treeBasicInfoVM.getResultLiveData().observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean result) {
                Log.d(TAG, " ** 결과 감지!!! ** "+result);
            }
        });

        treeBasicInfoVM.resultLiveData.observe(getActivity(), new Observer() {
            @Override
            public void onChanged(Object o) {
                Log.d(TAG, " ** resultLiveData 결과 감지!!! ** ");

            }
        });

        return treeBasicInfoActBinding.getRoot();
    }// ./onCreate



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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppCompatEditText editText = view.findViewById(R.id.tr_name);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 텍스트가 변경되기 전에 호출됩니다.
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 텍스트가 변경되는 동안 호출됩니다.
                if(s.toString()==null || s.toString().equals("")){
                    Toast.makeText(getContext(), "수목명을 선택해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    treeBasicInfoVM.processUserInput(s.toString());
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                // 텍스트가 변경된 후에 호출됩니다.
            }
        });
    }



    // 여기서 데이터를 넘기는 이유는 SharedPreferences를 참조하기 위함---> 화면에 표시하기
    public void setTreeBasicInfoDTO() throws JsonProcessingException {
        RegistTreeBasicInfoViewModel treeBasicInfoVM=new ViewModelProvider(getActivity()).get(RegistTreeBasicInfoViewModel.class);
        TreeBasicInfoDTO treeBasicInfoDTO=new TreeBasicInfoDTO();

        // 값이 채워진 상태에서 가져오기
        treeBasicInfoDTO.setSubmitter(sharedPreferences.getString("id",null));

        //Log.d(TAG, "** 텍스트만 잘 나오니? **"+getInputText(getActivity().findViewById(R.id.tr_name)));

        treeBasicInfoDTO.setNFC(idHex);
        treeBasicInfoDTO.setVendor(sharedPreferences.getString("company",null));

        Log.d(TAG, "** 디티오 확인 ** "+treeBasicInfoDTO.getSubmitter());

        // 위치 정보
        TreeLocationInfoDTO treeLocationInfoDTO=new TreeLocationInfoDTO();
        treeLocationInfoDTO.setLatitude(Double.parseDouble(sharedPreferences.getString("latitude",null)));
        treeLocationInfoDTO.setLongitude(Double.parseDouble(sharedPreferences.getString("longitude",null)));

        Log.d(TAG, "** 뭘까 ** "+Authorization);

        treeBasicInfoVM.Authorization=Authorization;
        treeBasicInfoVM.setTextViewModel(treeBasicInfoDTO, treeLocationInfoDTO);
    }



    // 1-2) 수목 위치(기본)정보 등록
    public void registerTreeLocationInfo(){
        Log.d(TAG, "** registerTreeLocationInfo 호풀됨 **");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Log.d(TAG, "** 뭘까1 ** "+sharedPreferences);
        Log.d(TAG, "** 뭘까2 ** "+Authorization);
        Log.d(TAG, "** 뭘까3 ** "+idHex);
        Log.d(TAG, "** 뭘까4 ** "+ sharedPreferences.getString("latitude", null));
        Log.d(TAG, "** 뭘까5 ** "+ sharedPreferences.getString("longitude", null));

        JSONObject treeLocationData=new JSONObject();
        try {
            // 입력 데이터 보내기
            String latitudeValue = String.format("%.7f", sharedPreferences.getString("latitude", null));     // 자릿수 맞추기
            treeLocationData.put("latitude", latitudeValue);
            String longitudeValue = String.format("%.7f", sharedPreferences.getString("longitude", null));
            treeLocationData.put("longitude", longitudeValue);
            treeLocationData.put("nfc", idHex);
            treeLocationData.put("submitter",sharedPreferences.getString("id",null));
            treeLocationData.put("vendor", sharedPreferences.getString("company",null));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        AppComponent appComponent = DaggerAppComponent.builder().appModule(new AppModule(null)).build();
        WriteUseCase writeUseCase=appComponent.writeUseCase();
        writeUseCase.registerLocationInfo(treeLocationData, sharedPreferences.getString("Authorization", null));

    }



    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("");
        builder.setMessage("사진을 삭제하시겠습니까?");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 확인 버튼을 눌렀을 때
                flag=true;
                photoAdapter.setAlertDialog(photoAdapter.clickedPosition, flag);
                // 삭제버튼 눌렀다면
                treeBasicInfoVM.cnt-=1;
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 취소 버튼을 눌렀을 때
                flag=false;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public void onResume() {
        super.onResume();
        nextText();
    }



    public void nextText(){
        EditText edit1=getView().findViewById(R.id.tr_name);
        edit1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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



}
