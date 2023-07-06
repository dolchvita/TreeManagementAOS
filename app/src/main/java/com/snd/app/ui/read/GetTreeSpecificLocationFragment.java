package com.snd.app.ui.read;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.snd.app.R;
import com.snd.app.common.TMFragment;
import com.snd.app.databinding.GetTreeSpecificLocationFrBinding;

public class GetTreeSpecificLocationFragment extends TMFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        GetTreeSpecificLocationFrBinding getTreeSpecificLocationFrBinding= DataBindingUtil.inflate(inflater, R.layout.get_tree_specific_location_fr, container, false);
        getTreeSpecificLocationFrBinding.setLifecycleOwner(this);
        GetTreeSpecificLocationViewModel getTreeSpecificLocationVM=new GetTreeSpecificLocationViewModel();
        getTreeSpecificLocationFrBinding.setSpecificLocationVM(getTreeSpecificLocationVM);
        return getTreeSpecificLocationFrBinding.getRoot();
    }

}
