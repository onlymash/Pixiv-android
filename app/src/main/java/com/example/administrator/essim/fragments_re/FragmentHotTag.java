package com.example.administrator.essim.fragments_re;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.example.administrator.essim.R;
import com.example.administrator.essim.activities.SearchResultActivity;
import com.example.administrator.essim.adapters_re.HotTagAdapter;
import com.example.administrator.essim.interf.OnItemClickListener;
import com.example.administrator.essim.network_re.Retro;
import com.example.administrator.essim.response.TrendingtagResponse;
import com.example.administrator.essim.utils.Common;
import com.example.administrator.essim.utils.DensityUtil;
import com.example.administrator.essim.utils.GridItemDecoration;
import com.example.administrator.essim.utils.PixivOperate;
import com.example.administrator.essim.utils.TagItemDecoration;
import com.example.administrator.essim.utils_re.LocalData;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentHotTag extends BaseFragment {

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;

    @Override
    void initLayout() {
        mLayoutID = R.layout.fragment_hot_tags;
    }

    @Override
    View initView(View v) {
        mRecyclerView = v.findViewById(R.id.recy_list);
        mProgressBar = v.findViewById(R.id.progress);
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 3);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(position == 0) {
                    return 3;
                }
                else {
                    return 1;
                }
            }
        });
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new TagItemDecoration(
                3, DensityUtil.dip2px(mContext, 1.0f), false));
        mRecyclerView.setHasFixedSize(true);
        return v;
    }

    @Override
    void initData() {
    }

    @Override
    void getFirstData() {
        Retro.initToken(() ->
                Retro.getAppApi().getHotTags(
                        LocalData.getToken(), "for_android")
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<TrendingtagResponse>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(TrendingtagResponse trendingtagResponse) {
                                if (trendingtagResponse != null && trendingtagResponse.getTrend_tags() != null) {
                                    List<TrendingtagResponse.TrendTagsBean> allIllusts = trendingtagResponse.getTrend_tags();
                                    HotTagAdapter adapter = new HotTagAdapter(allIllusts, mContext);
                                    adapter.setOnItemClickListener(new OnItemClickListener() {
                                        @Override
                                        public void onItemClick(@NotNull View view, int position, int viewType) {
                                            Intent intent = new Intent(mContext, SearchResultActivity.class);
                                            intent.putExtra("what is the keyword", allIllusts.get(position).getTag());
                                            startActivity(intent);
                                        }

                                        @Override
                                        public void onItemLongClick(@NotNull View view, int position) {
                                            PixivOperate.getSingleIllust(mContext, allIllusts.get(position).getIllust().getId());
                                        }
                                    });
                                    mRecyclerView.setAdapter(adapter);
                                } else {
                                    Common.showToast(getString(R.string.load_error));
                                }
                                mProgressBar.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onError(Throwable e) {
                                mProgressBar.setVisibility(View.INVISIBLE);
                                Common.showToast(getString(R.string.load_error));
                            }

                            @Override
                            public void onComplete() {

                            }
                        }));
    }

    @Override
    void getNextData() {
    }
}
