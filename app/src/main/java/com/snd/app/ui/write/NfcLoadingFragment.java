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
import com.snd.app.databinding.TreeWriteFrBinding;
import com.snd.app.ui.tree.TreeViewModel;

public class NfcLoadingFragment extends TMFragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TreeWriteFrBinding treeWriteFrBinding=DataBindingUtil.inflate(inflater, R.layout.tree_write_fr, container, false);
        treeWriteFrBinding.setLifecycleOwner(this);
        TreeViewModel treeVM=new ViewModelProvider(getActivity()).get(TreeViewModel.class);
        treeWriteFrBinding.setTreeVM(treeVM);
        return treeWriteFrBinding.getRoot();
    }

}
