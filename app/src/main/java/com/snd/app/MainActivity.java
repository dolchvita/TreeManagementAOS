package com.snd.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.snd.app.databinding.MainActBinding;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

        MainActBinding mainActBinding;

        @Inject
        MainViewModel mainVM;

        private String TAG=this.getClass().getName();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.main_act);
            Log.d(TAG, " 호출?");

            mainActBinding=DataBindingUtil.setContentView(this, R.layout.main_act);
            mainActBinding.setLifecycleOwner(this);

            mainVM=new ViewModelProvider(this).get(MainViewModel.class);
            mainActBinding.setMainVM(mainVM);

            mainVM=mainActBinding.getMainVM();


            Log.d(TAG, mainActBinding+" 바인딩 객체 뭔데");
            Log.d(TAG, mainVM+"!!!!!!!asaswqdwqedd!!!!!");

           mainVM.getTabClcick();
            Log.d(TAG, mainVM.getTabClcick()+"뭐가 나올까!!!!!!!asaswqdwqedd!!!!!");

        }

}