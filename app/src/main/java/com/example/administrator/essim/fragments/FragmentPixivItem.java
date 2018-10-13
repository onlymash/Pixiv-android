package com.example.administrator.essim.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.administrator.essim.R;
import com.example.administrator.essim.activities.CommentActivity;
import com.example.administrator.essim.activities.ImageDetailActivity;
import com.example.administrator.essim.activities.RelatedActivity;
import com.example.administrator.essim.activities.SearchResultActivity;
import com.example.administrator.essim.activities.UserDetailActivity;
import com.example.administrator.essim.activities.ViewPagerActivity;
import com.example.administrator.essim.download.DownloadTask;
import com.example.administrator.essim.download.SDDownloadTask;
import com.example.administrator.essim.network.AppApiPixivService;
import com.example.administrator.essim.network.RestClient;
import com.example.administrator.essim.response.IllustsBean;
import com.example.administrator.essim.response.RelatedIllust;
import com.example.administrator.essim.utils.Common;
import com.example.administrator.essim.utils.GlideUtil;
import com.example.administrator.essim.utils.LocalData;
import com.example.administrator.essim.utils.PixivOperate;
import com.sdsmdg.tastytoast.TastyToast;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.io.File;
import java.util.Objects;

import jp.wasabeef.glide.transformations.BlurTransformation;
import retrofit2.Call;


/**
 * Created by Administrator on 2018/1/18 0018.
 */

public class FragmentPixivItem extends BaseFragment implements View.OnClickListener {

    private int index;
    private String priority;
    private TextView mTextView;
    private RelatedIllust mRelatedIllust;
    private IllustsBean mIllustsBean;
    private FloatingActionButton mFloatingActionButton;

    public static FragmentPixivItem newInstance(int index) {
        Bundle args = new Bundle();
        args.putSerializable("index", index);
        FragmentPixivItem fragment = new FragmentPixivItem();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pixiv_item, container, false);
        index = (int) getArguments().getSerializable("index");
        mIllustsBean = ((ViewPagerActivity) getActivity()).getAllIllust().get(index);
        if (index == ((ViewPagerActivity) Objects.requireNonNull(getActivity())).mViewPager.getCurrentItem()) {
            setUserVisibleHint(true);
            priority = "high";
        } else {
            priority = "low";
        }
        reFreshLayout(view);
        return view;
    }

    private void reFreshLayout(View view) {
        ImageView imageView = view.findViewById(R.id.item_background_img);
        ImageView imageView2 = view.findViewById(R.id.detail_img);
        ImageView imageView3 = view.findViewById(R.id.author_head);
        ViewGroup.LayoutParams params = imageView2.getLayoutParams();
        params.height = (((getResources().getDisplayMetrics().widthPixels - getResources().getDimensionPixelSize(R.dimen.thirty_two_dp)) *
                mIllustsBean.getHeight()) / mIllustsBean.getWidth());
        imageView2.setLayoutParams(params);
        imageView2.setOnClickListener(this);
        imageView3.setOnClickListener(this);
        Glide.get(mContext).clearMemory();
        Glide.with(getContext()).load(new GlideUtil().getMediumImageUrl(mIllustsBean))
                .bitmapTransform(new BlurTransformation(mContext, 20, 2))
                .into(imageView);
        ProgressBar progressBar = view.findViewById(R.id.try_login);
        progressBar.setIndeterminateDrawable(Common.getLoaderAnimation(mContext));
        if (LocalData.getLocalDataSet().getBoolean("is_origin_pic", true)) {
            Glide.with(mContext).load(new GlideUtil().getLargeImageUrl(mIllustsBean, 0))
                    .priority(priority.equals("high") ? Priority.IMMEDIATE : Priority.NORMAL)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(new GlideDrawableImageViewTarget(imageView2) {
                        @Override
                        public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                            progressBar.setVisibility(View.INVISIBLE);
                            super.onResourceReady(drawable, anim);
                        }
                    });
        } else {
            Glide.with(mContext).load(new GlideUtil().getMediumImageUrl(mIllustsBean))
                    .priority(priority.equals("high") ? Priority.IMMEDIATE : Priority.NORMAL)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(new GlideDrawableImageViewTarget(imageView2) {
                        @Override
                        public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                            progressBar.setVisibility(View.INVISIBLE);
                            super.onResourceReady(drawable, anim);
                        }
                    });
        }
        Glide.with(mContext).load(new GlideUtil().getHead(mIllustsBean.getUser().getProfile_image_urls().getMedium()))
                .into(imageView3);
        TextView textView = view.findViewById(R.id.detail_author);
        mTextView = view.findViewById(R.id.is_following);
        mTextView.setOnClickListener(this);
        TextView textView2 = view.findViewById(R.id.detail_img_size);
        TextView textView3 = view.findViewById(R.id.detail_create_time);
        TextView textView4 = view.findViewById(R.id.viewed);
        TextView textView5 = view.findViewById(R.id.liked);
        TextView textView6 = view.findViewById(R.id.illust_id);
        TextView textView7 = view.findViewById(R.id.author_id);
        TextView textView8 = view.findViewById(R.id.all_item_size);
        TextView textView9 = view.findViewById(R.id.description);
        textView9.setOnLongClickListener(v -> {
            Common.copyMessage(mContext, textView9.getText().toString());
            return true;
        });
        TagFlowLayout mTagGroup = view.findViewById(R.id.tag_group);
        String allTag[] = new String[mIllustsBean.getTags().size()];
        for (int i = 0; i < mIllustsBean.getTags().size(); i++) {
            allTag[i] = mIllustsBean.getTags().get(i).getName();
        }
        mTagGroup.setAdapter(new TagAdapter<String>(allTag) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.tag_textview,
                        mTagGroup, false);
                tv.setText(s);
                tv.setOnClickListener(v -> {
                    Intent intent = new Intent(mContext, SearchResultActivity.class);
                    intent.putExtra("what is the keyword", allTag[position]);
                    mContext.startActivity(intent);
                });
                tv.setOnLongClickListener(v -> {
                    Common.copyMessage(mContext, s);
                    return true;
                });
                return tv;
            }
        });
        mFloatingActionButton = view.findViewById(R.id.fab_like);
        textView.setText(mIllustsBean.getUser().getName());
        textView.setOnClickListener(this);
        mTextView.setText(mIllustsBean.getUser().isIs_followed() ? "取消关注" : "+关注");
        textView2.setText(getString(R.string.string_full_size, mIllustsBean.getWidth(),
                mIllustsBean.getHeight()));
        textView3.setText(getString(R.string.string_create_time, mIllustsBean.getCreate_date().
                substring(0, mIllustsBean.getCreate_date().length() - 9)));
        textView4.setText(mIllustsBean.getTotal_view() >= 1000 ? getString(R.string.string_viewd,
                String.valueOf(mIllustsBean.getTotal_view() / 1000)) :
                String.valueOf(mIllustsBean.getTotal_view()));
        textView5.setText(mIllustsBean.getTotal_bookmarks() >= 1000 ? getString(R.string.string_viewd,
                String.valueOf(mIllustsBean.getTotal_bookmarks() / 1000)) :
                String.valueOf(mIllustsBean.getTotal_bookmarks()));
        textView6.setText(getString(R.string.illust_id, String.valueOf(mIllustsBean.getId())));
        textView7.setText(getString(R.string.author_id, String.valueOf(mIllustsBean.getUser().getId())));
        if (mIllustsBean.getPage_count() > 1) {
            textView8.setText(String.format("%sP", String.valueOf(mIllustsBean.getPage_count())));
        } else {
            textView8.setVisibility(View.INVISIBLE);
        }
        if (mIllustsBean.getCaption().length() > 0) {
            if (textView9.getVisibility() == View.GONE) {
                textView9.setVisibility(View.VISIBLE);
            }
            textView9.setText(Html.fromHtml(mIllustsBean.getCaption()));
        } else {

            if (textView9.getVisibility() == View.VISIBLE) {
                textView9.setVisibility(View.GONE);
            }
        }
        mFloatingActionButton.setImageResource(mIllustsBean.isIs_bookmarked() ?
                R.drawable.ic_favorite_white_24dp : R.drawable.no_favor);
        mFloatingActionButton.setOnClickListener(this);
        mFloatingActionButton.setOnLongClickListener(view1 -> {
            if (!mIllustsBean.isIs_bookmarked()) {
                new FragmentDialog(mContext, mFloatingActionButton, mIllustsBean).showDialog();
            }
            return true;
        });
        CardView cardView = view.findViewById(R.id.card_left);
        cardView.setOnClickListener(this);
        CardView cardView2 = view.findViewById(R.id.card_right);
        cardView2.setOnClickListener(this);
        getRelatedIllust(view);
    }

    private void getRelatedIllust(View view) {
        ImageView imageView = view.findViewById(R.id.related_one);
        ViewGroup.LayoutParams params2 = imageView.getLayoutParams();
        params2.width = (getResources().getDisplayMetrics().widthPixels - getResources().getDimensionPixelSize(R.dimen.thirty_two_dp)) / 3;
        params2.height = params2.width * 6 / 5;
        Common.showLog(params2.width);
        imageView.setLayoutParams(params2);
        ImageView imageView2 = view.findViewById(R.id.related_two);
        imageView2.setLayoutParams(params2);
        ImageView imageView3 = view.findViewById(R.id.related_three);
        imageView3.setLayoutParams(params2);
        CardView cardView = view.findViewById(R.id.get_related_illust);
        cardView.setOnClickListener(this);
        TextView textView = view.findViewById(R.id.text_get_related);
        TextView textView2 = view.findViewById(R.id.text_related);
        textView.setOnClickListener(this);
        Call<RelatedIllust> call = new RestClient()
                .getRetrofit_AppAPI()
                .create(AppApiPixivService.class)
                .getRelatedIllust(LocalData.getToken(), mIllustsBean.getId());
        call.enqueue(new retrofit2.Callback<RelatedIllust>() {
            @Override
            public void onResponse(Call<RelatedIllust> call, retrofit2.Response<RelatedIllust> response) {
                mRelatedIllust = response.body();
                if (mRelatedIllust != null && mRelatedIllust.illusts.size() >= 3 && getView() != null) {
                    Glide.with(mContext).load(new GlideUtil().getMediumImageUrl(mRelatedIllust.illusts.get(0)))
                            .priority(Priority.LOW)
                            .into(imageView);
                    Glide.with(mContext).load(new GlideUtil().getMediumImageUrl(mRelatedIllust.illusts.get(1)))
                            .priority(Priority.LOW)
                            .into(imageView2);
                    Glide.with(mContext).load(new GlideUtil().getMediumImageUrl(mRelatedIllust.illusts.get(2)))
                            .priority(Priority.LOW)
                            .into(imageView3);
                } else {
                    cardView.setVisibility(View.GONE);
                    textView.setVisibility(View.INVISIBLE);
                    textView2.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<RelatedIllust> call, Throwable throwable) {
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.detail_img:
                intent = new Intent(mContext, ImageDetailActivity.class);
                intent.putExtra("illust", mIllustsBean);
                mContext.startActivity(intent);
                break;
            case R.id.detail_author:
            case R.id.author_head:
                intent = new Intent(mContext, UserDetailActivity.class);
                intent.putExtra("user id", mIllustsBean.getUser().getId());
                mContext.startActivity(intent);
                break;
            case R.id.card_left:
                File realFile = Common.generatePictureFile(mContext, mIllustsBean, 0,
                        LocalData.getLocalDataSet().getInt("file_name_style", 0), 1);
                if (realFile.length() != 0) {
                    TastyToast.makeText(mContext, "该文件已存在~",
                            TastyToast.LENGTH_SHORT, TastyToast.CONFUSING).show();
                } else {
                    //只有一张图的情况下，从meta_single_page获取原图链接
                    if (mIllustsBean.getPage_count() == 1) {
                        if (LocalData.getDownloadPath().contains("emulated")) {
                            //下载至内置SD存储介质，使用传统文件模式;
                            new DownloadTask(realFile, mContext, mIllustsBean).execute(mIllustsBean
                                    .getMeta_single_page().getOriginal_image_url());
                        } else {//下载至可插拔SD存储介质，使用SAF 框架，DocumentFile文件模式;
                            new SDDownloadTask(realFile, mContext, mIllustsBean,
                                    LocalData.getLocalDataSet()).execute(mIllustsBean.getMeta_single_page()
                                    .getOriginal_image_url());
                        }
                    } else { //有多图的情况下，从meta_pages获取原图链接
                        if (LocalData.getToken().contains("emulated")) {
                            //下载至内置SD存储介质，使用传统文件模式;
                            new DownloadTask(realFile, mContext, mIllustsBean).execute(mIllustsBean
                                    .getMeta_pages().get(0).getImage_urlsX().getOriginal());
                        } else {//下载至可插拔SD存储介质，使用SAF 框架，DocumentFile文件模式;
                            new SDDownloadTask(realFile, mContext, mIllustsBean, LocalData.getLocalDataSet())
                                    .execute(mIllustsBean.getMeta_pages().get(0).getImage_urlsX().getOriginal());
                        }
                    }
                }
                break;
            case R.id.is_following:
                if (mIllustsBean.getUser().isIs_followed()) {
                    Common.postUnFollowUser(LocalData.getToken(), mIllustsBean.getUser().getId(), mTextView);
                    mIllustsBean.getUser().setIs_followed(false);
                    mTextView.setText("+关注");
                } else {
                    Common.postFollowUser(LocalData.getToken(),
                            mIllustsBean.getUser().getId(), mTextView, "public");
                    mIllustsBean.getUser().setIs_followed(true);
                    mTextView.setText("取消关注");
                }
                break;
            case R.id.card_right:
                intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra("title", mIllustsBean.getTitle());
                intent.putExtra("id", mIllustsBean.getId());
                mContext.startActivity(intent);
                break;
            case R.id.fab_like:
                if (mIllustsBean.isIs_bookmarked()) {
                    mFloatingActionButton.setImageResource(R.drawable.no_favor);
                    mFloatingActionButton.startAnimation(Common.getAnimation());
                    mIllustsBean.setIs_bookmarked(false);
                    PixivOperate.postUnstarIllust(mIllustsBean.getId(), mContext);
                } else {
                    mFloatingActionButton.setImageResource(R.drawable.ic_favorite_white_24dp);
                    mFloatingActionButton.startAnimation(Common.getAnimation());
                    mIllustsBean.setIs_bookmarked(true);
                    PixivOperate.postStarIllust(mIllustsBean.getId(), mContext, "public");
                }
                break;
            case R.id.get_related_illust:
            case R.id.text_get_related:
                if (mRelatedIllust != null && mRelatedIllust.illusts.size() >= 3) {
                    intent = new Intent(mContext, RelatedActivity.class);
                    intent.putExtra("illust set", mRelatedIllust);
                    intent.putExtra("illust title", mIllustsBean.getTitle());
                    mContext.startActivity(intent);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (getActivity() != null) {
                ((ViewPagerActivity) getActivity()).changeTitle();
                LocalData.saveViewHistory(mIllustsBean);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}