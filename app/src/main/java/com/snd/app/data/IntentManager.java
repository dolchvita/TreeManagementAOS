package com.snd.app.data;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;


public class IntentManager {
    protected String TAG = this.getClass().getName();

    private Context context;


    // 생성자를 통해 Context를 받음
    public IntentManager(Context context) {
        this.context = context;
    }


    public void launchActivity(Activity activity) {
        Intent intent = new Intent(context, activity.getClass());
        context.startActivity(intent);
    }
}
