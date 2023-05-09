package com.snd.app.data;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.snd.app.ui.login.LoginActivity;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class JsonWebTokenUtil extends Request<String> {

    private final Response.Listener<String> mListener;

    public JsonWebTokenUtil(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
    }

    @Override
    protected Map<String, String> getParams() {
        return null;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        String token = "";
        headers.put("Authorization", "Bearer " + token);
        return headers;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String responseString = "";
        if (response != null) {
            responseString = new String(response.data, Charset.forName("UTF-8"));
        }
        return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }

}
