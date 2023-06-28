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
import com.snd.app.databinding.GetTreeBasicInfoFrBinding;

public class GetTreeBasicInfoFragment extends TMFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        GetTreeBasicInfoFrBinding getTreeBasicInfoFrBinding= DataBindingUtil.inflate(inflater, R.layout.get_tree_basic_info_fr, container, false);
        getTreeBasicInfoFrBinding.setLifecycleOwner(this);
        GetTreeBasicInfoViewModel getTreeBasicInfoVM=new ViewModelProvider(getActivity()).get(GetTreeBasicInfoViewModel.class);
        getTreeBasicInfoFrBinding.setGetTreeBasicInfoVM(getTreeBasicInfoVM);
        return getTreeBasicInfoFrBinding.getRoot();
    }


}
