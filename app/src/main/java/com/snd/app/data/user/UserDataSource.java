package com.snd.app.data.user;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.toolbox.Volley;
import com.snd.app.data.AppModule;

// 유저정보 가져오기!
public class UserDataSource extends AppCompatActivity {

   public void getUser(){
       if(AppModule.requestQueue == null)
           AppModule.requestQueue = Volley.newRequestQueue(getApplicationContext());
   }


}
