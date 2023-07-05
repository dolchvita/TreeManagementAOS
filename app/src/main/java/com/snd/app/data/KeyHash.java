package com.snd.app.data;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

// 카카오맵 API 등록시 필요한 키해시값 추출
public class KeyHash {
    protected String TAG = this.getClass().getName();

    public static String getKeyHash(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(
                    context.getPackageName(),
                    PackageManager.GET_SIGNATURES
            );
            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            Log.d("KeyHashExtractor", "Error: " + e.getMessage());
        }
        return null;
    }



}
