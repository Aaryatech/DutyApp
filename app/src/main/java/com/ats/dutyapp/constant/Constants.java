package com.ats.dutyapp.constant;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.ats.dutyapp.interfaces.InterfaceAPI;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Constants {
    public static final String BASE_URL ="http://192.168.2.7:9000/";
// public static final String BASE_URL = "http://132.148.151.41:8080/SecurityAppApi/";
    public static final String IMAGE_URL = "http://132.148.151.41:8080/uploads/APP/";


    public static OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .header("Accept", "application/json")
                            .method(original.method(), original.body())
                            .build();

                    Response response = chain.proceed(request);
                    return response;
                }
            })
            .readTimeout(10000, TimeUnit.SECONDS)
            .connectTimeout(10000, TimeUnit.SECONDS)
            .writeTimeout(10000, TimeUnit.SECONDS)
            .build();


    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create()).build();

    public static InterfaceAPI myInterface = retrofit.create(InterfaceAPI.class);


//    public static Retrofit retrofit1 = new Retrofit.Builder()
//            .baseUrl(BASE_URL_SPINNER)
//            .client(client)
//            .addConverterFactory(GsonConverterFactory.create()).build();
//
//    public static InterfaceAPI myInterface1 = retrofit1.create(InterfaceAPI.class);

    public static boolean isOnline(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
            Toast.makeText(context, "No Internet Connection ! ", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


}
