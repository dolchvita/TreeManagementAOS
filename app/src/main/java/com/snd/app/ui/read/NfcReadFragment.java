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
import com.snd.app.databinding.TreeReadFrBinding;
import com.snd.app.ui.tree.TreeViewModel;

public class NfcReadFragment extends TMFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TreeReadFrBinding treeReadFrBinding= DataBindingUtil.inflate(inflater, R.layout.tree_read_fr, container, false);
        treeReadFrBinding.setLifecycleOwner(this);
        TreeViewModel treeVM=new ViewModelProvider(getActivity()).get(TreeViewModel.class);
        treeReadFrBinding.setTreeVM(treeVM);
        return treeReadFrBinding.getRoot();
    }
}
