package com.peihuo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView view = (ImageView) findViewById(R.id.main_image);
        Glide.with(this).load("http://p3.ifengimg.com/a/2017_33/382773bac7ea6a6_size101_w754_h532.jpg").into(view);
    }
}
