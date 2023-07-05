package com.snd.app.ui.tree;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import com.snd.app.R;
import com.snd.app.common.TMActivity;
import com.snd.app.data.IntentManager;
import com.snd.app.data.NfcManager;
import com.snd.app.databinding.TreeActBinding;
import com.snd.app.ui.read.GetTreeInfoActivity;
import com.snd.app.ui.write.RegistTreeInfoActivity;


public class TreeActivity extends TMActivity{
    private String TAG=this.getClass().getName();

    TreeActBinding treeActBinding;
    public NfcAdapter nfcAdapter = null;
    public PendingIntent nfcPendingIntent;
    TreeViewModel treeVM;
    String actVersion;

    // 전달할 NFC 코드
    private static final String IDHEX="IDHEX";
    String idHex;

    private boolean isTagDiscovered = false;
    NfcManager nfcManager;

    IntentManager intentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "** NFC 클래스 ** ");

        treeActBinding= DataBindingUtil.setContentView(this, R.layout.tree_act);
        treeActBinding.setLifecycleOwner(this);
        treeVM=new TreeViewModel();
        treeActBinding.setTreeVM(treeVM);

        actVersion=getIntent().getStringExtra("actVersion");
        setTitleText();

        initNfc();

        treeVM.back.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                finish();
            }
        });
        NFC_MODE=1;

        nfcManager = new NfcManager(this);
        nfcManager.ignore();

        intentManager=new IntentManager(this);

    }// ./onCeate



    public void setTitleText(){
        if(actVersion.equals("read")){
            treeVM.titleText.set("수목 진단 정보 조회를 위해\n NFC 칩을 태그하세요.");
        } else {
            treeVM.titleText.set("수목 정보 등록을 위해\n NFC 칩을 태그하세요.");
        }
    }



    // 이미 액티비티의 이름을 받아서 처리하는 메소드
    public void switchActivity(String actName){
        Log.d(TAG, "** switchActivity 호출 ** ");

        Intent intent = null;
        switch (actName){
            case "RegistTreeInfoActivity":
                intent = new Intent(this, RegistTreeInfoActivity.class); break;
            case "GetTreeInfoActivity":
                intent = new Intent(this, GetTreeInfoActivity.class); break;
        }
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
            enableNfcForegroundDispatch(pendingIntent);
            intent.putExtra(IDHEX, idHex);
            startActivity(intent);
        }
    }



    // NFC 초기화 설정
    private void initNfc() {
        Log.d(TAG, "** NFC 초기화 호출 ** ");
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_IMMUTABLE);

        if (nfcAdapter == null) {
            // 기기가 NFC를 지원하지 않을 경우 처리
            Toast.makeText(this, "NFC를 지원하지 않는 기기입니다.", Toast.LENGTH_SHORT).show();
        } else if (!nfcAdapter.isEnabled()) {
            // NFC가 비활성화된 경우 처리
            Toast.makeText(this, "NFC가 비활성화되어 있습니다. 설정에서 활성화해주세요.", Toast.LENGTH_SHORT).show();
        }
    }



    private static final long DEBOUNCE_TIME_MS = 1000; // Adjust this value as needed
    private long lastTagDiscoveryTime;

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null) {
            nfcAdapter.enableForegroundDispatch(this, nfcPendingIntent, null, null);
        }
        // NFC 리더 모드 활성화
        if (nfcAdapter != null) {
            nfcAdapter.enableReaderMode(this, new NfcAdapter.ReaderCallback() {
                @Override
                public void onTagDiscovered(Tag tag) {
                    Log.d(TAG, "** NFC 인식하였음 !! ** ");
                    // 여기서 아무것도 수행하지 않으면 된다.

                    byte[] id = tag.getId();
                    idHex = bytesToHexString(id).toUpperCase();
                    Log.d(TAG, "** NFC 아이디 추출 ** "+id);
                    Log.d(TAG, "** NFC 아이디 가공 ** "+idHex);
                    //setIsTagDiscovered();

                    long now = System.currentTimeMillis();
                    if (now - lastTagDiscoveryTime < DEBOUNCE_TIME_MS) {
                        // Ignore this event, since it's too close to the last one
                        return;
                    }
                    lastTagDiscoveryTime = now;

                    switchActivity(getIntent().getStringExtra("actName"));

                    nfcManager.handleTag(tag);
                }
            }, NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK, null);
        }
    }



    public void test(){
        Log.d(TAG, "** 실행하러 온더 !! ** ");

        // NFC 읽기 모드 비활성화
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "** run 호출 ** ");
                Log.d(TAG, "** run 호출 ** "+getIntent().getStringExtra("actName"));

                switchActivity(getIntent().getStringExtra("actName"));


            }
        });

    }



    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // Handle NFC tag here
    }



    @Override
    protected void onPause() {
        Log.d(TAG, "** onPause 호출 !! **");
        super.onPause();
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }


    private void enableNfcForegroundDispatch(PendingIntent pendingIntent) {
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
    }


    private void disableNfcForegroundDispatch() {
        nfcAdapter.disableForegroundDispatch(TreeActivity.this);
    }


    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }



}
