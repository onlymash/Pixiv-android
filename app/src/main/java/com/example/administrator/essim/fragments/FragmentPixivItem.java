package com.example.administrator.essim.fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.administrator.essim.glide.GlideApp;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.example.administrator.essim.R;
import com.example.administrator.essim.activities_re.CommentActivity;
import com.example.administrator.essim.activities_re.ImageDetailActivity;
import com.example.administrator.essim.activities_re.RelatedActivity;
import com.example.administrator.essim.activities.SearchResultActivity;
import com.example.administrator.essim.activities.ViewPagerActivity;
import com.example.administrator.essim.activities_re.UserDetailActivity;
import com.example.administrator.essim.network_re.Retro;
import com.example.administrator.essim.response_re.IllustListResponse;
import com.example.administrator.essim.response_re.IllustsBean;
import com.example.administrator.essim.utils.Common;
import com.example.administrator.essim.utils.PixivOperate;
import com.example.administrator.essim.utils_re.GlideUtil;
import com.example.administrator.essim.utils_re.LocalData;
import com.example.administrator.essim.utils_re.FileDownload;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.Objects;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import jp.wasabeef.glide.transformations.BlurTransformation;


/**
 * Created by Administrator on 2018/1/18 0018.
 */

public class FragmentPixivItem extends BaseFragment implements View.OnClickListener {

    private int index;
    private String priority;
    private TextView mTextView;
    private IllustListResponse mRelatedIllust;
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
        GlideApp.with(getContext()).load(GlideUtil.getSquare(mIllustsBean))
                .transform(new BlurTransformation(mContext, 20, 2))
                .into(imageView);
        ProgressBar progressBar = view.findViewById(R.id.try_login);
        progressBar.setIndeterminateDrawable(Common.getLoaderAnimation(mContext));
        if (LocalData.getLocalDataSet().getBoolean("is_origin_pic", true)) {
            GlideApp.with(mContext).load(GlideUtil.getLargeImage(mIllustsBean))
                    .priority(priority.equals("high") ? Priority.IMMEDIATE : Priority.NORMAL)
                    .addListener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            progressBar.setVisibility(View.INVISIBLE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            progressBar.setVisibility(View.INVISIBLE);
                            return false;
                        }
                    })
                    .into(imageView2);
        } else {
            GlideApp.with(mContext).load(GlideUtil.getMediumImg(mIllustsBean))
                    .priority(priority.equals("high") ? Priority.IMMEDIATE : Priority.NORMAL)
                    .addListener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            progressBar.setVisibility(View.INVISIBLE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            progressBar.setVisibility(View.INVISIBLE);
                            return false;
                        }
                    })
                    .into(imageView2);
        }
        Glide.with(mContext).load(GlideUtil.getUserHead(mIllustsBean.getUser()))
                .into(imageView3);
        TextView textView = view.findViewById(R.id.detail_author);
        mTextView = view.findViewById(R.id.is_following);
        mTextView.setOnClickListener(this);
        TextView textView2 = view.findViewById(R.id.detail_img_size);
        TextView textView3 = view.findViewById(R.id.detail_create_time);
        TextView textView4 = view.findViewById(R.id.viewed);
        TextView textView5 = view.findViewById(R.id.liked);
        TextView textView6 = view.findViewById(R.id.illust_id);
        textView6.setOnClickListener(this);
        TextView textView7 = view.findViewById(R.id.author_id);
        textView7.setOnClickListener(this);
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
        Retro.initToken(() ->
                Retro.getAppApi().getRelated(LocalData.getToken(), "for_android", mIllustsBean.getId())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<IllustListResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(IllustListResponse illustListResponse) {
                        if(illustListResponse != null && illustListResponse.getIllusts().size() >= 3){
                            mRelatedIllust = illustListResponse;
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
                            cardView.setOnClickListener(FragmentPixivItem.this);
                            TextView textView = view.findViewById(R.id.text_get_related);
                            textView.setOnClickListener(FragmentPixivItem.this);

                            Glide.with(mContext).load(GlideUtil.getMediumImg(mRelatedIllust.getIllusts().get(0)))
                                    .priority(Priority.LOW)
                                    .into(imageView);
                            Glide.with(mContext).load(GlideUtil.getMediumImg(mRelatedIllust.getIllusts().get(1)))
                                    .priority(Priority.LOW)
                                    .into(imageView2);
                            Glide.with(mContext).load(GlideUtil.getMediumImg(mRelatedIllust.getIllusts().get(2)))
                                    .priority(Priority.LOW)
                                    .into(imageView3);
                        }else {
                            CardView cardView = view.findViewById(R.id.get_related_illust);
                            TextView textView = view.findViewById(R.id.text_get_related);
                            TextView textView2 = view.findViewById(R.id.text_related);
                            cardView.setVisibility(View.GONE);
                            textView.setVisibility(View.INVISIBLE);
                            textView2.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        CardView cardView = view.findViewById(R.id.get_related_illust);
                        TextView textView = view.findViewById(R.id.text_get_related);
                        TextView textView2 = view.findViewById(R.id.text_related);
                        cardView.setVisibility(View.GONE);
                        textView.setVisibility(View.INVISIBLE);
                        textView2.setVisibility(View.INVISIBLE);

                    }

                    @Override
                    public void onComplete() {

                    }
                }));
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
                FileDownload.downloadIllust(mIllustsBean);
                break;
            case R.id.is_following:
                if (mIllustsBean.getUser().isIs_followed()) {
                    Common.postUnFollowUser(LocalData.getToken(), mIllustsBean.getUser().getId(), mTextView);
                    mIllustsBean.getUser().setIs_followed(false);
                    mTextView.setText("+关注");
                } else {
                    PixivOperate.postFollowUser(mIllustsBean.getUser().getId(), "public");
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
                if (mRelatedIllust != null && mRelatedIllust.getIllusts().size() >= 3) {
                    intent = new Intent(mContext, RelatedActivity.class);
                    intent.putExtra("illust set", mRelatedIllust);
                    intent.putExtra("illust title", mIllustsBean.getTitle());
                    mContext.startActivity(intent);
                }
                break;

            case R.id.illust_id:
                Common.copyMessage(mContext, String.valueOf(mIllustsBean.getId()));
                break;

            case R.id.author_id:
                Common.copyMessage(mContext, String.valueOf(mIllustsBean.getUser().getId()));
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