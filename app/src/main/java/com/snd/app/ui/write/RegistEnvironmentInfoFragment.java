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
import com.snd.app.databinding.RegistEnvironmentInfoFrBinding;

public class RegistEnvironmentInfoFragment extends TMFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RegistEnvironmentInfoFrBinding registEnvironmentInfoFrBinding= DataBindingUtil.inflate(inflater, R.layout.regist_environment_info_fr, container, false);
        registEnvironmentInfoFrBinding.setLifecycleOwner(this);
        RegistEnvironmentInfoViewModel registEnvironmentInfoVM=new ViewModelProvider(getActivity()).get(RegistEnvironmentInfoViewModel.class);
        registEnvironmentInfoFrBinding.setEnvironmentVM(registEnvironmentInfoVM);
        return registEnvironmentInfoFrBinding.getRoot();
    }



}
