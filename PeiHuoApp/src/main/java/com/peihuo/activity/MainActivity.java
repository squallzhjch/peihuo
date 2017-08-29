package com.peihuo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

//import com.bumptech.glide.Glide;

import com.peihuo.R;

import java.io.IOException;

//import okhttp3.ResponseBody;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//import retrofit2.http.Field;
//import retrofit2.http.FormUrlEncoded;
//import retrofit2.http.GET;
//import retrofit2.http.POST;
//import retrofit2.http.Path;
//import retrofit2.http.Query;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView view = (ImageView) findViewById(R.id.main_image);
//        Glide.with(this).load("http://p3.ifengimg.com/a/2017_33/382773bac7ea6a6_size101_w754_h532.jpg").into(view);
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://192.168.4.188:9700/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        BlogService service = retrofit.create(BlogService.class);
//        Call<ResponseBody> call = service.getBlog("{\"userNickName\":\"hanxuesong01664\",\"userPassword\":\"016640\",\"deviceVersion\":\"2.0.28124\",\"devicePlatform\":\"Android\"}");
//        // 用法和OkHttp的call如出一辙,
//        // 不同的是如果是Android系统回调方法执行在主线程
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                try {
//                    Log.e("jingo",response.body().string());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                t.printStackTrace();
//            }
//        });
//    }
//
//    public interface BlogService {
//        @GET("service/man/userInfo/login/")
//        Call<ResponseBody> getBlog(@Query("parameter") String parameter);
    }
}
