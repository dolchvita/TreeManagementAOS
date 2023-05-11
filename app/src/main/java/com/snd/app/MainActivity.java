package com.snd.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;

import com.snd.app.databinding.MainActBinding;
import com.snd.app.ui.home.HomeFragment;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

        MainActBinding mainActBinding;

        @Inject
        MainViewModel mainVM;

        private String TAG=this.getClass().getName();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            mainActBinding=DataBindingUtil.setContentView(this, R.layout.main_act);
            mainActBinding.setLifecycleOwner(this);

            mainVM=new ViewModelProvider(this).get(MainViewModel.class);    // ViewModel 가져오기
            mainActBinding.setMainVM(mainVM);
            mainVM=mainActBinding.getMainVM();  // 뷰모델 연동


            Log.d(TAG, mainActBinding+" 바인딩 객체 뭔데");
            Log.d(TAG, mainVM+"!!!!!!!asaswqdwqedd!!!!!");


            // 화면에 보일 프레그먼트
            HomeFragment homeFragment=new HomeFragment();

            // 아마 처음 화면을 메인으로 갖추는 것
            getSupportFragmentManager().beginTransaction().replace(R.id.content, homeFragment).commit();


            mainVM.getTabClcick().observe(this, new Observer() {
                @Override
                public void onChanged(Object o) {
                    // 여기서 UI 업데이트 처리
                    Log.d(TAG,"클릭 감지");

                    // 매개변수로 넘어오는 객체는 뷰모델이 설정한 상수의 값! - 세팅된 것이 넘어옴
                    Log.d(TAG,o+"이 객체는 뭘까~~~??");

                    FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();


                    // 메인 화면을 4가지의 프레그먼트로 분할
                    if(o.equals(1)){
                        Log.d(TAG,"홈이 올 예정");
                        transaction.replace(R.id.content, homeFragment);

                    } else if (o.equals(2)) {
                        Log.d(TAG,"맵이 올 예정");

                    }
                    transaction.commit();
                }
            });

        }

}