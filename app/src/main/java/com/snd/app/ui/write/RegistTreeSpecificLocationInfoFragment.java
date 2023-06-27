package com.snd.app.ui.write;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.snd.app.R;
import com.snd.app.common.TMFragment;
import com.snd.app.databinding.RegistTreeSpecificLocationFrBinding;
import com.snd.app.domain.tree.TreeSpecificLocationInfoDTO;

public class RegistTreeSpecificLocationInfoFragment extends TMFragment {
    RegistTreeSpecificLocationFrBinding registTreeSpecificLocationFrBinding;
    RegistTreeSpecificLocationInfoViewModel specificLocationInfoVM;
    TreeSpecificLocationInfoDTO treeSpecificLocationInfoDTO;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        registTreeSpecificLocationFrBinding= DataBindingUtil.inflate(inflater, R.layout.regist_tree_specific_location_fr, container, false);
        registTreeSpecificLocationFrBinding.setLifecycleOwner(this);
        specificLocationInfoVM=new ViewModelProvider(getActivity()).get(RegistTreeSpecificLocationInfoViewModel.class);
        registTreeSpecificLocationFrBinding.setSpecificLocationVM(specificLocationInfoVM);

        return registTreeSpecificLocationFrBinding.getRoot();
    }



}
