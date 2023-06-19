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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.snd.app.R;
import com.snd.app.common.TMActivity;
import com.snd.app.data.AppModule;
import com.snd.app.data.LocationRepository;
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


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


public class RegistTreeBasicInfoActivity extends TMActivity implements MyCallback {
    RegistTreeBasicInfoActBinding treeBasicInfoActBinding;
    RegistTreeBasicInfoViewModel treeBasicInfoVM;
    // TreeActivity 로부터 전달 받은 문자 데이터
    private static final String IDHEX="IDHEX";
    String idHex;
    TreeBasicInfoDTO treeBasicInfoDTO;
    // 이미지 권한
    private static final int REQUEST_PERMISSION = 1;
    // 이미지 리스트
    private File currentPhotoFile;
    List<String> photoPaths;
    private RecyclerView recyclerView;
    private PhotoAdapter photoAdapter;
    // 사진 지울시 확인 버튼 감지용
    public Boolean flag=true;
    List<File> currentList=new ArrayList<>();
    String species;


    // 위치 권한과 관련된 변수들!  (**지금 작업 중*)
   Boolean isGranted;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "** RegistTreeBasicInfoActivity 객체 생성 **");
        super.onCreate(savedInstanceState);
        treeBasicInfoActBinding= DataBindingUtil.setContentView(this, R.layout.regist_tree_basic_info_act);
        treeBasicInfoActBinding.setLifecycleOwner(this);
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
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));      // 가로 정렬
        recyclerView.addItemDecoration(new SpaceItemDecoration(20));
        // 어댑터 연결
        photoAdapter=new PhotoAdapter();
        recyclerView.setAdapter(photoAdapter);
        // 입력 문자열 추출
        AppCompatAutoCompleteTextView tree_name=(AppCompatAutoCompleteTextView) findViewById(R.id.tr_name);

        // 위치 - 이게 호출되어야만 할까? / 권한 요청하는 메서드.. / 결국 getLocation() 가 호출되어야 했던 것!
        //getLocation();

        tree_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG,"** 2 **");
            }
            @Override
            public void afterTextChanged(Editable s) {
                species = s.toString();
                Log.d(TAG,"** 이게 몰까 **"+species);
            }
        });
        try {
            setTreeBasicInfoDTO();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        onCamera();

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
            public void onChanged(String event) {
                treeBasicInfoVM.cnt+=1;
            }
        });
        photoAdapter.tabClick.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                showAlertDialog();
            }
        });


        treeBasicInfoVM.back.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegistTreeBasicInfoActivity.this);
                builder.setTitle("나가시겠습니까?");
                builder.setMessage("입력 중인 내용은 저장되지 않습니다.");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 확인 버튼을 눌렀을 때
                       finish();
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
        });


        // 위성 개수 추출
        LocationRepository locationRepository=new LocationRepository(this);
        locationRepository.setPermissionGranted(true);
        locationRepository.startTracking();

        locationRepository.getSatellites().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer satellitesCount) {
                Log.d(TAG, "현재 위성 개수: " + satellitesCount);

                if (satellitesCount>6){
                    // 위도 경도 가져오기
                    getLocation();
                    // 디자인 요소 반영
                    getTreeLocation();
                }
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
                // 삭제버튼 눌렀다면
                treeBasicInfoVM.cnt-=1;
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


    // 디자인 요소에 세팅하기
   public void getTreeLocation(){
       treeBasicInfoVM.latitude.set(""+latitude);
       treeBasicInfoVM.longitude.set(""+longitude);
   }


    public void setTreeBasicInfoDTO() throws JsonProcessingException {
        treeBasicInfoDTO=new TreeBasicInfoDTO();
        treeBasicInfoDTO.setNFC(idHex);
        treeBasicInfoDTO.setSpecies(sharedPreferences.getString("species",null));
        treeBasicInfoDTO.setSubmitter(sharedPreferences.getString("id",null));
        treeBasicInfoDTO.setVendor(sharedPreferences.getString("company",null));
        // 매핑된 DTO 넘겨줌
        treeBasicInfoVM.setTextViewModel(treeBasicInfoDTO);
    }


    // 뷰모델에서 호출 - 저장 버튼 누를 시
    @Override
    public void onCustomCallback() {
        //팝업 창 띄우기
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistTreeBasicInfoActivity.this);
        builder.setTitle("추가 입력");
        builder.setMessage("수목 상태 정보를 등록하시겠습니까?");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                registerTreeInfo();

            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 취소 버튼을 눌렀을 때
                registerTreeInfo();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }


   public void registerTreeInfo(){
       registerTreeBasicInfo();
       if(currentList.size()>0){
           registerTreeImage(currentList);
       }
       registerTreeLocationInfo();
       Toast.makeText(RegistTreeBasicInfoActivity.this, "등록되었습니다", Toast.LENGTH_SHORT).show();
    }


    // 수목 기본정보 등록
    public void registerTreeBasicInfo() {
        JSONObject treeBasicData=new JSONObject();

        try {
            // 입력 데이터 보내기
            treeBasicData.put("nfc", idHex);
            treeBasicData.put("species", species);
            treeBasicData.put("submitter", sharedPreferences.getString("id",null));
            treeBasicData.put("vendor", sharedPreferences.getString("company",null));

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        JsonObjectRequest request1 = new JsonObjectRequest(com.android.volley.Request.Method.POST, sndUrl+"/app/tree/registerBasicInfo", treeBasicData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "** 기본정보 응답 ** "+response);

                        // 확인 버튼을 눌렀을 때
                        Intent intent=new Intent(RegistTreeBasicInfoActivity.this, RegistTreeStatusInfoActivity.class);
                        intent.putExtra("NFC",  idHex);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "** 기본정보 오류남 **"+error);
                        Toast.makeText(getApplicationContext(),"이미 등록된 나무이거나\n수목 정보가 올바르지 않습니다", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // 헤더에 값 보내기
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", sharedPreferences.getString("Authorization",null));
                return headers;
            }
        };
        AppModule.requestQueue.add(request1);
    }


    // okHttp 라이브러리 사용하여 서버 통신하기    -- 환경 정보 등록
    public void registerTreeLocationInfo(){
        OkHttpClient client = new OkHttpClient();
        JSONObject treeLocationData=new JSONObject();
        try {
            // 입력 데이터 보내기
            String latitudeValue = String.format("%.7f", latitude);     // 자릿수 맞추기
            treeLocationData.put("latitude", latitudeValue);
            String longitudeValue = String.format("%.7f", longitude);
            treeLocationData.put("longitude", longitudeValue);
            treeLocationData.put("nfc",treeBasicInfoDTO.getNFC().toUpperCase());
            treeLocationData.put("submitter",treeBasicInfoDTO.getSubmitter());
            treeLocationData.put("vendor",treeBasicInfoDTO.getVendor());

            Log.d(TAG,"** 보낼 데이터 모습 **"+treeLocationData);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), treeLocationData.toString());
        String url = sndUrl+"/app/tree/registerLocationInfo";
        // 요청 생성
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", sharedPreferences.getString("Authorization", null))
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) throws IOException {
               if (response.isSuccessful()){
                   String responseData = response.body().string();
                   Log.d(TAG,"** 위치 성공 / 응답 **"+responseData);

               }else{
                   String responseData = response.body().string();
                   Log.d(TAG,"** 위치 실패 / 응답 **"+responseData);
               }
            }
            @Override
            public void onFailure(Call call, IOException e) {
                // 여기에 요청이 실패했을 때 실행될 코드를 작성하세요.
                Log.d(TAG,"** 위치 등록 오류남 **");
            }
        });
    }


    // 6-2) 사진 파일 보내기
    public void registerTreeImage(List<File> files){
        OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        if (files.size()>0){
            for (int i=0; i<files.size(); i++){
                builder.addFormDataPart("image"+(i+1), files.get(i).getName(), RequestBody.create(MediaType.parse("multipart/form-data"), files.get(i)));
            }
        }
        builder.addFormDataPart("tagId", idHex );

        // 업로드할 URL을 생성합니다.
        String url = sndUrl+"/app/tree/registerTreeImage"; // 실제 업로드할 서버의 URL로 변경해야 합니다.

        // 요청 생성
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", sharedPreferences.getString("Authorization", null))
                .post(builder.build())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) throws IOException {
                Log.d(TAG,"** 업로드 성공? **"+response);
            }
            @Override
            public void onFailure(Call call, IOException e) {
                // 여기에 요청이 실패했을 때 실행될 코드를 작성하세요.
                Log.d(TAG,"** 사진 오류남 **");
            }
        });
    }


    /*--------------------------------------
            카메라 관련 로직 start
       -------------------------------------*/

    public void onCamera (){
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


    // 1 파일 객체 가져오기 - 사진 찍기 전에 빈 파일 미리 생성하는 역할
    private File createImageFile() throws IOException {
        // 사진 이름 가공
        String imageFileName = "JPEG_" + System.currentTimeMillis();
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File imageFile = new File(storageDir, imageFileName + ".jpg");
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
       boolean isCameraPermissionGranted=sharedPreferences.getBoolean("camera_permission_granted", false);
       if (isCameraPermissionGranted) {
           photoPaths = new ArrayList<>(); // 사진 파일 경로 리스트
           // 사진 파일 저장
           String uri=currentPhotoFile.getAbsolutePath();
           photoPaths.add(uri);      // 사진 경로
           // 4 갤러리 저장
           scanImageFile(currentPhotoFile);
           galleryAddPic();
           // 5-1) 경로에 있는 사진 꺼냄
           Bitmap bitmap = BitmapFactory.decodeFile(uri);
           // 5-2) 실제 사진을 리스트에 담기
           treeBasicInfoVM.setImageList(bitmap);
           // 6-1 보낼 파일 리스트
           File file=new File(uri);
           currentList.add(file);
        }
    }


    // 4 갤러리 저장
    private void galleryAddPic() {
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
