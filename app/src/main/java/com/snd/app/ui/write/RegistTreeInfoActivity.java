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

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        getSupportFragmentManager().beginTransaction().replace(R.id.write_content, new RegistTreeBasicInfoFragment()).commit();

        //photoAdapter=new PhotoAdapter();
        //Log.d(TAG,"** 액티비티에서 어댑터 확인 **"+photoAdapter);

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
        //ㄷ
        treeBasicInfoVM.back.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegistTreeInfoActivity.this);
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
                    click=true;
                    // 위도 경도 가져오기
                    getLocation();

                    // 디자인 요소 반영
                    getTreeLocation();

                    // 로딩 객체 해제
                    findViewById(R.id.loading_layout_box).setVisibility(View.GONE);
                }
            }
        });


        // 카카오맵
        kakaoMapFragment = new KakaoMapFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.treeBasic_kakao_map, kakaoMapFragment)
                .commit();

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
    public void onCustomCallback() {
    }
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
