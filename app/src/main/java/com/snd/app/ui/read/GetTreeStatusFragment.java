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
import com.snd.app.databinding.RegistTreeStatusInfoFrBinding;
import com.snd.app.ui.write.RegistTreeStatusInfoViewModel;

public class GetTreeStatusFragment extends TMFragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RegistTreeStatusInfoFrBinding registTreeStatusInfoFrBinding= DataBindingUtil.inflate(inflater, R.layout.regist_tree_status_info_fr, container, false);
        registTreeStatusInfoFrBinding.setLifecycleOwner(this);
        RegistTreeStatusInfoViewModel treeStatusInfoVM=new ViewModelProvider(getActivity()).get(RegistTreeStatusInfoViewModel.class);
        registTreeStatusInfoFrBinding.setTreeStatusInfoVM(treeStatusInfoVM);
        return registTreeStatusInfoFrBinding.getRoot();
    }


}
