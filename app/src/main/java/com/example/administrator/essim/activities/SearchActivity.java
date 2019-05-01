package com.example.administrator.essim.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.widget.NestedScrollView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.essim.R;
import com.example.administrator.essim.activities_re.FollowActivity;
import com.example.administrator.essim.activities_re.UserDetailActivity;
import com.example.administrator.essim.adapters.AutoFieldAdapter;
import com.example.administrator.essim.interf.OnItemClickListener;
import com.example.administrator.essim.network.AppApiPixivService;
import com.example.administrator.essim.network.RestClient;
import com.example.administrator.essim.response.PixivResponse;
import com.example.administrator.essim.response.TagResponse;
import com.example.administrator.essim.utils.Common;
import com.example.administrator.essim.utils.PixivOperate;
import com.example.administrator.essim.utils_re.LocalData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;

public class SearchActivity extends BaseActivity implements MaterialSearchBar.OnSearchActionListener {

    private static final String GET_TAG_URL = "https://api.imjad.cn/pixiv/v1/?type=tags";

    private int searchType;
    private CardView mCardView;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private MaterialSearchBar searchBar;
    private TagFlowLayout mTagFlowLayout;
    private NestedScrollView mNestedScrollView;
    private AutoFieldAdapter customSuggestionsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
    }

    private void initView() {
        mCardView = findViewById(R.id.card_search);
        mRecyclerView = findViewById(R.id.recy);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mProgressBar = findViewById(R.id.try_login);
        mProgressBar.setVisibility(View.INVISIBLE);
        searchBar = findViewById(R.id.searchBar);
        searchBar.setPlaceHolder(getResources().getString(R.string.word_get_illust));
        searchBar.inflateMenu(R.menu.search_type);
        searchBar.getMenu().setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.type_one:
                    if (searchType != 0) {
                        searchBar.setPlaceHolder(getResources().getString(R.string.word_get_illust));
                        searchType = 0;
                    }
                    break;
                case R.id.type_two:
                    if (searchType != 1) {
                        searchBar.setPlaceHolder(getResources().getString(R.string.id_get_illust));
                        searchType = 1;
                    }
                    break;
                case R.id.type_three:
                    if (searchType != 2) {
                        searchBar.setPlaceHolder(getResources().getString(R.string.word_get_user));
                        searchType = 2;
                    }
                    break;
                case R.id.type_four:
                    if (searchType != 3) {
                        searchBar.setPlaceHolder(getResources().getString(R.string.id_get_user));
                        searchType = 3;
                    }
                    break;
                default:
                    break;
            }
            return false;
        });
        searchBar.setOnSearchActionListener(this);
        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (searchBar.getText().length() != 0) {
                    if (searchType == 0) {
                        getAutoCompleteWords();
                        mNestedScrollView.setVisibility(View.INVISIBLE);
                    }
                } else {
                    mCardView.setVisibility(View.INVISIBLE);
                    mNestedScrollView.setVisibility(View.VISIBLE);
                }
            }
        });
        mNestedScrollView = findViewById(R.id.card_tag);
        mTagFlowLayout = findViewById(R.id.tag_group);
        getTagGroup();
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {
    }

    @Override
    public void onSearchConfirmed(CharSequence text) {
        if (searchType == 0) {
            Intent intent = new Intent(mContext, SearchResultActivity.class);
            intent.putExtra("what is the keyword", searchBar.getText().trim());
            startActivity(intent);
        } else if (searchType == 1) {
            if (Common.isNumeric(searchBar.getText().trim())) {
                PixivOperate.getSingleIllust(mContext, Integer.valueOf(searchBar.getText().trim()), mProgressBar);
            } else {
                Snackbar.make(searchBar, "作品ID有误~（当前状态 ID搜作品）", Snackbar.LENGTH_SHORT).show();
            }
        } else if (searchType == 2) {
            Intent intent = new Intent(mContext, FollowActivity.class);
            intent.putExtra("search_key", searchBar.getText().trim());
            mContext.startActivity(intent);
        } else if (searchType == 3) {
            if (Common.isNumeric(searchBar.getText().trim())) {
                Intent intent = new Intent(mContext, UserDetailActivity.class);
                intent.putExtra("user id", Integer.valueOf(searchBar.getText().trim()));
                startActivity(intent);
                searchBar.setText("");
            } else {
                Common.showToast("画师ID有误~（当前状态 ID搜画师）");
            }
        }
    }

    @Override
    public void onButtonClicked(int buttonCode) {
        switch (buttonCode) {
            case MaterialSearchBar.BUTTON_BACK:
                searchBar.disableSearch();
                mCardView.setVisibility(View.INVISIBLE);
                mNestedScrollView.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    private void getTagGroup() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(GET_TAG_URL).build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {
                // 注：该回调是子线程，非主线程.
                assert response.body() != null;
                String responseData = response.body().string();
                Gson gson = new Gson();
                try {
                    final List<TagResponse> booksInfo = gson.fromJson(responseData, new TypeToken<List<TagResponse>>() {
                    }.getType());
                    mTagFlowLayout.setOnTagClickListener((view, position, parent) -> {
                        Intent intent = new Intent(mContext, SearchResultActivity.class);
                        intent.putExtra("what is the keyword", booksInfo.get(position).getName());
                        startActivity(intent);
                        return true;
                    });
                    runOnUiThread(() -> mTagFlowLayout.setAdapter(new TagAdapter<List<TagResponse>>(Collections.singletonList(booksInfo)) {
                        @Override
                        public View getView(FlowLayout parent, int position, List<TagResponse> tagResponses) {
                            TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.tag_textview_search,
                                    mTagFlowLayout, false);
                            tv.setText(tagResponses.get(position).getName());
                            return tv;
                        }
                    }));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getAutoCompleteWords() {
        mCardView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        Call<PixivResponse> call = RestClient.retrofit_AppAPI
                .create(AppApiPixivService.class)
                .getSearchAutoCompleteKeywords(LocalData.getToken(),
                        searchBar.getText());
        call.enqueue(new Callback<PixivResponse>() {
            @Override
            public void onResponse(@NonNull Call<PixivResponse> call, @NonNull retrofit2.Response<PixivResponse> response) {
                if(response.body() != null) {
                    customSuggestionsAdapter = new AutoFieldAdapter(response.body(), mContext);
                    customSuggestionsAdapter.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position, int viewType) {
                            Intent intent = new Intent(mContext, SearchResultActivity.class);
                            intent.putExtra("what is the keyword", ((TextView) view).getText());
                            startActivity(intent);
                        }

                        @Override
                        public void onItemLongClick(View view, int position) {

                        }
                    });
                    mRecyclerView.setAdapter(customSuggestionsAdapter);
                    mCardView.setVisibility(View.VISIBLE);
                    mNestedScrollView.setVisibility(View.INVISIBLE);

                }else {
                    Common.showToast("相关搜索词获取失败");
                }
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(@NonNull Call<PixivResponse> call, @NonNull Throwable throwable) {

            }
        });
    }
}
