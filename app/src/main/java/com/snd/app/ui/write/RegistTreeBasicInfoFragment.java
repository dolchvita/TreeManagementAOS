package com.snd.app.ui.write;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.snd.app.R;
import com.snd.app.common.TMFragment;
import com.snd.app.databinding.RegistTreeBasicInfoFrBinding;
import com.snd.app.ui.tree.PhotoAdapter;
import com.snd.app.ui.tree.SpaceItemDecoration;

import java.util.List;

public class RegistTreeBasicInfoFragment extends TMFragment {
    RegistTreeBasicInfoFrBinding treeBasicInfoActBinding;
    RegistTreeBasicInfoViewModel treeBasicInfoVM;

    private RecyclerView recyclerView;
    public PhotoAdapter photoAdapter;
    public Boolean flag=true;   // 사진 지울시 확인 버튼 감지용

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        treeBasicInfoActBinding= DataBindingUtil.inflate(inflater, R.layout.regist_tree_basic_info_fr, container, false);
        treeBasicInfoActBinding.setLifecycleOwner(this);
        // 뷰모델 연결
        treeBasicInfoVM=new ViewModelProvider(getActivity()).get(RegistTreeBasicInfoViewModel.class);
        treeBasicInfoActBinding.setTreeBasicInfoVM(treeBasicInfoVM);

        recyclerView = treeBasicInfoActBinding.basicRvImage;
        Log.d(TAG, "** 무슨 문제라도? **"+recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.addItemDecoration(new SpaceItemDecoration(20));
        photoAdapter=new PhotoAdapter();
        recyclerView.setAdapter(photoAdapter);


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
        return treeBasicInfoActBinding.getRoot();
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
