package com.example.administrator.essim.fragments_re;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.administrator.essim.R;
import com.example.administrator.essim.activities_re.ImageDetailActivity;
import com.example.administrator.essim.glide.GlideApp;
import com.example.administrator.essim.response_re.IllustsBean;
import com.example.administrator.essim.utils_re.GlideUtil;
import com.github.ybq.android.spinkit.style.Wave;

public class FragmentImageDetail extends BaseFragment{

    private int index;
    private IllustsBean mIllustsBean;
    private ProgressBar mProgressBar;
    private ImageView mImageView;

    public static FragmentImageDetail newInstance(int index) {
        Bundle args = new Bundle();
        args.putSerializable("index", index);
        FragmentImageDetail fragment = new FragmentImageDetail();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    void initLayout() {
        mLayoutID = R.layout.fragment_image_detail;
    }

    @Override
    View initView(View v) {
        mProgressBar = v.findViewById(R.id.mProgressbar);
        mImageView = v.findViewById(R.id.originalImage);
        mProgressBar.setIndeterminateDrawable(new Wave());
        return v;
    }

    @Override
    void initData() {
        mIllustsBean = ((ImageDetailActivity) getActivity()).getIllustsBean();
        index = (int) getArguments().getSerializable("index");
        GlideApp.with(mContext).load(GlideUtil.getLargeImage(mIllustsBean, index))
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        return false;
                    }
                })
                .into(mImageView);
    }

    @Override
    void getFirstData() {

    }

    @Override
    void getNextData() {

    }
}
