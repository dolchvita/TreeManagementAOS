package com.snd.app.common;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.snd.app.data.AppComponent;
import com.snd.app.data.AppModule;
import com.snd.app.data.DaggerAppComponent;

public class TMFragment extends Fragment {
    public String TAG = this.getClass().getName();
    protected String sndUrl = "http://snd.synology.me:9955";
    //protected SharedPreferences sharedPreferences;
    protected AppComponent appComponent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());


        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
