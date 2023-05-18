package com.snd.app.ui.tree;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.snd.app.R;
import com.snd.app.common.TMActivity;
import com.snd.app.data.AppComponent;
import com.snd.app.data.AppModule;
import com.snd.app.data.DaggerAppComponent;
import com.snd.app.data.user.SharedPreferencesManager;
import com.snd.app.databinding.TreeActBinding;
import com.snd.app.domain.UserDTO;
import com.snd.app.domain.tree.TreeBasicInfoDTO;
import com.snd.app.sharedPreferences.SharedApplication;
import com.snd.app.ui.write.RegistTreeBasicInfoActivity;
import com.snd.app.ui.write.RegistTreeBasicInfoViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

public class TreeActivity extends TMActivity implements NfcAdapter.ReaderCallback {
    private String TAG=this.getClass().getName();

    TreeActBinding treeActBinding;
    public NfcAdapter nfcAdapter = null;
    public PendingIntent nfcPendingIntent;


    private static final String IDHEX="idHex";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "** NFC 클래스 ** ");

        treeActBinding= DataBindingUtil.setContentView(this, R.layout.tree_act);
        treeActBinding.setLifecycleOwner(this);

        initNfc();

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


    // NFC 인식
    @Override
    public void onTagDiscovered(Tag tag) {
        Log.d(TAG, "** NFC 인식하였음 !! ** ");

        byte[] id = tag.getId();
        String idHex = bytesToHexString(id);
        Log.d(TAG, "** NFC 아이디 추출 ** "+id);
        Log.d(TAG, "** NFC 아이디 가공 ** "+idHex);


        // 화면 전환하기
        Intent intent = new Intent(this, RegistTreeBasicInfoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        enableNfcForegroundDispatch(pendingIntent);

        intent.putExtra(IDHEX, idHex);
        startActivity(intent);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(TreeActivity.this, "NFC 발견", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void enableNfcForegroundDispatch(PendingIntent pendingIntent) {
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);

        //nfcAdapter.enableForegroundDispatch(this, PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE), null, null);
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
