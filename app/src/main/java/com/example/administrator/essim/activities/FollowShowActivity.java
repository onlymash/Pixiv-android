package com.example.administrator.essim.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.essim.R;
import com.example.administrator.essim.adapters.UserFollowAdapter;
import com.example.administrator.essim.interf.OnItemClickListener;
import com.example.administrator.essim.network.AppApiPixivService;
import com.example.administrator.essim.network.RestClient;
import com.example.administrator.essim.response.SearchUserResponse;
import com.example.administrator.essim.utils.Common;
import com.example.administrator.essim.utils.DensityUtil;
import com.example.administrator.essim.utils.LinearItemDecoration;
import com.example.administrator.essim.utils.LocalData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class FollowShowActivity extends BaseActivity {

    private Toolbar mToolbar;
    private Context mContext;
    private TextView mTextView;
    private int userID, dataType;
    private boolean isLoadingMore;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private String title, next_url, searchKey;
    private UserFollowAdapter mUserFollowAdapter;
    private List<SearchUserResponse.UserPreviewsBean> mUserPreviewsBeanList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_show);
        mContext = this;
        Intent intent = getIntent();
        userID = intent.getIntExtra("user id", 0);
        initView();
        if (userID == 0) {  //这是不传入用户id的启动方式，userID被默认置0，是从搜索页面（SearchActivity）跳转过来的
            searchKey = intent.getStringExtra("search_key");    //获取搜索页的关键词开始搜索
            getUserByName();
        } else {
            title = intent.getStringExtra("user name");
            if (intent.getBooleanExtra("show recommend user", false)) {
                getRecommendUser();
            } else {
                getUserFollowing("public");
            }
        }
    }

    private void initView() {
        mProgressBar = findViewById(R.id.try_login);
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView = findViewById(R.id.pixiv_recy);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(view -> finish());
        LinearLayoutManager linearLayout = new LinearLayoutManager(mContext);
        linearLayout.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayout);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new LinearItemDecoration(DensityUtil.dip2px(mContext, 16.0f)));
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, final int dx, final int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = linearLayout.findLastVisibleItemPosition();
                int totalItemCount = mUserFollowAdapter.getItemCount();
                if (lastVisibleItem >= totalItemCount - 2 && dy > 0 && !isLoadingMore) {
                    getNextData();
                    isLoadingMore = true;
                }
            }
        });
        mTextView = findViewById(R.id.show_nothing);
    }

    private void getUserFollowing(String followerType) {
        mProgressBar.setVisibility(View.VISIBLE);
        dataType = followerType.equals("public") ? 0 : 1;
        Call<SearchUserResponse> call = RestClient.retrofit_AppAPI
                .create(AppApiPixivService.class)
                .getUserFollowing(LocalData.getToken(), userID, followerType);
        call.enqueue(new Callback<SearchUserResponse>() {
            @Override
            public void onResponse(Call<SearchUserResponse> call, retrofit2.Response<SearchUserResponse> response) {
                if (response.body().getUser_previews().size() == 0) {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mTextView.setText("这里空空的，什么也没有~");
                    mTextView.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.INVISIBLE);
                } else {
                    next_url = response.body().getNext_url();
                    mUserPreviewsBeanList.clear();
                    mUserPreviewsBeanList.addAll(response.body().getUser_previews());
                    mUserFollowAdapter = new UserFollowAdapter(mUserPreviewsBeanList, mContext);
                    mToolbar.setTitle(dataType == 0 ? title + "的关注(公开)" : title + "的关注(非公开)");
                    mUserFollowAdapter.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position, int viewType) {
                            try {
                                Intent intent = new Intent(mContext, UserDetailActivity.class);
                                intent.putExtra("user id", mUserPreviewsBeanList.get(position)
                                        .getUser().getId());
                                startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onItemLongClick(View view, int position) {

                        }
                    });
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mRecyclerView.setAdapter(mUserFollowAdapter);
                    if (mRecyclerView.getVisibility() == View.INVISIBLE) {
                        mRecyclerView.setVisibility(View.VISIBLE);
                        mTextView.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchUserResponse> call, Throwable throwable) {

            }
        });
    }

    private void getRecommendUser() {
        mProgressBar.setVisibility(View.VISIBLE);
        dataType = 2;
        Call<SearchUserResponse> call = RestClient.retrofit_AppAPI
                .create(AppApiPixivService.class)
                .getRecommendUser(LocalData.getToken());
        call.enqueue(new Callback<SearchUserResponse>() {
            @Override
            public void onResponse(Call<SearchUserResponse> call, retrofit2.Response<SearchUserResponse> response) {
                if (response.body().getUser_previews().size() == 0) {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mTextView.setText("这里空空的，什么也没有~");
                    mTextView.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.INVISIBLE);
                } else {
                    next_url = response.body().getNext_url();
                    mUserPreviewsBeanList.clear();
                    mUserPreviewsBeanList.addAll(response.body().getUser_previews());
                    mUserFollowAdapter = new UserFollowAdapter(mUserPreviewsBeanList, mContext);
                    mToolbar.setTitle(title + "的关注(推荐)");
                    mUserFollowAdapter.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position, int viewType) {
                            try {
                                Intent intent = new Intent(mContext, UserDetailActivity.class);
                                intent.putExtra("user id", mUserPreviewsBeanList.get(position)
                                        .getUser().getId());
                                startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onItemLongClick(View view, int position) {

                        }
                    });
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mRecyclerView.setAdapter(mUserFollowAdapter);
                    if (mRecyclerView.getVisibility() == View.INVISIBLE) {
                        mRecyclerView.setVisibility(View.VISIBLE);
                        mTextView.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchUserResponse> call, Throwable throwable) {

            }
        });
    }

    private void getUserByName() {
        mProgressBar.setVisibility(View.VISIBLE);
        Call<SearchUserResponse> call = RestClient.retrofit_AppAPI
                .create(AppApiPixivService.class)
                .getSearchUser(LocalData.getToken(), searchKey);
        call.enqueue(new Callback<SearchUserResponse>() {
            @Override
            public void onResponse(Call<SearchUserResponse> call, retrofit2.Response<SearchUserResponse> response) {
                next_url = response.body().getNext_url();
                mUserPreviewsBeanList.clear();
                mUserPreviewsBeanList.addAll(response.body().getUser_previews());
                mUserFollowAdapter = new UserFollowAdapter(mUserPreviewsBeanList, mContext);
                mToolbar.setTitle("搜索结果");
                mUserFollowAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position, int viewType) {
                        try {
                            Intent intent = new Intent(mContext, UserDetailActivity.class);
                            intent.putExtra("user id", mUserPreviewsBeanList.get(position)
                                    .getUser().getId());
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });
                mProgressBar.setVisibility(View.INVISIBLE);
                mRecyclerView.setAdapter(mUserFollowAdapter);
            }

            @Override
            public void onFailure(Call<SearchUserResponse> call, Throwable throwable) {

            }
        });
    }

    private void getNextData() {
        if (next_url != null) {
            mProgressBar.setVisibility(View.VISIBLE);
            Call<SearchUserResponse> call = RestClient.retrofit_AppAPI
                    .create(AppApiPixivService.class)
                    .getNextUser(LocalData.getToken(), next_url);
            call.enqueue(new Callback<SearchUserResponse>() {
                @Override
                public void onResponse(Call<SearchUserResponse> call,
                                       retrofit2.Response<SearchUserResponse> response) {
                    next_url = response.body().getNext_url();
                    mUserPreviewsBeanList.addAll(response.body().getUser_previews());
                    mUserFollowAdapter.notifyDataSetChanged();
                    isLoadingMore = false;
                    mProgressBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onFailure(Call<SearchUserResponse> call, Throwable throwable) {
                    mProgressBar.setVisibility(View.INVISIBLE);
                }
            });
        } else {
            Snackbar.make(mRecyclerView, "再怎么找也找不到了~", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if (LocalData.getUserID() == userID) {
            //加载关于自己的菜单，可以查看自己的公开或者非公开收藏
            getMenuInflater().inflate(R.menu.user_follow, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_get_public:
                if (dataType != 0) {
                    getUserFollowing("public");
                }
                break;
            case R.id.action_get_private:
                if (dataType != 1) {
                    getUserFollowing("private");
                }
                break;
            case R.id.action_get_recommend:
                if (dataType != 2) {
                    getRecommendUser();
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);



    }
}
