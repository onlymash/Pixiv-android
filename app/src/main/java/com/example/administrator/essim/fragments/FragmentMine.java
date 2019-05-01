package com.example.administrator.essim.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.administrator.essim.R;
import com.example.administrator.essim.activities_re.MainActivity;
import com.example.administrator.essim.adapters.ListHitokotoAdapter;
import com.example.administrator.essim.interf.OnListHitokotoClickListener;
import com.example.administrator.essim.response.HitoModel;
import com.example.administrator.essim.utils.Common;
import com.example.administrator.essim.utils.DensityUtil;
import com.example.administrator.essim.utils.LinearItemDecoration;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Administrator on 2018/1/15 0015.
 */

public class FragmentMine extends BaseFragment {

    private RecyclerView mRecyclerView;
    private List<HitoModel> mHitoModels = new ArrayList<>();
    private ListHitokotoAdapter mAdapter;
    private ImageView mImageView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar_mine);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> ((MainActivity) Objects.requireNonNull(getActivity()))
                .getDrawer().openDrawer(GravityCompat.START, true));
        mRecyclerView = view.findViewById(R.id.mine_recy);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new LinearItemDecoration(DensityUtil.dip2px(mContext, 16.0f)));
        mImageView = view.findViewById(R.id.no_data);
        mAdapter = new ListHitokotoAdapter(mHitoModels, mContext);
        mAdapter.setOnItemClickListener(new OnListHitokotoClickListener() {
            @Override
            public void onItemClick(View view, int position, int code) {
                if (code == 1) {
                    createDialog(position, "这一条", 0);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Common.copyMessage(mContext, mHitoModels.get(position).getHitokoto());
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        //读取数据库
        reFreshLocalData();
    }

    private void setEditableMode() {
        ListHitokotoAdapter.is_editable = !ListHitokotoAdapter.is_editable;
        mAdapter.notifyDataSetChanged();
    }

    private void createDialog(final int index, String title, final int deleteType) {
        getDialog(title).setPositiveButton("确定", (dialog, which) -> {
            if (deleteType == 0) {
                DataSupport.deleteAll(HitoModel.class, "hitokoto = ?", mHitoModels.get(index).getHitokoto());
                mHitoModels.remove(index);
                reFreshLocalData();
            } else if (deleteType == 1) {
                for (int i = 0; i < mHitoModels.size(); i++) {
                    if (mHitoModels.get(i).getSelected()) {
                        DataSupport.deleteAll(HitoModel.class, "hitokoto = ?", mHitoModels.get(i).getHitokoto());
                    }
                }
                reFreshLocalData();
                setEditableMode();
            } else if (deleteType == 2) {
                DataSupport.deleteAll(HitoModel.class);
                mHitoModels.clear();
                mAdapter.notifyDataSetChanged();
            }
        }).setNegativeButton("取消", (dialog, which) -> {
        }).create().show();
    }

    private void reFreshLocalData() {
        mHitoModels.clear();
        mHitoModels.addAll(DataSupport.findAll(HitoModel.class));
        if(mHitoModels.size() == 0){
            mImageView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
            mAdapter.notifyDataSetChanged();
        }else {
            mImageView.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onHiddenChanged(boolean hide) {
        if (!hide) {
            Common.showLog("跑到这里来了啊");
            if (FragmentHitokoto.need_to_refresh) {
                reFreshLocalData();
                mRecyclerView.smoothScrollToPosition(mHitoModels.size());
                mAdapter.notifyDataSetChanged();
                FragmentHitokoto.need_to_refresh = false;
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_mine, menu);
    }

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setHasOptionsMenu(true);
    }

    private AlertDialog.Builder getDialog(String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("来自九条的提示");
        builder.setMessage("真的要删除" + title + "记录吗？");
        return builder;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getItemId() == R.id.action_edit_mode) {
            setEditableMode();
        } else if (item.getItemId() == R.id.action_delete) {
            if (ListHitokotoAdapter.is_editable) {
                createDialog(0, "选中的", 1);
            } else {
                Common.showToast("请先进入<编辑>模式");
            }
        } else if (item.getItemId() == R.id.delete_all) {
            if (mHitoModels.size() != 0) {
                createDialog(0, "所有", 2);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}