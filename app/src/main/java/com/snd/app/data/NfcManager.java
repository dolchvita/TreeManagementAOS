package com.snd.app.data;

import android.content.Context;
import android.nfc.NfcAdapter;
import android.nfc.Tag;


public class NfcManager {
    protected String TAG = this.getClass().getName();
    private NfcAdapter nfcAdapter;
    private boolean shouldIgnore = false;


    public NfcManager(Context context) {
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(context);
    }

    public void ignore() {
        shouldIgnore = true;
    }

    public void handleTag(Tag tag) {
        if (shouldIgnore) {
            shouldIgnore = false;
            return;
        }
        // 태그를 처리하는 코드...


    }


}
