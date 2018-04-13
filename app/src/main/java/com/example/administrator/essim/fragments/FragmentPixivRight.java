package com.example.administrator.essim.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.administrator.essim.R;
import com.example.administrator.essim.activities.ViewPagerActivity;
import com.example.administrator.essim.adapters.HotTagAdapter;
import com.example.administrator.essim.adapters.PixivAdapter;
import com.example.administrator.essim.models.PixivRankItem;
import com.example.administrator.essim.models.Tag;
import com.example.administrator.essim.utils.Common;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * Created by Administrator on 2018/1/15 0015.
 */

public class FragmentPixivRight extends BaseFragment {

    private Button mButton;
    private List<Tag> mTagList = new ArrayList<>();
    private HotTagAdapter mHotTagAdapter;
    private RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pixiv_right, container, false);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mContext);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView = view.findViewById(R.id.tag_list);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        getData("https://api.imjad.cn/pixiv/v1/?type=tags");
        return view;
    }


    private void getData(String address) {
        Common.sendOkhttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TastyToast.makeText(mContext, "数据加载失败", TastyToast.LENGTH_SHORT, TastyToast.CONFUSING).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Gson gson = new Gson();
                final List<Tag> booksInfo = gson.fromJson(responseData, new TypeToken<List<Tag>>() {}.getType());
                mTagList = booksInfo.subList(0, 60);
                mHotTagAdapter = new HotTagAdapter(mTagList, getContext());
                mHotTagAdapter.setOnItemClickLitener(new HotTagAdapter.OnItemClickLitener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemLongClick(View view, int position)
                    {
                        ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData mClipData = ClipData.newPlainText("Label", booksInfo.get(position).getName());
                        cm.setPrimaryClip(mClipData);
                        Toast.makeText(mContext, booksInfo.get(position).getName()+ " 已复制到剪切板~",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.setAdapter(mHotTagAdapter);
                    }
                });
            }
        });
    }
}