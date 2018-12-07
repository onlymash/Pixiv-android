package com.example.administrator.essim.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;

import com.example.administrator.essim.activities.MainActivity;
import com.example.administrator.essim.activities.SearchResultActivity;
import com.example.administrator.essim.network.RestClient;
import com.example.administrator.essim.response.AnnounceResponse;
import com.example.administrator.essim.response.OnlineResponse;
import com.example.administrator.essim.utils.Common;
import com.example.administrator.essim.utils.LocalData;
import com.example.administrator.essim.views.MainView;
import com.sdsmdg.tastytoast.TastyToast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPresenter implements BasePresenter {

    private Context mContext;
    private MainView mView;
    private Handler timeHandler = new Handler();

    public MainPresenter(MainView view) {
        mView = view;
        mContext = mView.getSelfContext();
    }

    @Override
    public void start() {
        timeHandler.post(runnable);
        getR18Pwd();
    }

    @Override
    public void end() {
        mView = null;
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Common.showLog("正在获取在线人数~");
            getOnlineCount();
            timeHandler.postDelayed(this, 60 * 1000);
        }
    };


    private void getOnlineCount() {
        Call<OnlineResponse> call = new RestClient().getOnlineApi().getOnlineCount();
        call.enqueue(new Callback<OnlineResponse>() {
            @Override
            public void onResponse(Call<OnlineResponse> call, Response<OnlineResponse> response) {
                if (response.body() != null && response.body().getValue() != null) {
                    mView.setNowPressure("在线人数：" + response.body().getValue());
                }

            }

            @Override
            public void onFailure(Call<OnlineResponse> call, Throwable t) {

            }
        });
    }

    private void getR18Pwd() {
        Call<AnnounceResponse> call = new RestClient().getOnlineApi().getPassword();
        call.enqueue(new Callback<AnnounceResponse>() {
            @Override
            public void onResponse(Call<AnnounceResponse> call, Response<AnnounceResponse> response) {
                if (response.body() != null && response.body().getR18() != null) {
                    LocalData.setRealR18Pwd(response.body().getR18());
                }

            }

            @Override
            public void onFailure(Call<AnnounceResponse> call, Throwable t) {

            }
        });
    }
}
