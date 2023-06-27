package com.snd.app.ui.tree;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import com.snd.app.R;
import com.snd.app.common.TMActivity;
import com.snd.app.databinding.TreeActBinding;
import com.snd.app.ui.read.GetTreeInfoActivity;
import com.snd.app.ui.write.RegistTreeInfoActivity;


public class TreeActivity extends TMActivity implements NfcAdapter.ReaderCallback {
    private String TAG=this.getClass().getName();

    TreeActBinding treeActBinding;
    public NfcAdapter nfcAdapter = null;
    public PendingIntent nfcPendingIntent;
    TreeViewModel treeVM;

    // 전달할 NFC 코드
    private static final String IDHEX="IDHEX";
    String idHex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "** NFC 클래스 ** ");

        treeActBinding= DataBindingUtil.setContentView(this, R.layout.tree_act);
        treeActBinding.setLifecycleOwner(this);
        treeVM=new TreeViewModel();
        treeActBinding.setTreeVM(treeVM);

        initNfc();

        treeVM.back.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                finish();
            }
        });
    }


    // NFC 인식
    @Override
    public void onTagDiscovered(Tag tag) {
        Log.d(TAG, "** NFC 인식하였음 !! ** ");
        byte[] id = tag.getId();
        idHex = bytesToHexString(id).toUpperCase();
        Log.d(TAG, "** NFC 아이디 추출 ** "+id);
        Log.d(TAG, "** NFC 아이디 가공 ** "+idHex);

        // 화면 전환하기
        // 이 근거는 뭘까..
        switchActivity("RegistTreeInfoActivity");
        Log.d(TAG, "** actName 확인dddd ** "+getIntent().getStringExtra("actName"));


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(TreeActivity.this, "NFC 발견", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // 이미 액티비티의 이름을 받아서 처리하는 메소드
    public void switchActivity(String actName){
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


    @Override
    protected void onResume() {
        super.onResume();

        // NFC 리더 모드 활성화
        if (nfcAdapter != null) {
            nfcAdapter.enableReaderMode(this, this, NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK, null);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();

        // NFC 리더 모드 비활성화
        if (nfcAdapter != null) {
            nfcAdapter.disableReaderMode(this);
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            // NFC 태그가 감지되었을 때 처리
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String tagUid = bytesToHexString(tag.getId()); // 태그의 UID를 가져옴

            // 태그의 UID 처리
            Toast.makeText(this, "NFC 태그 UID: " + tagUid, Toast.LENGTH_SHORT).show();

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
