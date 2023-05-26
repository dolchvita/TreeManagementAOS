package com.snd.app.ui.write;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
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

import com.snd.app.R;
import com.snd.app.common.LocationActivity;
import com.snd.app.data.AppComponent;
import com.snd.app.data.AppModule;
import com.snd.app.data.DaggerAppComponent;
import com.snd.app.data.user.SharedPreferencesManager;
import com.snd.app.databinding.RegistTreeBasicInfoActBinding;
import com.snd.app.domain.tree.TreeBasicInfoDTO;
import com.snd.app.ui.tree.PhotoAdapter;
import com.snd.app.ui.tree.SpaceItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    private RecyclerView recyclerView;
    private PhotoAdapter photoAdapter;

    public Boolean flag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "** RegistTreeBasicInfoActivity 객체 생성 **");
        super.onCreate(savedInstanceState);
        treeBasicInfoActBinding= DataBindingUtil.setContentView(this, R.layout.regist_tree_basic_info_act);
        treeBasicInfoActBinding.setLifecycleOwner(this);
        // 의존성 주입하기
        AppComponent appComponent= DaggerAppComponent.builder().appModule(new AppModule(this)).build();
        appComponent.inject(this);

        // 뷰모델 연결
        treeBasicInfoVM=new RegistTreeBasicInfoViewModel();
        treeBasicInfoActBinding.setTreeBasicInfoVM(treeBasicInfoVM);
        // NFC 코드 추출
        idHex=getIntent().getStringExtra(IDHEX);
        Log.d(TAG,"** 아이디 확인 **"+idHex);
        // 콜백 인터페이스 연결
        treeBasicInfoVM.setCallback(this);

        // 이미지 저장
        recyclerView=findViewById(R.id.rv_image);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));        // 가로 정렬
        recyclerView.addItemDecoration(new SpaceItemDecoration(20));

        // 어댑터 연결
        photoAdapter=new PhotoAdapter();
        recyclerView.setAdapter(photoAdapter);

        try {
            setTreeBasicInfoDTO();

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        //getTreeLocation();
        registerTreeImage();

        treeBasicInfoVM.listData.observe(this, new Observer<List<Bitmap>>() {
            @Override
            public void onChanged(List<Bitmap> bitmaps) {
                // 5-3) 변경 감지
                photoAdapter.setImageList(bitmaps);
                Log.d(TAG, "개수 확인"+photoAdapter.getItemCount());
            }
        });

        treeBasicInfoVM.imgCount.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                treeBasicInfoVM.cnt+=1;
            }
        });

        // 2
        photoAdapter.tabClick.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                showAlertDialog();
            }
        });
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("");
        builder.setMessage("사진을 삭제하시겠습니까?");

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 확인 버튼을 눌렀을 때
                flag=true;
                photoAdapter.setAlertDialog(photoAdapter.clickedPosition, flag);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 취소 버튼을 눌렀을 때
                flag=false;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

   public void getTreeLocation(){
       //getLocation();
       Log.d(TAG,"** 위도 **"+latitude);
       Log.d(TAG,"** 경도 **"+longitude);
   }

    public void setTreeBasicInfoDTO() throws JsonProcessingException {
        Log.d(TAG,"** 매니저 **"+sharedPreferencesManager);

        treeBasicInfoDTO=new TreeBasicInfoDTO();
        treeBasicInfoDTO.setNFC(idHex);
        treeBasicInfoDTO.setSpecies("산사나무");

        // 여기가 문제
        treeBasicInfoDTO.setSubmitter(sharedPreferences.getString("id",null));
        treeBasicInfoDTO.setVendor(sharedPreferences.getString("company",null));

        // 매핑된 DTO 넘겨줌
        treeBasicInfoVM.setTextViewModel(treeBasicInfoDTO);
    }

    // 수목 기본정보 등록
    public void registerTreeBasicInfo() {
        Log.d(TAG,"** registerTreeBasicInfo 호출 **");
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

    // 뷰모델에서 호출 - 저장 버튼 누를 시
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

    List<String> photoPaths;

    public void registerTreeImage(){
        treeBasicInfoVM.camera.observe(this, new Observer() {

            @Override
            public void onChanged(Object o) {
                Log.d(TAG, "** 클릭 감지 **");

                boolean isCameraPermissionGranted=sharedPreferences.getBoolean("camera_permission_granted", false);
                // 권한 상태에 따른 처리
                if (isCameraPermissionGranted) {
                    // 권한이 허용된 경우 처리
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

    // 2 카메라 촬영 - 아마 문제 없음
    private void startCamera() {
        Log.d(TAG,"** startCamera 호출됨 **");

        if (treeBasicInfoVM.listData.getValue() != null && treeBasicInfoVM.currentList.size() >= 2) {
            Toast.makeText(this, "이미지 업로드 최대 2개를 초과하였습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

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

                Uri photoUri = FileProvider.getUriForFile(this, "com.snd.app.fileprovider", currentPhotoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);  // 2-3)저장 경로 설정 후

                // 2-4) 결과 메서드 호출
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CODE);
            }
        }
        Log.d(TAG,"** 통과됨 **"+currentPhotoFile);
    }


    // 3 결과 호출
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 매개변수로 날라오는 data의 의미 --> 카메라 앱에서 설정한 결과 데이터 (사진의 섬네일 등)
        // 그러나 사진을 파일로 저장하는 경우 일반적으로 null
       Log.d(TAG,"** onActivityResult 호출됨 **");

       boolean isCameraPermissionGranted=sharedPreferences.getBoolean("camera_permission_granted", false);
       if (isCameraPermissionGranted) {
           //isPhotoTaken=true;
           Log.d(TAG,"** 단계 1 **");
           Log.d(TAG,"** 저장 메서드 가기 전 확인 ** "+currentPhotoFile);

           photoPaths = new ArrayList<>(); // 사진 파일 경로 리스트
           // 사진 파일 저장
           String uri=currentPhotoFile.getAbsolutePath();
           photoPaths.add(uri);      // 사진 경로


           // 4 갤러리 저장
           scanImageFile(currentPhotoFile);
           galleryAddPic();

           // 5-1) 경로에 있는 사진 꺼냄
           Bitmap bitmap = BitmapFactory.decodeFile(uri);
           Log.d(TAG,"** 리스트에 담기는 사진! ** "+bitmap);

           // 5-2) 실제 사진을 리스트에 담기
           treeBasicInfoVM.setImageList(bitmap);
        }
    }


    // 4 갤러리 저장
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
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File galleryDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        Uri contentUri = Uri.fromFile(galleryDir);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }


}
