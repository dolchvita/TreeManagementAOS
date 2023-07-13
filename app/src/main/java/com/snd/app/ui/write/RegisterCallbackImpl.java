package com.snd.app.ui.write;


import android.util.Log;
import com.snd.app.data.AppComponent;
import com.snd.app.data.AppModule;
import com.snd.app.data.DaggerAppComponent;



public class RegisterCallbackImpl implements RegisterCallback{
    protected String TAG = this.getClass().getName();

    private RegistTreeBasicInfoViewModel viewModel;

    public RegisterCallbackImpl() {
        AppComponent appComponent = DaggerAppComponent.builder().appModule(new AppModule(null)).build();
        viewModel=appComponent.registTreeBasicInfoViewModel();
    }



    @Override
    public void responseSuccessful() {
        // 여기까지 옴
        Log.d(TAG, "반응해");

        new Thread(() -> {
            // Background thread
            viewModel._resultLiveData.postValue("반응해");
        }).start();

        //viewModel.responseTest();
    }



    @Override
    public void responseFailure() {

    }


}
