package com.example.administrator.essim.fragments_re;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.administrator.essim.R;
import com.example.administrator.essim.activities_re.ArticleActivity;
import com.example.administrator.essim.activities_re.IllustListActivity;
import com.example.administrator.essim.activities_re.RankActivity;

public class FragmentCard extends BaseFragment{

    private ImageView mImageView;
    private static final int[] IMAGES = new int[]{R.mipmap.rank_logo, R.mipmap.recommend_logo, R.mipmap.pivision};

    public static FragmentCard newInstance(int index) {
        Bundle args = new Bundle();
        args.putSerializable("index", index);
        FragmentCard fragment = new FragmentCard();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    void initLayout() {
        mLayoutID = R.layout.fragment_card;
    }

    @Override
    View initView(View v) {
        mImageView = v.findViewById(R.id.imageView);
        return v;
    }

    @Override
    void initData() {
        int index = (int) getArguments().getSerializable("index");
        mImageView.setOnClickListener(v -> {
            Intent intent;
            if(index == 0){
                intent = new Intent(mContext, RankActivity.class);
                mContext.startActivity(intent);
            }else if(index == 1){
                intent = new Intent(mContext, IllustListActivity.class);
                mContext.startActivity(intent);
            }else if(index == 2){
                intent = new Intent(mContext, ArticleActivity.class);
                mContext.startActivity(intent);
            }

        });
        Glide.with(mContext).load(IMAGES[index]).into(mImageView);
    }

    @Override
    void getFirstData() {

    }

    @Override
    void getNextData() {

    }
}
