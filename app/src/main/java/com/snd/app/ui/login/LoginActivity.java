package com.snd.app.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.snd.app.MainActivity;
import com.snd.app.MainViewModel;
import com.snd.app.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.inject.Inject;

public class LoginActivity extends AppCompatActivity {
    private String TAG=this.getClass().getName();

    EditText t_id;
    EditText t_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_login);

        t_id=findViewById(R.id.id_input);
        t_pass=findViewById(R.id.pass_input);

        Button bt_login=findViewById(R.id.bt_login);

        // 로그인 버튼
        bt_login.setOnClickListener((v) ->{
            Thread thread=new Thread(){
                @Override
                public void run() {
                    Log.d(TAG,"저장 버튼 누름");
                    loginRequest();
                }
            };
            thread.start();
        });

    } //onCreate


    public void loginRequest(){
        Log.d(TAG, "로그인 버튼");
        BufferedWriter buffw=null;
        BufferedReader buffr=null;
        OutputStreamWriter os=null;
        InputStreamReader is=null;

        // 여기서 http 주소를 얻는 방법!!
        URL url=new URL("");


    } //send



}