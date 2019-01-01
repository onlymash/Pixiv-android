package com.example.administrator.essim.activities_re;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.essim.R;
import com.example.administrator.essim.utils.Common;

public class FreeActivity extends BaseActivity {

    private TextView mTextView, mTextView2, mTextView3;

    @Override
    void initLayout() {
        mLayoutID = R.layout.activity_free;
    }

    @Override
    void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(view -> finish());
        mTextView = findViewById(R.id.key);

        mTextView.setOnLongClickListener(view -> {
            Common.copyMessage(mContext, mTextView.getText().toString());
            return true;
        });

        /*mTextView2 = findViewById(R.id.help);

        mTextView2.setOnLongClickListener(view -> {
            Common.copyMessage(mContext, mTextView2.getText().toString());
            return true;
        });*/

        mTextView3 = findViewById(R.id.join_app_qun);

        mTextView3.setOnClickListener(view -> joinQQGroup("0UeLWx5RwM7dEr3pmhInTJhqcdetXZI7"));
    }

    /****************
     *
     * 发起添加群流程。群号：大闹天宫拍摄组(882869310) 的 key 为： 0UeLWx5RwM7dEr3pmhInTJhqcdetXZI7
     * 调用 joinQQGroup(0UeLWx5RwM7dEr3pmhInTJhqcdetXZI7) 即可发起手Q客户端申请加群 大闹天宫拍摄组(882869310)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     ******************/
    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }


    @Override
    void initData() {

    }

    @Override
    void getFirstData() {

    }

    @Override
    void getNextData() {

    }
}
