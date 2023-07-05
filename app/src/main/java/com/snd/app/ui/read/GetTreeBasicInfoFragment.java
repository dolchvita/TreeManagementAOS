package com.snd.app.ui.read;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
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
    private RecyclerView recyclerView;
    public Boolean flag=true;   // 사진 지울시 확인 버튼 감지용

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        GetTreeBasicInfoFrBinding getTreeBasicInfoFrBinding= DataBindingUtil.inflate(inflater, R.layout.get_tree_basic_info_fr, container, false);
        getTreeBasicInfoFrBinding.setLifecycleOwner(this);
        GetTreeBasicInfoViewModel getTreeBasicInfoVM=new ViewModelProvider(getActivity()).get(GetTreeBasicInfoViewModel.class);
        getTreeBasicInfoFrBinding.setGetTreeBasicInfoVM(getTreeBasicInfoVM);

        // 웹에서 가져온 이미지 URL 리스트
        List<String> photoUrls = new ArrayList<>();
        photoUrls.add("http://snd.synology.me:9955/images/0495150A635A80_1.jpg");
        photoUrls.add("http://snd.synology.me:9955/images/0495150A635A80_2.jpg");


        // 사진 설정
        recyclerView = getTreeBasicInfoFrBinding.getTreeInfoRvImage;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.addItemDecoration(new SpaceItemDecoration(20));
        PhotoUrlAdapter photoAdapter=new PhotoUrlAdapter();
        recyclerView.setAdapter(photoAdapter);

        photoAdapter.setImageList(photoUrls);

        return getTreeBasicInfoFrBinding.getRoot();
    }



}
