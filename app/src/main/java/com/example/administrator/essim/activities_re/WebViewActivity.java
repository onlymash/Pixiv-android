package com.example.administrator.essim.activities_re;

import android.content.Intent;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.example.administrator.essim.R;
import com.example.administrator.essim.activities.ViewPagerActivity;
import com.example.administrator.essim.network_re.Retro;
import com.example.administrator.essim.response.Reference;
import com.example.administrator.essim.response_re.SingleIllustResponse;
import com.example.administrator.essim.utils.Common;
import com.example.administrator.essim.utils.PixivOperate;
import com.example.administrator.essim.utils_re.LocalData;
import com.just.agentweb.AgentWeb;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class WebViewActivity extends BaseActivity {

    private String url;
    private AgentWeb mAgentWeb;
    private RelativeLayout mRelativeLayout;
    private static final String USER_URL = "https://www.pixiv.net/member.php?id=";
    private static final String ILLUST_URL = "https://www.pixiv.net/member_illust.php?mode=medium&illust_id=";

    @Override
    void initLayout() {
        mLayoutID = R.layout.web_activity;
    }

    @Override
    protected void initView() {
        url = getIntent().getStringExtra("article url");
        mRelativeLayout = findViewById(R.id.parent);
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(mRelativeLayout, new RelativeLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        Common.showLog(url);
                        if (url.contains(ILLUST_URL)) {
                            PixivOperate.getSingleIllust(mContext, Integer.valueOf(url.substring(ILLUST_URL.length())));
                            return true;
                        } else if (url.contains(USER_URL)) {
                            Intent intent = new Intent(mContext, UserDetailActivity.class);
                            intent.putExtra("user id", Integer.valueOf(USER_URL.length()));
                            startActivity(intent);
                            return true;
                        } else {
                            return false;
                        }

                    }

                })
                .createAgentWeb()
                .ready()
                .go(url);
    }

    @Override
    void initData() {

    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (mAgentWeb.handleKeyEvent(keyCode, event)) {
                return true;
            }
            return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);

    }

    @Override
    void getFirstData() {

    }

    @Override
    void getNextData() {

    }

    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();

    }

    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        mAgentWeb.getWebLifeCycle().onDestroy();
        super.onDestroy();
    }
}
