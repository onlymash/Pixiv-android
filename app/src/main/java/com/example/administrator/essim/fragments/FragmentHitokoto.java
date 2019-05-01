package com.example.administrator.essim.fragments;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.essim.R;
import com.example.administrator.essim.activities_re.MainActivity;
import com.example.administrator.essim.response.HitoModel;
import com.example.administrator.essim.response.HitokotoType;
import com.example.administrator.essim.utils.Common;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/1/15 0015.
 */

public class FragmentHitokoto extends BaseFragment {

    public static boolean need_to_refresh = false;
    private final String url_head = "https://v1.hitokoto.cn/?c=";
    private TextView mTextView1, mTextView2, mTextView3, mTextView4, mTextView5;
    private Button mButton, mButton2;
    private Toolbar mToolbar;
    private CardView mCardView;
    private String responseData, finalResponseData, catname;
    private Gson gson = new Gson();
    private HitoModel mHitoModel;
    private AppCompatSpinner mAppCompatSpinner;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_hitikoto, container, false);
        initView(v);
        return v;
    }

    private void initView(View view) {
        mCardView = view.findViewById(R.id.card_hitokoto);
        mTextView1 = view.findViewById(R.id.hitokoto_text);
        mTextView2 = view.findViewById(R.id.hitokoto_author);
        mTextView3 = view.findViewById(R.id.hitokoto_date);
        mTextView4 = view.findViewById(R.id.hitokoto_catname);
        mTextView5 = view.findViewById(R.id.hitokoto_resouce);
        mButton = view.findViewById(R.id.refresh);
        mButton2 = view.findViewById(R.id.collect_it);
        mButton.setOnClickListener(v -> getData(url_head + catname));
        mButton2.setOnClickListener(v -> {
            HitoModel hitoModel = mHitoModel;
            hitoModel.save();
            need_to_refresh = true;
            Common.showToast("已添加到收藏~");
        });
        mAppCompatSpinner = view.findViewById(R.id.spinner);
        mAppCompatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        catname = "";
                        break;
                    case 1:
                        catname = "a";
                        break;
                    case 2:
                        catname = "b";
                        break;
                    case 3:
                        catname = "c";
                        break;
                    case 4:
                        catname = "d";
                        break;
                    case 5:
                        catname = "e";
                        break;
                    case 6:
                        catname = "f";
                        break;
                    case 7:
                        catname = "g";
                        break;
                }
                getData(url_head + catname);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        mToolbar = view.findViewById(R.id.toolbar_hitokoto);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(v -> ((MainActivity) Objects.requireNonNull(getActivity()))
                .getDrawer().openDrawer(GravityCompat.START, true));
    }

    private void getData(String address) {
        Common.sendOkhttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(() -> Common.showToast("数据加载失败"));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                responseData = response.body().string();
                finalResponseData = responseData.replaceAll("from", "from_where");
                mHitoModel = gson.fromJson(finalResponseData, HitoModel.class);
                Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                    mTextView1.setText(mHitoModel.getHitokoto());
                    mTextView2.setText(getString(R.string.author_is, mHitoModel.getCreator()));
                    mTextView3.setText(Common.getTime(mHitoModel.getCreated_at()));
                    mTextView4.setText(mHitoModel.getFrom_where());
                    mTextView4.requestFocus();
                    mTextView5.setText(HitokotoType.getType(mHitoModel.getType()));
                });
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_hitokoto, menu);
    }

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                getData(url_head + catname);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}