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
import com.snd.app.databinding.GetEnvironmentInfoFrBinding;
import com.snd.app.databinding.RegistEnvironmentInfoFrBinding;
import com.snd.app.ui.write.RegistEnvironmentInfoViewModel;

public class GetEnvironmentFragment extends TMFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        GetEnvironmentInfoFrBinding getEnvironmentInfoFrBinding= DataBindingUtil.inflate(inflater, R.layout.get_environment_info_fr, container, false);
        getEnvironmentInfoFrBinding.setLifecycleOwner(this);
        GetEnvironmentViewModel getEnvironmentVM=new ViewModelProvider(getActivity()).get(GetEnvironmentViewModel.class);
        getEnvironmentInfoFrBinding.setEnvironmentVM(getEnvironmentVM);
        return getEnvironmentInfoFrBinding.getRoot();
    }


}
