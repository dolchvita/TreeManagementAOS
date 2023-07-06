package com.snd.app.ui.read;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.snd.app.R;
import com.snd.app.common.TMActivity;
import com.snd.app.data.KakaoMapFragment;
import com.snd.app.data.LocalDateTimeAdapter;
import com.snd.app.data.NfcManager;
import com.snd.app.databinding.ReadActBinding;
import com.snd.app.domain.tree.TreeIntegratedVO;
import com.snd.app.ui.write.MyCallback;
import com.snd.app.ui.write.RegistTreeInfoActivity;
import com.snd.app.ui.write.RegistTreeSpecificLocationInfoViewModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class GetTreeInfoActivity extends TMActivity implements AdapterView.OnItemSelectedListener, MyCallback {
    private String idHex;
    private KakaoMapFragment kakaoMapFragment;
    GetTreeBasicInfoViewModel getTreeBasicInfoVM;

    // 프레그먼트들
    GetTreeBasicInfoFragment getTreeBasicInfoFr;
    GetTreeSpecificLocationFragment getTreeSpecificLocationFr;
    GetTreeStatusFragment getTreeStatusFr;
    GetEnvironmentFragment getEnvironmentFr;
    NfcReadFragment nfcReadFragment;
    TreeIntegratedVO treeIntegratedVO;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initNfc();
        ReadActBinding readActBinding= DataBindingUtil.setContentView(this, R.layout.read_act);
        readActBinding.setLifecycleOwner(this);
        GetTreeInfoViewModel getTreeInfoVM=new GetTreeInfoViewModel();
        readActBinding.setGetTreeInfoVM(getTreeInfoVM);
        getTreeBasicInfoVM=new ViewModelProvider(this).get(GetTreeBasicInfoViewModel.class);
        getTreeInfoVM.setCallback(this);

        getTreeBasicInfoFr=new GetTreeBasicInfoFragment();
        getTreeSpecificLocationFr=new GetTreeSpecificLocationFragment();
        getTreeStatusFr=new GetTreeStatusFragment();
        getEnvironmentFr=new GetEnvironmentFragment();
        nfcReadFragment=new NfcReadFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.read_content, nfcReadFragment).commit();

        // 스피너 설정
        Spinner spinner = findViewById(R.id.read_sipnner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.read_act_menu, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        onCamera();

        // 버튼 클릭하기
        getTreeBasicInfoVM.gps_btn.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                AlertDialog.Builder builder = new AlertDialog.Builder(GetTreeInfoActivity.this);
                builder.setTitle("위치를 변경하시겠습니까?");
                builder.setMessage("");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //getLocation();

                        // 지도 수정
                        //kakaoMapFragment.addMarkers(latitude,longitude, idHex);
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
        });

    }// ./onCreate



    /* ---------------------------- NFC ---------------------------- */

    private void initNfc() {
        Log.d(TAG, "** NFC 초기화 호출 ** ");
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        PendingIntent nfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_IMMUTABLE);

        if (nfcAdapter != null) {
            nfcAdapter.enableReaderMode(this, new NfcAdapter.ReaderCallback() {
                @Override
                public void onTagDiscovered(Tag tag) {
                    Log.d(TAG, "** NFC 인식하였음 !! 22 ** ");

                    byte[] id = tag.getId();
                    idHex = bytesToHexString(id).toUpperCase();
                    Log.d(TAG, "** NFC 아이디 추출 ** "+id);
                    Log.d(TAG, "** NFC 아이디 가공 ** "+idHex);

                    // 여기서 아무것도 수행하지 않으면 된다.
                    NfcManager nfcManager = new NfcManager(GetTreeInfoActivity.this);
                    nfcManager.handleTag(tag);

                    initBasicInfoFr();

                }
            }, NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK, null);
        }
    }


    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }


    /* ---------------------------------- FRAGMENT ---------------------------------- */

    public void initBasicInfoFr(){
        switchFragment(getTreeBasicInfoFr);
        // 카카오맵
        kakaoMapFragment = new KakaoMapFragment();
        setKakaoMapFragment(R.id.readTreeBasic_map_layout);
        getTreeInfoByNFCtagId();
        getTreeBasicInfoFr.idHex=idHex;
    }

    
    // 수정 버튼
    @Override
    public void onCustomCallback() {
        Log.d(TAG, "**수정 버튼 **");

        //팝업 창 띄우기
        AlertDialog.Builder builder = new AlertDialog.Builder(GetTreeInfoActivity.this);
        builder.setTitle("입력하신 내용으로 수정하시겠습니까?");
        builder.setMessage("");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //mappingDTO();
                // 확인 버튼을 눌렀을 때
                modifyTreeBasicInfo();
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



    public void modifyTreeBasicInfo(){
        JSONObject treeBasicData=new JSONObject();
        try {
            // 입력 데이터 보내기
            treeBasicData.put("nfc", idHex);
            treeBasicData.put("species", getInputText(findViewById(R.id.scarlet_diam)));
            treeBasicData.put("submitter", treeIntegratedVO.getBasicSubmitter());
            treeBasicData.put("vendor", treeIntegratedVO.getBasicVendor());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        putTreeInfo(treeBasicData, "/app/tree/modifyBasicInfo");
    }



    public void putTreeInfo(JSONObject putData, String postUrl){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), putData.toString());
        String url = sndUrl+postUrl;
        // 요청 생성
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", sharedPreferences.getString("Authorization", null))
                .put(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) throws IOException {
                if (response.isSuccessful()){
                    String responseData = response.body().string();
                    Log.d(TAG,"** 성공 / 응답 **"+responseData);

                    GetTreeInfoActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (response.isSuccessful()) {

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
                Log.d(TAG,"** 오류남 **");
            }
        });
        Toast.makeText(this, "수정되었습니다", Toast.LENGTH_SHORT).show();
    }



    public void setKakaoMapFragment(int viewId){
        getSupportFragmentManager().beginTransaction()
                .replace(viewId, kakaoMapFragment)
                .commit();
    }



    // 수목에 대한 모든 정보 가져오는 메서드 !
    public void getTreeInfoByNFCtagId(){
        String url = sndUrl+"/app/tree/getTreeInfo/"+idHex;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", sharedPreferences.getString("Authorization", null))
                .build();
        // 비동기 방식 사용하기 !!! - ** 네트워크 요청 이후 작업할 것 **
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) throws IOException {
                if (response.isSuccessful()){
                    String responseData = response.body().string();
                    Log.d(TAG,"** 위치 성공 / 응답 **"+responseData);

                    treeIntegratedVO=new TreeIntegratedVO();

                    try {
                        // 제이슨 데이터로 변환 후 추출
                        JSONObject json=new JSONObject(responseData);
                        JSONObject data=json.getJSONObject("data");
                        Log.d(TAG,"** data 추출 **"+data.toString());

                        treeIntegratedVO.setNFC(data.getString("nfc"));

                        // 기본 정보
                        treeIntegratedVO.setSpecies(data.getString("species"));
                        treeIntegratedVO.setBasicSubmitter(data.getString("basicSubmitter"));
                        treeIntegratedVO.setBasicVendor(data.getString("basicVendor"));
                        treeIntegratedVO.setLatitude(Double.parseDouble(data.getString("latitude")));
                        treeIntegratedVO.setLongitude(Double.parseDouble(data.getString("longitude")));
                        LocalDateTime basicInserted=deserialize(data.getString("basicInserted"));
                        treeIntegratedVO.setBasicInserted(basicInserted);
                        getTreeBasicInfoVM.setTextViewModel(treeIntegratedVO);
                        kakaoMapFragment.addMarkers(Double.parseDouble(data.getString("latitude")), Double.parseDouble(data.getString("longitude")), idHex);


                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
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


    public LocalDateTime deserialize(String inserted){
        String[] strArr = inserted.replace("[", "").replace("]", "").split(",");
        int[] intArr = new int[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            intArr[i] = Integer.parseInt(strArr[i]);
        }
        LocalDateTimeAdapter localDateTimeAdapter=new LocalDateTimeAdapter();
        return localDateTimeAdapter.test(intArr);
    }



    /* ----------------------------- 사진 조회 관련 ----------------------------- */

    // 이미지 리스트
    List<String> photoPaths;
    private File currentPhotoFile;
    List<File> currentList=new ArrayList<>();

    public void onCamera (){
        // 카메라 가동시키기
        getTreeBasicInfoVM.camera.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {

                AlertDialog.Builder builder = new AlertDialog.Builder(GetTreeInfoActivity.this);
                builder.setTitle("사진을 수정하시겠습니까?");
                builder.setMessage("기존 사진은 모두 삭제됩니다.");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 이 전에 먼저 사진 삭제하고 렌더링 하기

                        startCamera();
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

        if (getTreeBasicInfoVM.listData.getValue() != null && getTreeBasicInfoVM.currentList.size() >= 2) {
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



    /* ----------------------------- 프레그먼트 설정 관련 ----------------------------- */

    public void switchFragment(Fragment frName){
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.read_content, frName);
        transaction.commit();
    }



    /* ----------------------------- 스피너 설정 관련 ----------------------------- */

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "** 선택된 메뉴 값 ** "+position);

       if (position==1){
           switchFragment(getTreeBasicInfoFr);
           initBasicInfoFr();
       } else if (position == 2) {
           switchFragment(getTreeSpecificLocationFr);

       } else if (position == 3) {
           switchFragment(getTreeStatusFr);
       } else if (position == 4) {
           switchFragment(getEnvironmentFr);
       }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.d(TAG, "** 언제 호출되나요 ** ");
    }


}
