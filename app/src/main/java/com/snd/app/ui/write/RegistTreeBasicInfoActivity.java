package com.snd.app.ui.write;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.snd.app.R;
import com.snd.app.common.LocationActivity;
import com.snd.app.common.TMActivity;
import com.snd.app.data.AppComponent;
import com.snd.app.data.AppModule;
import com.snd.app.data.DaggerAppComponent;
import com.snd.app.data.user.SharedPreferencesManager;
import com.snd.app.databinding.RegistTreeBasicInfoActBinding;
import com.snd.app.domain.tree.TreeBasicInfoDTO;
import com.snd.app.sharedPreferences.SharedApplication;
import com.snd.app.ui.tree.PhotoAdapter;
import com.snd.app.ui.tree.TreeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

public class RegistTreeBasicInfoActivity extends LocationActivity implements  Callback{
    RegistTreeBasicInfoActBinding treeBasicInfoActBinding;
    RegistTreeBasicInfoViewModel treeBasicInfoVM;
    @Inject
    SharedPreferencesManager sharedPreferencesManager;
    // TreeActivity 로부터 전달 받은 문자 데이터
    private static final String IDHEX="idHex";
    String idHex;
    TreeBasicInfoDTO treeBasicInfoDTO;

    // 이미지 권한
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    // 이미지 리스트
    private File currentPhotoFile;
    // 사진 찍히는 것 제한하기
    private boolean isPhotoTaken = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        treeBasicInfoActBinding= DataBindingUtil.setContentView(this, R.layout.regist_tree_basic_info_act);
        treeBasicInfoActBinding.setLifecycleOwner(this);
        // 의존성 주입하기
        AppComponent appComponent= DaggerAppComponent.builder().appModule(new AppModule(new SharedApplication())).build();
        appComponent.inject(this);
        // 로그인 객체 주입하여 사용하기
        sharedPreferencesManager=appComponent.sharedPreferencesManager();
        // 뷰모델 연결
        treeBasicInfoVM=new RegistTreeBasicInfoViewModel();
        treeBasicInfoActBinding.setTreeBasicInfoVM(treeBasicInfoVM);
        // NFC 코드 추출
        idHex=getIntent().getStringExtra(IDHEX);
        Log.d(TAG,"** 아이디 확인 **"+idHex);
        // 콜백 인터페이스 연결
        treeBasicInfoVM.setCallback(this);


        try {
            setTreeBasicInfoDTO();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        //getTreeLocation();
        registerTreeImage();
    }


   public void getTreeLocation(){
       //getLocation();
       Log.d(TAG,"** 위도 **"+latitude);
       Log.d(TAG,"** 경도 **"+longitude);
   }


    public void setTreeBasicInfoDTO() throws JsonProcessingException {
        treeBasicInfoDTO=new TreeBasicInfoDTO();
        treeBasicInfoDTO.setNFC(idHex);
        treeBasicInfoDTO.setSpecies("산사나무");
        treeBasicInfoDTO.setSubmitter(sharedPreferencesManager.getUserInfo("id",null));
        treeBasicInfoDTO.setVendor(sharedPreferencesManager.getUserInfo("company",null));

        // 매핑된 DTO 넘겨줌
        treeBasicInfoVM.setTextViewModel(treeBasicInfoDTO);
    }

    // 수목 기본정보 등록
    public void registerTreeBasicInfo() {
        Log.d(TAG,"** 호출 **");
        Log.d(TAG,"** url **"+sndUrl+"/app/tree/registerBasicInfo");

        JSONObject treeBasicData=new JSONObject();

        // 입력 데이터 보내기
        try {
            treeBasicData.put("nfc",treeBasicInfoDTO.getNFC());
            treeBasicData.put("species",treeBasicInfoDTO.getSpecies());
            treeBasicData.put("submitter",treeBasicInfoDTO.getSubmitter());
            treeBasicData.put("vendor",treeBasicInfoDTO.getVendor());

            Log.d(TAG,"** 보낼 데이터 모습 **"+treeBasicData);
            Log.d(TAG,"** 보낼 토큰 모습 **"+sharedPreferencesManager.getUserInfo("Authorization",null));

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.POST, sndUrl+"/app/tree/registerBasicInfo", treeBasicData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "** 기본정보 응답 ** "+response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "** 오류남 **"+error);
                        Toast.makeText(getApplicationContext(),"이미 등록된 나무이거나\n수목 정보가 올바르지 않습니다", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // 헤더에 값 보내기
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", sharedPreferencesManager.getUserInfo("Authorization",null));
                return headers;
            }
        };
        AppModule.requestQueue.add(request1);
    }

    // 뷰모델에서 호출
    @Override
    public void onCallback() {
        // 수목 기본 정보 등록
        registerTreeBasicInfo();
    }


    /*--------------------------------------
            카메라 관련 로직 start
        -------------------------------------*/
    public void registerTreeImage(){
        treeBasicInfoVM.addPhoto.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                Log.d(TAG, "** 클릭 감지 **");

                // 카메라 실행시키는 로직 수행
                if(requestPermissions()>0){
                    startCamera();
                }
            }
        });
    }


    // 파일 객체 가져오기
    private File createImageFile() throws IOException {
        // 파일 이름 생성
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        // 파일 저장 디렉토리 생성
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (storageDir != null && !storageDir.exists()) {
            storageDir.mkdirs();
        }

        // 이미지 파일 생성
        File imageFile = File.createTempFile(
                imageFileName,  /* 파일 이름 */
                ".jpg",         /* 파일 확장자 */
                storageDir      /* 파일이 저장될 디렉토리 */
        );
        return imageFile;
    }



    private void startCamera() {
        Log.d(TAG,"** startCamera 호출됨 **");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            // 이미지 파일 생성
            try {
                currentPhotoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            // 파일이 정상적으로 생성된 경우에만 카메라 앱에 전달
            if (currentPhotoFile != null) {
                Log.d(TAG,"** currentPhotoFile 전달 **");
                Uri photoUri = FileProvider.getUriForFile(this, "com.snd.app.fileprovider", currentPhotoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }

        Log.d(TAG,"** 통과됨 **"+currentPhotoFile);
    }

    // 사진 저장 되었는지 확인
    private void scanSavedPhoto(File photoFile) {
        MediaScannerConnection.scanFile(
                this,
                new String[]{photoFile.getAbsolutePath()},
                null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        Log.d(TAG,"** 여기까지 오나 보자 **");

                        // 미디어 스캐닝이 완료된 후에 수행할 작업을 여기에 추가
                    }
                }
        );
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       Log.d(TAG,"** onActivityResult 호출됨 **");

       if (!isPhotoTaken && requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            isPhotoTaken=true;
           Log.d(TAG,"** 단계 1 **");

           scanSavedPhoto(currentPhotoFile);


           // data가 null
           if (data != null) {
               Log.d(TAG,"** 단계 2 **");

               Bundle extras = data.getExtras();

                if (extras != null) {
                    Log.d(TAG,"** 단계 3 **");

                    // null
                    Bitmap image1 = (Bitmap) extras.get("data");
                    Log.d(TAG,"** 비트맵 ** "+image1);

                    // 찍은 사진 파일을 가져와서 이미지 뷰에 적용하기
                    List<Bitmap> photoList=new ArrayList<>();


                    List<String> photoPaths = new ArrayList<>(); // 사진 파일 경로 리스트

                    //-------------------------------------------------------- 여기 작업 중 -
                    // 사진 찍은 후 저장할 때 경로를 리스트에 추가
                    photoPaths.add(currentPhotoFile.getAbsolutePath());

                    // RecyclerView에 표시할 Bitmap 리스트
                    List<Bitmap> photoBitmaps = new ArrayList<>();

                    // 리스트에 있는 모든 사진 파일을 Bitmap으로 변환하여 리스트에 추가
                    for (String photoPath : photoPaths) {
                        Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
                        if (bitmap != null) {
                            photoBitmaps.add(bitmap);
                        }
                    }


                    photoList.add(image1);

                    RecyclerView recyclerView=findViewById(R.id.rv_image);
                    // 가로 정렬
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));

                    PhotoAdapter photoAdapter = (PhotoAdapter) recyclerView.getAdapter();
                    if (photoAdapter == null) {
                        Log.d(TAG,"** 단계 4 **");

                        photoAdapter = new PhotoAdapter(new ArrayList<>());
                        recyclerView.setAdapter(photoAdapter);
                    }

                    Log.d(TAG,"** 단계 5 **");

                    photoAdapter.addPhoto(image1);
                    Log.d(TAG, "** 담긴 사진 수 **"+photoAdapter.getItemCount());
                }
            }
        }
    }



}
