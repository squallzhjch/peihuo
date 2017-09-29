package com.peihuo.net;


import android.util.Log;

import com.peihuo.system.SystemConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by hb on 2017/9/23.
 */

public class NetManager {
    private static volatile NetManager mInstance;
    private Retrofit retrofit;
    private NetService netService;
    public static NetManager getInstance() {
        if (mInstance == null) {
            synchronized (NetManager.class) {
                if (mInstance == null) {
                    mInstance = new NetManager();
                }
            }
        }
        return mInstance;
    }

    private NetManager() {
//        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
//            @Override
//            public void log(String message) {
//                Log.e("jingo", message);
//            }
//        });
//        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        InterceptorLog log = new InterceptorLog();
        OkHttpClient client = new OkHttpClient.Builder().
                addInterceptor(log).
                addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                                .build();
                        return chain.proceed(request);
                    }
                }).
                retryOnConnectionFailure(true).
                connectTimeout(15, TimeUnit.SECONDS)
//                .addNetworkInterceptor(authorizationInterceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(SystemConfig.APP_IP)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

       netService =  retrofit.create(NetService.class);
    }


    public <T> T createCallback(Class<T> service) {
        if (retrofit != null)
            return retrofit.create(service);
        else
            return null;
    }

    public NetService getNetService(){
        return netService;
    }

    public interface NetService {
        @GET("user/login")
        Call<ResponseBody> login(@Query("account") String username, @Query("password") String password);

        @GET("androidInterface/sortingList")
        Call<ResponseBody> querySortingList(@Query("userID") String userID, @Query("pageNum") int pageNum, @Query("pageSize") int pageSize);

        @GET("androidInterface/findPipeline")
        Call<ResponseBody> queryWorkLines(@Query("repCode") String repCode);

        @GET("androidInterface/acceptanceList")
        Call<ResponseBody> queryAcceptanceList(@Query("lineNo") String lineNo, @Query("pageNum") int pageNum, @Query("pageSize")int pageSize,  @Query("state")int state);

        @GET("androidInterface/sortingInfo")
        Call<ResponseBody> querySortingInfo(@Query("ordercode") String ordercode, @Query("humanId") String userid);

        @GET("androidInterface/sortingPass")
        Call<ResponseBody> updateSortingPass(@Query("userId") String userId, @Query("id") String id);

        @GET("androidInterface/acceptancePass")
        Call<ResponseBody> updateAcceptancePass(@Query("id") String id, @Query("userId") String userId);

        @GET("androidInterface/acceptanceError")
        Call<ResponseBody> updateAcceptanceError(@Query("id") String id, @Query("userId") String userId);


        @GET("androidInterface/acceptanceInfo")
        Call<ResponseBody> queryAcceptanceInfo(@Query("acceptancecode") String acceptancecode);


    }
}
