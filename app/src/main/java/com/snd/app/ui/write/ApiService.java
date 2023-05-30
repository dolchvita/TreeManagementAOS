package com.snd.app.ui.write;

import org.json.JSONObject;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {

    @Multipart
    @POST("registerTreeImage")
    Call<JSONObject> registerTreeImage(
            @Part("tagId") String tagId,
            @Part MultipartBody.Part image1,
            @Part MultipartBody.Part image2
    );
}
