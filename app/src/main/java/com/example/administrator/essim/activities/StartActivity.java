package com.example.administrator.essim.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.administrator.essim.R;


import static com.example.administrator.essim.utils.Constant.SPLASH_TIME;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE );
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_start);

        ImageView imageView = findViewById(R.id.splash_image);
        Glide.with(StartActivity.this).load(R.mipmap.one_splash).into(imageView);
        Handler handler = new Handler();
        //当计时结束时，跳转至主界面
        handler.postDelayed(() -> {
            startActivity(new Intent(StartActivity.this, MainActivity.class));
            finish();
        }, SPLASH_TIME);
    }
}
