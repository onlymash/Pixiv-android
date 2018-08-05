package com.example.administrator.essim.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.administrator.essim.R;
import com.example.administrator.essim.adapters.DonaterAdapter;

public class AboutDonationActivity extends AppCompatActivity {

    private static final String[] donaterList = new String[]{"@黄鹏飞", "@海洋", "@陈硕", "@花国", "@南墙", "@最萌三千", "@易霏", "@◤康◢◤鑫◢",
            "@灵零子", "@咸鱼", "@天美削弱部", "@震宇", "@俞易", "@钧", "@阿Q", "@W-two world", "@简单，就好", "@仙女走路都带风",
            "@Biu", "@海~少年", "@风舞月咏叹调子", "@可乐酱", "@随心", "@星辰沐雨", "@萌芽的狂想", "@辰许262", "@墨魇",
            " @灵玲子", "@Cation", "@白夜行er", "@此号三秒后删除"};
    private static final String[] libraries = new String[]{
            "com.roughike:bottom-bar:2.3.1",
            "com.astuetz:pagerslidingtabstrip:1.0.1",
            "de.hdodenhof:circleimageview:2.2.0",
            "jp.wasabeef:glide-transformations:2.0.1",
            "com.github.bumptech.glide:glide:3.8.0",
            "com.google.code.gson:gson:2.8.0",
            "com.squareup.okhttp3:okhttp:3.8.0",
            "org.litepal.android:core:1.3.2",
            "com.sdsmdg.tastytoast:tastytoast:0.1.1",
            "com.github.florent37:arclayout:1.0.3",
            "com.flaviofaria:kenburnsview:1.0.7",
            "com.nightonke:boommenu:2.1.1",
            "com.github.mancj:MaterialSearchBar:0.7.6",
            "com.squareup.retrofit2:retrofit:2.3.0",
            "com.squareup.retrofit2:converter-gson:2.3.0",
            "com.squareup.okhttp3:logging-interceptor:3.4.1",
            "com.hyman:flowlayout-lib:1.1.2",
            "com.github.codekidX:storage-chooser:2.0.3",
            "com.github.ybq:Android-SpinKit:1.1.0",
            "com.github.qingmei2:rximagepicker:0.4.0",
            "com.github.chrisbanes:PhotoView:2.1.3"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_donation);
        Context context = this;

        Toolbar toolbar = findViewById(R.id.toolbar_pixiv);
        toolbar.setOnClickListener(view -> finish());
        RecyclerView recyclerView = findViewById(R.id.donate_recy);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        String dataType = getIntent().getStringExtra("dataType");
        DonaterAdapter adapter = null;
        if (dataType.equals("donation")) {
            adapter = new DonaterAdapter(donaterList, context);
            toolbar.setTitle("神豪列表");
        } else if (dataType.equals("libraries")) {
            adapter = new DonaterAdapter(libraries, context);
            toolbar.setTitle("开源库列表");
        }

        recyclerView.setAdapter(adapter);
    }
}
