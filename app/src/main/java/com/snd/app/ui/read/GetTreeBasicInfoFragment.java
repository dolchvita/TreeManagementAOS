package com.snd.app.ui.read;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.snd.app.R;
import com.snd.app.common.TMFragment;
import com.snd.app.databinding.GetTreeBasicInfoFrBinding;
import com.snd.app.ui.tree.PhotoUrlAdapter;
import com.snd.app.ui.tree.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class GetTreeBasicInfoFragment extends TMFragment {
    GetTreeBasicInfoViewModel getTreeBasicInfoVM;
    PhotoUrlAdapter photoAdapter;
    private RecyclerView recyclerView;
    public Boolean flag=true;   // 사진 지울시 확인 버튼 감지용
    public String idHex;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        GetTreeBasicInfoFrBinding getTreeBasicInfoFrBinding= DataBindingUtil.inflate(inflater, R.layout.get_tree_basic_info_fr, container, false);
        getTreeBasicInfoFrBinding.setLifecycleOwner(this);
        getTreeBasicInfoVM=new ViewModelProvider(getActivity()).get(GetTreeBasicInfoViewModel.class);
        getTreeBasicInfoFrBinding.setGetTreeBasicInfoVM(getTreeBasicInfoVM);

        // 사진 설정
        recyclerView = getTreeBasicInfoFrBinding.getTreeInfoRvImage;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.addItemDecoration(new SpaceItemDecoration(20));
        photoAdapter=new PhotoUrlAdapter();
        recyclerView.setAdapter(photoAdapter);


        if(getPhotoUrl().size()>1){
            photoAdapter.setImageList(getPhotoUrl());
        }

        photoAdapter.tabClick.observe(getActivity(), new Observer() {
            @Override
            public void onChanged(Object o) {
                showAlertDialog();
            }
        });
        return getTreeBasicInfoFrBinding.getRoot();
    }



   public List<String> getPhotoUrl(){
       Log.d(TAG, "** 넘어온 태그 아이디 **"+idHex);
       List<String> photoUrls = new ArrayList<>();

       String sndUrl="http://snd.synology.me:9955/images/";
       String photo1=sndUrl+idHex+"_1.jpg";
       String photo2=sndUrl+idHex+"_2.jpg";

        if(photo1!=null || photo2!=null){
            Log.d(TAG, "** 실제 존재하는지 확인 **"+photo1);
            Log.d(TAG, "** 실제 존재하는지 확인 **"+photo2);
            photoUrls.add(photo1);
            photoUrls.add(photo2);
        }
        return photoUrls;
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



}
