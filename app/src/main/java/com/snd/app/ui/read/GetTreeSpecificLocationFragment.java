package com.snd.app.ui.read;

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
import com.snd.app.ui.write.RegistTreeSpecificLocationInfoViewModel;

public class GetTreeSpecificLocationFragment extends TMFragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RegistTreeSpecificLocationFrBinding registTreeSpecificLocationFrBinding= DataBindingUtil.inflate(inflater, R.layout.regist_tree_specific_location_fr, container, false);
        registTreeSpecificLocationFrBinding.setLifecycleOwner(this);
        RegistTreeSpecificLocationInfoViewModel specificLocationInfoVM=new ViewModelProvider(getActivity()).get(RegistTreeSpecificLocationInfoViewModel.class);
        registTreeSpecificLocationFrBinding.setSpecificLocationVM(specificLocationInfoVM);
        return registTreeSpecificLocationFrBinding.getRoot();
    }



}
