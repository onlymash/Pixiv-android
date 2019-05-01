package com.example.administrator.essim.activities_re;

import android.content.Intent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.administrator.essim.R;
import com.example.administrator.essim.adapters.CommentAdapter;
import com.example.administrator.essim.interf.OnItemClickListener;
import com.example.administrator.essim.network_re.Retro;
import com.example.administrator.essim.response.IllustCommentsResponse;
import com.example.administrator.essim.utils.Common;
import com.example.administrator.essim.utils_re.LocalData;
import com.scwang.smartrefresh.header.DeliveryHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class CommentActivity extends BaseActivity{

    private RecyclerView mRecyclerView;
    private RefreshLayout mRefreshLayout;
    private ProgressBar mProgressBar;
    private CommentAdapter mAdapter;
    private ImageView clearHint, sendComment, noData;
    private EditText textInput;
    private int illustID, parentCommentID;
    private List<IllustCommentsResponse.CommentsBean> allIllusts = new ArrayList<>();
    private String nextUrl = null, title;
    private Toolbar mToolbar;

    @Override
    void initLayout() {
        mLayoutID = R.layout.activity_comment;
    }

    @Override
    void initView() {
        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(v -> finish());
        mRecyclerView = findViewById(R.id.recy_list);
        mProgressBar = findViewById(R.id.progress);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRefreshLayout = findViewById(R.id.refreshLayout);
        clearHint = findViewById(R.id.clear_hint);
        noData = findViewById(R.id.no_data);
        sendComment = findViewById(R.id.sendComment);
        sendComment.setOnClickListener(v -> postComment());
        textInput = findViewById(R.id.mEditText);
        clearHint.setOnClickListener(v -> {
            if(textInput.getText().toString().length() > 0){
                textInput.setText("");
            }else {
                parentCommentID = 0;
                textInput.setHint("留下你的评论吧~");
            }
        });
        mRefreshLayout.setRefreshHeader(new DeliveryHeader(mContext));
        mRefreshLayout.setOnLoadMoreListener(layout -> getNextData());
        mRefreshLayout.setOnRefreshListener(layout -> getFirstData());
    }

    @Override
    void initData() {
        illustID = getIntent().getIntExtra("id", 0);
        title = getIntent().getStringExtra("title");
        mToolbar.setTitle(title + "的评论");
        getFirstData();
    }

    /**
     * 发布评论
     */
    private void postComment(){
        if(textInput.getText().toString().length() == 0){
            Common.showToast("评论不能为空");
        }else {
            Retro.initToken(() -> Retro.getAppApi().postComment(
                    LocalData.getToken(), illustID, textInput.getText().toString(), parentCommentID)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ResponseBody>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(ResponseBody responseBody) {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Common.showToast("评论失败，请检查网络连接");
                        }

                        @Override
                        public void onComplete() {
                            Common.showToast("评论成功~");
                            parentCommentID = 0;
                            textInput.setText("");
                            textInput.setHint("留下你的评论吧~");
                            Common.hideKeyboard(mActivity);
                            getFirstData();
                        }
                    }));
        }
    }

    @Override
    void getFirstData() {
        Retro.initToken(() ->
                Retro.getAppApi().getComment(
                LocalData.getToken(), illustID)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<IllustCommentsResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(IllustCommentsResponse illustCommentsResponse) {
                        if (illustCommentsResponse != null && illustCommentsResponse.getComments().size() > 0) {
                            allIllusts.clear();
                            allIllusts = illustCommentsResponse.getComments();
                            nextUrl = illustCommentsResponse.getNext_url();
                            mAdapter = new CommentAdapter(allIllusts, mContext);
                            mAdapter.setOnItemClickListener(new OnItemClickListener() {
                                @Override
                                public void onItemClick(@NotNull View view, int position, int viewType) {
                                    if(viewType == 1) {
                                        Intent intent = new Intent(mContext, UserDetailActivity.class);
                                        intent.putExtra("user id", allIllusts.get(position).getUser().getId());
                                        startActivity(intent);
                                    }else if(viewType == 0){
                                        if(textInput.getText().toString().length() == 0) {
                                            parentCommentID = allIllusts.get(position).getId();
                                            textInput.setHint(String.format("回复@%s", allIllusts.get(position).getUser().getName()));
                                        }
                                    }
                                }

                                @Override
                                public void onItemLongClick(@NotNull View view, int position) {

                                }
                            });
                            mRefreshLayout.getLayout().setVisibility(View.VISIBLE);
                            noData.setVisibility(View.INVISIBLE);
                            mRecyclerView.setAdapter(mAdapter);
                            mRefreshLayout.finishRefresh(true);
                        } else {
                            mRefreshLayout.finishRefresh(false);
                            mRefreshLayout.getLayout().setVisibility(View.INVISIBLE);
                            noData.setVisibility(View.VISIBLE);
                        }
                        mProgressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mRefreshLayout.finishRefresh(false);
                        mProgressBar.setVisibility(View.INVISIBLE);
                        mRefreshLayout.getLayout().setVisibility(View.INVISIBLE);
                        noData.setVisibility(View.VISIBLE);
                        Common.showToast(getString(R.string.load_error));
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    @Override
    void getNextData() {
        if (nextUrl != null) {
            Retro.initToken(() -> Retro.getAppApi().getNextComment(LocalData.getToken(), nextUrl)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<IllustCommentsResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(IllustCommentsResponse illustCommentsResponse) {
                            if (illustCommentsResponse != null && illustCommentsResponse.getComments() != null) {
                                mRefreshLayout.finishLoadMore(true);
                                allIllusts.addAll(illustCommentsResponse.getComments());
                                nextUrl = illustCommentsResponse.getNext_url();
                                mAdapter.notifyItemRangeChanged(allIllusts.size() -
                                        illustCommentsResponse.getComments().size(),
                                        illustCommentsResponse.getComments().size());
                            } else {
                                mRefreshLayout.finishLoadMore(false);
                                Common.showToast(getString(R.string.load_error));
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            mRefreshLayout.finishLoadMore(false);
                            Common.showToast(getString(R.string.load_error));
                        }

                        @Override
                        public void onComplete() {

                        }
                    }));
        } else {
            mRefreshLayout.finishLoadMore(true);
            Common.showToast(getString(R.string.no_more_data));
        }
    }
}
