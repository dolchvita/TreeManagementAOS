package com.snd.app.ui.write;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "** RegistTreeBasicInfoActivity 객체 생성 **");
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
    // 이미지 권한
    private static final int REQUEST_PERMISSION = 1;
    // 이미지 리스트
    private File currentPhotoFile;
    // 사진 찍히는 것 제한하기
    private boolean isPhotoTaken = false;


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


    // 1 파일 객체 가져오기 - 사진 찍기 전에 빈 파이 미리 생성하는 역할
    private File createImageFile() throws IOException {
        Log.d(TAG, "** createImageFile 호출됨 **");

        // 사진 이름 가공
        String imageFileName = "JPEG_" + System.currentTimeMillis();
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File imageFile = new File(storageDir, imageFileName + ".jpg");

        Log.d(TAG, "** 보내기 전에 확인 ** " + imageFile);
        return imageFile;
    }


    // 2 카메라 촬영
    private void startCamera() {
        Log.d(TAG,"** startCamera 호출됨 **");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            try {
                // 2-1) 이미지 파일 생성 - 사진이 저장될 경로 반환
                currentPhotoFile = createImageFile();

            } catch (IOException ex) {
                ex.printStackTrace();
            }

            // 2-2) 카메라 앱이 사진을 찍은 후 앱으로 결과를 전달할 때 필요한 URI를 설정하는 부분
            if (currentPhotoFile != null) {
                Log.d(TAG,"** currentPhotoFile 전달 **");

                // 여기가 null
                Uri photoUri = FileProvider.getUriForFile(this, "com.snd.app.fileprovider", currentPhotoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);  // 2-3)저장 경로 설정 후

                // 2-4) 결과 메서드 호출
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CODE);
            }
        }

        Log.d(TAG,"** 통과됨 **"+currentPhotoFile);
    }


    //사진 저장
    public void saveImage(File photoFile) throws IOException {
        // 권한 체크
        if (ContextCompat.checkSelfPermission(RegistTreeBasicInfoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(RegistTreeBasicInfoActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
        } else {
            // 사진 촬영
            dispatchTakePictureIntent();
        }
    }

    private String currentPhotoPath;

    //  사진 파일 저장할 임시 파일 생성
    private void dispatchTakePictureIntent() throws IOException {
        Log.d(TAG,"** dispatchTakePictureIntent 호출 ** ");

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // 사진을 저장할 파일 생성
            File photoFile = null;
            try {
                photoFile = createImageFile();
                currentPhotoPath = photoFile.getAbsolutePath();
            } catch (Exception ex) {
                Log.e("Error", ex.getMessage());
            }

            if (photoFile != null) {
                Uri photoUri = Uri.fromFile(photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CODE);
            }
        }
    }


    // 3 결과 호출
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 매개변수로 날라오는 data의 의미 --> 카메라 앱에서 설정한 결과 데이터 (사진의 섬네일 등)
        // 그러나 사진을 파일로 저장하는 경우 일반적으로 null
       Log.d(TAG, "순서 좀 알아보자");
        super.onActivityResult(requestCode, resultCode, data);

       Log.d(TAG,"** onActivityResult 호출됨 **");

       if (!isPhotoTaken && requestCode == REQUEST_IMAGE_CODE && resultCode == RESULT_OK) {
            isPhotoTaken=true;
           Log.d(TAG,"** 단계 1 **");

           Log.d(TAG,"** 저장 메서드 가기 전 확인 **"+currentPhotoFile);
           try {
               saveImage(currentPhotoFile);

           } catch (IOException e) {
               throw new RuntimeException(e);
           }

           Log.d(TAG,"** 단계 2 **");


            // 찍은 사진 파일을 가져와서 이미지 뷰에 적용하기
            List<Bitmap> photoList=new ArrayList<>();
            List<String> photoPaths = new ArrayList<>(); // 사진 파일 경로 리스트

            //-------------------------------------------------------- 여기 작업 중 -
            // 사진 찍은 후 저장할 때 경로를 리스트에 추가
            photoPaths.add(currentPhotoFile.getAbsolutePath());
           scanImageFile(currentPhotoFile);

            // RecyclerView에 표시할 Bitmap 리스트
            List<Bitmap> photoBitmaps = new ArrayList<>();

           Bitmap bitmap=null;
            // 리스트에 있는 모든 사진 파일을 Bitmap으로 변환하여 리스트에 추가
            for (String photoPath : photoPaths) {
                 bitmap = BitmapFactory.decodeFile(photoPath);
                if (bitmap != null) {
                    photoBitmaps.add(bitmap);
                }
            }
           Log.d(TAG,"** 결과 확인 ** "+bitmap);

            RecyclerView recyclerView=findViewById(R.id.rv_image);
            // 가로 정렬
            recyclerView.setLayoutManager(new LinearLayoutManager(this));


            PhotoAdapter photoAdapter = (PhotoAdapter) recyclerView.getAdapter();
            if (photoAdapter == null) {
                Log.d(TAG,"** 단계 4 **");
                photoAdapter = new PhotoAdapter(photoList);
                recyclerView.setAdapter(photoAdapter);
            }

           // 변환된 이미지를 어댑터에 추가
           for (Bitmap photoBitmap : photoList) {
               Drawable drawable = new BitmapDrawable(getResources(), photoBitmap);
               Bitmap convertedBitmap = ((BitmapDrawable) drawable).getBitmap();
               photoAdapter.addPhoto(convertedBitmap);
           }


            // 파일이 들어가야 함!
            photoAdapter.addPhoto(bitmap);

            Log.d(TAG,"** 단계 5 **");

            // photoAdapter 없음
            Log.d(TAG, "** 담긴 사진 수 **"+photoAdapter.getItemCount());

            // 4 갤러리 저장
           galleryAddPic();
        }
    }


    // 갤러리 저장
    private void galleryAddPic() {
        Log.d(TAG,"** galleryAddPic 호출 **");

        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath());
        Uri contentUri = Uri.fromFile(imageFile);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
        Toast.makeText(this, "Saved to Gallery", Toast.LENGTH_SHORT).show();
    }


    // 갤러리 이미지 스캔
    private void scanImageFile(File imageFile) {
        Log.d(TAG,"** scanImageFile 호출 **");

        MediaScannerConnection.scanFile(this,
                new String[]{imageFile.getAbsolutePath()},
                null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        // 스캔 완료 후 갤러리 갱신
                        refreshGallery();
                    }
                });
    }


    // 갤러리 새로고침
    private void refreshGallery() {
        Log.d(TAG,"** refreshGallery 호출 **");
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File galleryDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        Uri contentUri = Uri.fromFile(galleryDir);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }


}
