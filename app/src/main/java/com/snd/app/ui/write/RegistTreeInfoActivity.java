package com.snd.app.ui.write;

import android.annotation.SuppressLint;
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
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.snd.app.MainActivity;
import com.snd.app.R;
import com.snd.app.common.TMActivity;
import com.snd.app.data.KakaoMapFragment;
import com.snd.app.data.LocationRepository;
import com.snd.app.data.SpinnerValueListener;
import com.snd.app.databinding.WriteActBinding;
import com.snd.app.domain.tree.TreeBasicInfoDTO;
import com.snd.app.ui.tree.PhotoAdapter;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class RegistTreeInfoActivity extends TMActivity implements MyCallback, MapView.POIItemEventListener, SpinnerValueListener {
    WriteActBinding writeActBinding;
    RegistTreeInfoViewModel treeInfoVM;
    private RegistTreeBasicInfoFragment registTreeBasicInfoFr;

    String idHex;
    String submitter;
    String vendor;

    private final int BASIC=1;
    private final int SPACIFICLOCATION=2;
    private final int STATUS=3;
    private final int ENVIRONMENT=4;

    // 이미지 권한
    private static final int REQUEST_PERMISSION = 1;
    // 이미지 리스트
    private File currentPhotoFile;
    List<String> photoPaths;
    private PhotoAdapter photoAdapter;
    // 사진 지울시 확인 버튼 감지용
    public Boolean pest=true;
    List<File> currentList=new ArrayList<>();
    // 수목 기본 정보
    RegistTreeBasicInfoViewModel treeBasicInfoVM;
    TreeBasicInfoDTO treeBasicInfoDTO;
    Boolean click=false;
    // 카카오 맵
    private KakaoMapFragment kakaoMapFragment;
    // 팝업버튼 확인
    int num;
    int result;

    // 수목 위치상세 정보
    RegistTreeSpecificLocationInfoFragment registTreeSpecificLocationInfoFr;
    RegistTreeSpecificLocationInfoViewModel registTreeSpecificLocationInfoVM;
    Boolean sidewalk;
    // 수목 상태 정보
    RegistTreeStatusInfoFragment registTreeStatusInfoFr;
    // 환경 정보
    RegistEnvironmentInfoFragment registEnvironmentInfoFr;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        writeActBinding= DataBindingUtil.setContentView(this, R.layout.write_act);
        writeActBinding.setLifecycleOwner(this);
        treeInfoVM=new RegistTreeInfoViewModel();
        writeActBinding.setTreeInfoVM(treeInfoVM);

        submitter=sharedPreferences.getString("id", null);
        vendor=sharedPreferences.getString("company", null);
        // 화면에 보일 프레그먼트
        // NFC 코드 추출
        idHex=getIntent().getStringExtra("IDHEX");
        Log.d(TAG,"** 아이디 확인 **"+idHex);
        // 수목 기본 정보
        treeBasicInfoVM=new ViewModelProvider(this).get(RegistTreeBasicInfoViewModel.class);
        // 콜백 연결
        treeInfoVM.setCallback(this);

        // 프레그먼트들
        registTreeBasicInfoFr=new RegistTreeBasicInfoFragment();
        registTreeSpecificLocationInfoFr=new RegistTreeSpecificLocationInfoFragment();
        registTreeStatusInfoFr=new RegistTreeStatusInfoFragment();
        registEnvironmentInfoFr=new RegistEnvironmentInfoFragment();

        treeInfoVM.registTitle.set("기본 정보 입력");
        getSupportFragmentManager().beginTransaction().replace(R.id.write_content, new RegistTreeBasicInfoFragment()).commit();

        try {
            setTreeBasicInfoDTO();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        onCamera();

        treeBasicInfoVM.imgCount.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String event) {
                treeBasicInfoVM.cnt+=1;
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
                if (satellitesCount==6){findViewById(R.id.loading_layout_box).setVisibility(View.GONE);}
                if (satellitesCount>6){
                    // 위도 경도 가져오기
                    getLocation();
                    // 디자인 요소 반영
                    getTreeLocation();
                    click=true;
                }
            }
        });

        // 카카오맵
        setKakaoMapFragment(R.id.treeBasic_kakao_map);
        registTreeSpecificLocationInfoVM=new ViewModelProvider(this).get(RegistTreeSpecificLocationInfoViewModel.class);
    }//./onCreate



    // 등록 호출 1
    @SuppressLint("WrongViewCast")
    @Override
    public void onCustomCallback() {
        if(!click){
            // 거짓이라면..
            Toast.makeText(getApplicationContext(), "위성 감지 중입니다. 잠시만 기다려주세요.", Toast.LENGTH_LONG).show();

        }else {
            //팝업 창 띄우기
            AlertDialog.Builder builder = new AlertDialog.Builder(RegistTreeInfoActivity.this);
            builder.setTitle("입력하신 내용을 저장하시겠습니까?");
            builder.setMessage("");
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mappingDTO();
                    // 확인 버튼을 눌렀을 때
                }
            });
            builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //registerTreeInfo();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }



    // 등록 호출 2
    public void mappingDTO(){
        num++;

        if(num==BASIC){
            registerTreeBasicInfo();
            if(currentList.size()>0){
                registerTreeImage(currentList);
            }
            registerTreeLocationInfo();

        } else if (num == SPACIFICLOCATION) {
            registerSpecificLocationInfo();

        } else if (num == STATUS) {
            registerTreeStatusInfo();

        } else if (num == ENVIRONMENT) {
            registerTreeEnvironmentInfo();
        }
    }



    // 등록 호출 3
    // PostMethod (공통)
    public void registerTreeInfo(JSONObject postData, String postUrl){
        OkHttpClient client = new OkHttpClient();
        Log.d(TAG,"** 보낼 데이터 모습 **"+postData);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), postData.toString());
        String url = sndUrl+postUrl;
        // 요청 생성
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", sharedPreferences.getString("Authorization", null))
                .post(requestBody)
                .build();
        final boolean[] runCalled = {false}; // 플래그 변수

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) throws IOException {
                if (response.isSuccessful()){
                    String responseData = response.body().string();
                    Log.d(TAG,"** 성공 / 응답 **"+responseData);

                    RegistTreeInfoActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (response.isSuccessful()) {
                                if(num==BASIC){
                                    AlertDialog("수목 기본 정보가 등록되었습니다", "이어서 위치 상세 정보를 등록하시겠습니까?"); // UI 작업 수행

                                } else if (num == STATUS) {
                                    AlertDialog("수목 상태 정보가 등록되었습니다", "이어서 환경 정보를 등록하시겠습니까?"); // UI 작업 수행

                                } else if (num == ENVIRONMENT) {
                                    AlertDialog("수목 환경 정보가 등록되었습니다", ""); // UI 작업 수행
                                }
                            }

                        }
                    });

                }else{
                    String responseData = response.body().string();
                    Log.d(TAG,"** 실패 / 응답 **"+responseData);
                }
            }
            @Override
            public void onFailure(Call call, IOException e) {
                // 여기에 요청이 실패했을 때 실행될 코드를 작성하세요.
                Log.d(TAG,"** 오류남 **");
            }
        });
        Toast.makeText(this, "등록되었습니다", Toast.LENGTH_SHORT).show();
    }


    // PatchMethod (수목 위치 상세 정보)
    public void patchTreeInfo(JSONObject postData, String postUrl){
        OkHttpClient client = new OkHttpClient();
        Log.d(TAG,"** 보낼 데이터 모습 **"+postData);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), postData.toString());
        String url = sndUrl+postUrl;
        // 요청 생성
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", sharedPreferences.getString("Authorization", null))
                .patch(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) throws IOException {
                if (response.isSuccessful()){
                    String responseData = response.body().string();
                    Log.d(TAG,"** 성공 / 응답 **"+responseData);

                    RegistTreeInfoActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog("수목의 위치 상세 정보가 등록되었습니다", "이어서 수목 상태 정보를 등록하시겠습니까?"); // UI 작업 수행
                        }
                    });

                }else{
                    String responseData = response.body().string();
                    Log.d(TAG,"** 실패 / 응답 **"+responseData);
                }
            }
            @Override
            public void onFailure(Call call, IOException e) {
                // 여기에 요청이 실패했을 때 실행될 코드를 작성하세요.
                Log.d(TAG,"** 오류남 **");
            }
        });
        Toast.makeText(this, "등록되었습니다", Toast.LENGTH_SHORT).show();
    }


    // 등록 호출 4
    public void AlertDialog(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistTreeInfoActivity.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 프레그먼트 변경 예정
                if(num ==BASIC) {
                    switchFragment(registTreeSpecificLocationInfoFr);
                    // 위치 상세정보 출력
                    initSpecificLocationFr();

                } else if (num == SPACIFICLOCATION) {
                    switchFragment(registTreeStatusInfoFr);
                    initStatusInfoFr();

                } else if (num == STATUS) {
                    switchFragment(registEnvironmentInfoFr);
                    initEnvironmentInfoFr();

                } else if (num == ENVIRONMENT) {
                    // 메인화면으로 인텐트 전환!!
                    Intent intent=new Intent(RegistTreeInfoActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    /* -----------------------------------------------
            매핑 메서드들
         ---------------------------------------------- */
    // 1-1) 수목 기본정보 등록
    public void registerTreeBasicInfo(){
        JSONObject treeBasicData=new JSONObject();
        try {
            // 입력 데이터 보내기
            treeBasicData.put("nfc", idHex);
            treeBasicData.put("species", getInputText(findViewById(R.id.tr_name)));
            treeBasicData.put("submitter", submitter);
            treeBasicData.put("vendor", vendor);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        registerTreeInfo(treeBasicData, "/app/tree/registerBasicInfo");
    }


    // 1-2) 수목 위치(기본)정보 등록
    public void registerTreeLocationInfo(){
        JSONObject treeLocationData=new JSONObject();
        try {
            // 입력 데이터 보내기
            String latitudeValue = String.format("%.7f", latitude);     // 자릿수 맞추기
            treeLocationData.put("latitude", latitudeValue);
            String longitudeValue = String.format("%.7f", longitude);
            treeLocationData.put("longitude", longitudeValue);
            treeLocationData.put("nfc", idHex);
            treeLocationData.put("submitter",submitter);
            treeLocationData.put("vendor", vendor);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        registerTreeInfo(treeLocationData, "/app/tree/registerLocationInfo");
    }


    // 1-3) 사진 리스트 등록
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


    // 2) 위치상세 정보 등록
    public void  registerSpecificLocationInfo(){
        JSONObject treeSpecificLocationData=new JSONObject();
        try {
            treeSpecificLocationData.put("nfc", idHex);
            treeSpecificLocationData.put("sidewalk", sidewalk);
            treeSpecificLocationData.put("distance", getInputText(findViewById(R.id.specificLocation_distance)));
            treeSpecificLocationData.put("carriageway", getInputText(findViewById(R.id.specificLocation_carriageway)));

        } catch (JSONException e){
            e.printStackTrace();
        }
        patchTreeInfo(treeSpecificLocationData, "/app/tree/registerSpecificLocationInfo");
    }


    // 3) 수목 상태정보 등록
    public void registerTreeStatusInfo(){
        JSONObject treeStatusData=new JSONObject();
        try {
            // 입력 데이터 보내기
            treeStatusData.put("creation", LocalDate.now());
            treeStatusData.put("nfc", idHex);
            treeStatusData.put("dbh", getInputText(findViewById(R.id.treeStatus_scarlet_diam)));
            treeStatusData.put("rcc", getInputText(findViewById(R.id.treeStatus_tr_height)));
            treeStatusData.put("height", getInputText(findViewById(R.id.treeStatus_crw_height)));
            treeStatusData.put("length", getInputText(findViewById(R.id.treeStatus_crw_diam)));
            treeStatusData.put("width", getInputText(findViewById(R.id.treeStatus_pest_dmg_state)));
            treeStatusData.put("pest", pest);
            treeStatusData.put("submitter", submitter);
            treeStatusData.put("vendor", vendor);
            treeStatusData.put("modified", null);
            treeStatusData.put("inserted", null);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "** 상태정보 확인 **"+treeStatusData);
        registerTreeInfo(treeStatusData, "/app/tree/registerStatusInfo");
    }


    // 4) 환경 정보 등록
    public void registerTreeEnvironmentInfo(){
        JSONObject environmentData=new JSONObject();
        try {
            // 입력 데이터 보내기
            environmentData.put("nfc", idHex);
            environmentData.put("frameHorizontal",  getInputText(findViewById(R.id.grd_fr_width)));
            environmentData.put("frameVertical",  getInputText(findViewById(R.id.grd_fr_height)));
            environmentData.put("frameMaterial",  getInputText(findViewById(R.id.environment_material)));
            environmentData.put("boundaryStone", getInputText(findViewById(R.id.boundary_stone)));
            environmentData.put("roadWidth", getInputText(findViewById(R.id.road_width)));
            environmentData.put("sidewalkWidth", getInputText(findViewById(R.id.sidewalk_width)));
            environmentData.put("packingMaterial",  getInputText(findViewById(R.id.packing_material)));
            environmentData.put("soilPH",  getInputText(findViewById(R.id.soil_ph)));
            environmentData.put("soilDensity",  getInputText(findViewById(R.id.soil_density)));
            environmentData.put("submitter",   submitter);
            environmentData.put("vendor", vendor);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        registerTreeInfo(environmentData, "/app/tree/registerEnvironmentInfo");
    }



    /* --------------------------------------------------------
            프레그먼트 설정 관련
        -------------------------------------------------------- */
    public void switchFragment(Fragment frName){
        Log.d(TAG, "**프레그먼트 이름 확인 ** "+frName);
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.write_content, frName);
        transaction.commit();
    }


    // 카카오맵 전환
    public void setKakaoMapFragment(int viewId){
        kakaoMapFragment = new KakaoMapFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(viewId, kakaoMapFragment)
                .commit();
    }


    public void initEnvironmentInfoFr(){
        treeInfoVM.registTitle.set("환경 정보 입력");
        setKakaoMapFragment(R.id.environment_map_layout);
        RegistEnvironmentInfoViewModel registEnvironmentInfoVM=new ViewModelProvider(this).get(RegistEnvironmentInfoViewModel.class);
        registEnvironmentInfoVM.setCallback(this);
        registEnvironmentInfoVM.idHex.set(idHex);
    }


    public void initStatusInfoFr(){
        treeInfoVM.registTitle.set("수목 상태 정보 입력");
        setKakaoMapFragment(R.id.treeStatus_map_layout);
        RegistTreeStatusInfoViewModel registTreeStatusInfoVM=new ViewModelProvider(this).get(RegistTreeStatusInfoViewModel.class);
        registTreeStatusInfoVM.setCallback(this);
        registTreeStatusInfoVM.idHex.set(idHex);
    }


    public void initSpecificLocationFr(){
        treeInfoVM.registTitle.set("위치 상세 정보 입력");
        setKakaoMapFragment(R.id.specificLocation_map_layout);
        registTreeSpecificLocationInfoVM.setCallback(this);
        registTreeSpecificLocationInfoVM.idHex.set(idHex);
    }


    // 디자인 요소에 세팅하기
    public void getTreeLocation(){
        treeBasicInfoVM.latitude.set(""+latitude);
        treeBasicInfoVM.longitude.set(""+longitude);
        kakaoMapFragment.addMarkers(latitude,longitude, idHex);
    }


    public void setTreeBasicInfoDTO() throws JsonProcessingException {
        treeBasicInfoDTO=new TreeBasicInfoDTO();
        treeBasicInfoDTO.setNFC(idHex);
        //treeBasicInfoDTO.setSpecies(sharedPreferences.getString("species",null));
        treeBasicInfoDTO.setSubmitter(sharedPreferences.getString("id",null));
        treeBasicInfoDTO.setVendor(sharedPreferences.getString("company",null));
        Log.d(TAG, "** 디티오 확인 ** "+treeBasicInfoDTO.getSubmitter());
        // 매핑된 DTO 넘겨줌
        treeBasicInfoVM.setTextViewModel(treeBasicInfoDTO);
    }



    @Override
    protected void onResume() {
        super.onResume();
        findViewById(R.id.loading_layout_box).setVisibility(View.VISIBLE);
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


    // 리스너 메소드들
    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
    }
    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
    }
    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {
    }
    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {
    }


    // 스피너 관련
    @Override
    public void onSpinnerValueChanged(String value) {
        Log.d(TAG, "** 스피너 값 ** " + value);
        if(value.equals("없음 X")){
            sidewalk=false;
        }else {
            sidewalk=true;
        }
    }


}
