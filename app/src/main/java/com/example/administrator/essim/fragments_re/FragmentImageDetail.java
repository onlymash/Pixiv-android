package com.example.administrator.essim.fragments_re;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.administrator.essim.R;
import com.example.administrator.essim.activities_re.ImageDetailActivity;
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
        Glide.with(mContext).load(GlideUtil.getLargeImage(mIllustsBean, index))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(new GlideDrawableImageViewTarget(mImageView) {
                    @Override
                    public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        super.onResourceReady(drawable, anim);
                    }
                });
    }

    @Override
    void getFirstData() {

    }

    @Override
    void getNextData() {

    }
}
