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
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.snd.app.R;
import com.snd.app.common.TMActivity;
import com.snd.app.data.KakaoMapFragment;
import com.snd.app.data.LocationRepository;
import com.snd.app.databinding.RegistTreeBasicInfoFrBinding;
import com.snd.app.databinding.WriteActBinding;
import com.snd.app.domain.tree.TreeBasicInfoDTO;
import com.snd.app.ui.tree.PhotoAdapter;
import com.snd.app.ui.tree.SpaceItemDecoration;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class RegistTreeInfoActivity extends TMActivity implements MyCallback, MapView.POIItemEventListener{
    WriteActBinding writeActBinding;
    RegistTreeInfoViewModel treeInfoVM;
    private RegistTreeBasicInfoFragment registTreeBasicInfoFr;

    String idHex;

    // 이미지 권한
    private static final int REQUEST_PERMISSION = 1;
    // 이미지 리스트
    private File currentPhotoFile;
    List<String> photoPaths;
    private PhotoAdapter photoAdapter;
    // 사진 지울시 확인 버튼 감지용
    public Boolean flag=true;
    List<File> currentList=new ArrayList<>();
    // 수목 기본 정보
    RegistTreeBasicInfoViewModel treeBasicInfoVM;
    TreeBasicInfoDTO treeBasicInfoDTO;
    Boolean click=false;
    // 카카오 맵
    private KakaoMapFragment kakaoMapFragment;
    // 팝업버튼 확인
    int num;
    //String species;

    RegistTreeSpecificLocationInfoFragment registTreeSpecificLocationInfoFr;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        writeActBinding= DataBindingUtil.setContentView(this, R.layout.write_act);
        writeActBinding.setLifecycleOwner(this);
        treeInfoVM=new RegistTreeInfoViewModel();
        writeActBinding.setTreeInfoVM(treeInfoVM);

        // 화면에 보일 프레그먼트
        registTreeBasicInfoFr=new RegistTreeBasicInfoFragment();
        // NFC 코드 추출
        idHex=getIntent().getStringExtra("IDHEX");
        Log.d(TAG,"** 아이디 확인 **"+idHex);
        // 수목 기본 정보
        treeBasicInfoVM=new ViewModelProvider(this).get(RegistTreeBasicInfoViewModel.class);
        // 콜백 연결
        treeInfoVM.setCallback(this);
        // 수몯 위치상세 정보
        registTreeSpecificLocationInfoFr=new RegistTreeSpecificLocationInfoFragment();


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
                if (satellitesCount>6){
                    click=true;
                    // 위도 경도 가져오기
                    getLocation();

                    // 디자인 요소 반영
                    getTreeLocation();

                    // 로딩 객체 해제
                    //findViewById(R.id.loading_layout_box).setVisibility(View.GONE);
                }
            }
        });

        // 카카오맵
        kakaoMapFragment = new KakaoMapFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.treeBasic_kakao_map, kakaoMapFragment)
                .commit();
    }//./onCreate




    public void mappingDTO(){
        registerTreeBasicInfo();
        if(currentList.size()>0){
            registerTreeImage(currentList);
        }
        registerTreeLocationInfo();
    }



    // 화면 전환
    public void AlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistTreeInfoActivity.this);
        builder.setTitle("수목 기본 정보가 등록되었습니다");
        builder.setMessage("이어서 위치 상세 정보를 등록하시겠습니까?");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               Log.d(TAG, "** 확인 버튼 누름**");
                // 프레그먼트 변경 예정
                switchFragment();

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



    public void switchFragment(){
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.write_content, registTreeSpecificLocationInfoFr);
        transaction.commit();
    }



    // 1-1) 수목 기본정보 등록
    public void registerTreeBasicInfo(){
        JSONObject treeBasicData=new JSONObject();
        try {
            // 입력 데이터 보내기
            treeBasicData.put("nfc", idHex);
            treeBasicData.put("species", getInputText(findViewById(R.id.tr_name)));
            treeBasicData.put("submitter", sharedPreferences.getString("id",null));
            treeBasicData.put("vendor", sharedPreferences.getString("company",null));
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
            treeLocationData.put("nfc",treeBasicInfoDTO.getNFC().toUpperCase());
            treeLocationData.put("submitter",treeBasicInfoDTO.getSubmitter());
            treeLocationData.put("vendor",treeBasicInfoDTO.getVendor());
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
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) throws IOException {
                if (response.isSuccessful()){
                    String responseData = response.body().string();
                    Log.d(TAG,"** 성공 / 응답 **"+responseData);

                    RegistTreeInfoActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog(); // UI 작업 수행
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
    }



    // 뷰모델에서 호출 - 저장 버튼 누를 시
    @Override
    public void onCustomCallback() {
        Log.d(TAG, "** 반응 하는지부터 확인 **");
        //팝업 창 띄우기
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistTreeInfoActivity.this);
        builder.setTitle("입력하신 내용을 저장하시겠습니까?");
        builder.setMessage("수목 기본 정보 등록");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mappingDTO();
                // 확인 버튼을 눌렀을 때
                num=which;
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //registerTreeInfo();
                num=which;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    // 디자인 요소에 세팅하기
    public void getTreeLocation(){
        treeBasicInfoVM.latitude.set(""+latitude);
        treeBasicInfoVM.longitude.set(""+longitude);
        // 중심점 변경
        //mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude), true);
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
        //findViewById(R.id.loading_layout_box).setVisibility(View.VISIBLE);
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


}