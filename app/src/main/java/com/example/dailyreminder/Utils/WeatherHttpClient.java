package com.example.dailyreminder.Utils;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import cz.msebera.android.httpclient.entity.StringEntity;

public class WeatherHttpClient {
    private static String BASE_URL = "https://api.openweathermap.org/data/2.5/weather?q=";
    public static String IMG_URL = "https://openweathermap.org/img/w/";
    private static String APPID = "538b1f109f84a4fb26067c4032b83044";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(Context ctx, String url, StringEntity entity, java.lang.String contentType, AsyncHttpResponseHandler responseHandler ){
        client.post(ctx,getAbsoluteUrl(url),entity,contentType,responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
