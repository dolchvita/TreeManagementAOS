package com.snd.app.ui.write;

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
import com.snd.app.databinding.TreeFrBinding;
import com.snd.app.ui.tree.TreeViewModel;

public class NfcLoadingFragment extends TMFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "** 로딩 프레그먼트 출력? **");

        TreeFrBinding treeFrBinding=DataBindingUtil.inflate(inflater, R.layout.tree_fr, container, false);
        treeFrBinding.setLifecycleOwner(this);
        TreeViewModel treeVM=new ViewModelProvider(getActivity()).get(TreeViewModel.class);
        treeFrBinding.setTreeVM(treeVM);
        return treeFrBinding.getRoot();
    }

}
