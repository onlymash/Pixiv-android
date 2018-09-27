package com.example.administrator.essim.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.example.administrator.essim.R;
import com.example.administrator.essim.network.HttpDns;
import com.example.administrator.essim.utils.Common;
import com.example.administrator.essim.utils.DnsData;
import com.sdsmdg.tastytoast.TastyToast;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class DnsEditActivity extends BaseActivity {

    private Context mContext;
    private Switch mSwitch;
    private Button mButton, mButton2;
    private LinearLayout mLinearLayout;
    private List<EditText> mEditTextList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dns_edit);

        mContext = this;
        initView();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar_pixiv);
        toolbar.setNavigationOnClickListener(view -> finish());
        mLinearLayout = findViewById(R.id.all_dns);
        mSwitch = findViewById(R.id.switch_bar);
        mButton = findViewById(R.id.feed_back);
        mButton2 = findViewById(R.id.save_edit);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mSwitch.isChecked()){
                    mSwitch.setChecked(true);
                }
                showLocalDnsSet();
                TastyToast.makeText(mContext, "恢复成功！", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
            }
        });
        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mSwitch.isChecked()){
                    TastyToast.makeText(mContext, "不能更改默认DNS", TastyToast.LENGTH_SHORT, TastyToast.CONFUSING).show();
                }else {
                   saveLocalDns();
                }
            }
        });
        showDnsView();
        if(Common.getLocalDataSet().getBoolean("inner_dns", true))
        {
            changeEditState(false);
        }
        mSwitch.setChecked(Common.getLocalDataSet().getBoolean("inner_dns", true));
        mSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            SharedPreferences.Editor editor = Common.getLocalDataSet().edit();
            editor.putBoolean("inner_dns", b);
            editor.apply();
            if(b){
                showLocalDnsSet();
            }
            changeEditState(!b);
        });
    }

    private void showDnsView(){
        mLinearLayout.removeAllViews();
        mEditTextList.clear();
        for (int i = 0; i < HttpDns.getInstance().newDns.size(); i++) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.dns_item, null);
            EditText editText = view.findViewById(R.id.dns_content);
            editText.setEnabled(false);
            mEditTextList.add(editText);
            editText.setText(String.valueOf(HttpDns.getInstance().newDns.get(i).getHostAddress()));
            mLinearLayout.addView(view);
        }
    }

    private void showLocalDnsSet()
    {
        HttpDns.reformatLocalDns();
        showDnsView();
        for (int i = 0; i < mEditTextList.size(); i++) {
            mEditTextList.get(i).setText(String.valueOf(HttpDns.getInstance().newDns.get(i).getHostAddress()));
        }
    }

    private void changeEditState(boolean b)
    {
        for (int i = 0; i < mEditTextList.size(); i++) {
            mEditTextList.get(i).setEnabled(b);
        }
    }

    private void saveLocalDns(){
        DnsData.clearDns(mContext);
        HttpDns.getInstance().newDns.clear();
        for (int i = 0; i < mEditTextList.size(); i++) {
            if(mEditTextList.get(i).getText().toString().length() > 0){
                DnsData.saveDns(mContext, mEditTextList.get(i).getText().toString());
                try {
                    HttpDns.getInstance().newDns.add(InetAddress.getByName(mEditTextList.get(i).getText().toString()));
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }
        TastyToast.makeText(mContext, "保存成功！", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
    }
}
