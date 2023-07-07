package com.snd.app.ui.read;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.snd.app.R;
import com.snd.app.common.TMFragment;
import com.snd.app.databinding.GetTreeSpecificLocationFrBinding;

public class GetTreeSpecificLocationFragment extends TMFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "무슨 문제라도 있니");
        GetTreeSpecificLocationFrBinding getTreeSpecificLocationFrBinding= DataBindingUtil.inflate(inflater, R.layout.get_tree_specific_location_fr, container, false);
        getTreeSpecificLocationFrBinding.setLifecycleOwner(this);
        GetTreeSpecificLocationViewModel getTreeSpecificLocationVM=new ViewModelProvider(this).get(GetTreeSpecificLocationViewModel.class);
        getTreeSpecificLocationFrBinding.setSpecificLocationVM(getTreeSpecificLocationVM);
        return getTreeSpecificLocationFrBinding.getRoot();
    }




}
