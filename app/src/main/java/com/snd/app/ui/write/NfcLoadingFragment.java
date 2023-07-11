package com.snd.app.ui.write;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
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
import com.snd.app.data.NfcManager;
import com.snd.app.databinding.TreeWriteFrBinding;
import com.snd.app.ui.tree.TreeViewModel;

public class NfcLoadingFragment extends TMFragment {
    String idHex;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TreeWriteFrBinding treeWriteFrBinding=DataBindingUtil.inflate(inflater, R.layout.tree_write_fr, container, false);
        treeWriteFrBinding.setLifecycleOwner(this);
        TreeViewModel treeVM=new ViewModelProvider(getActivity()).get(TreeViewModel.class);
        treeWriteFrBinding.setTreeVM(treeVM);
        return treeWriteFrBinding.getRoot();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
