package com.example.administrator.essim.fragments;

import android.app.Dialog;
import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.example.administrator.essim.R;
import com.example.administrator.essim.adapters.IllustTagAdapter;
import com.example.administrator.essim.interf.OnItemClickListener;
import com.example.administrator.essim.network.AppApiPixivService;
import com.example.administrator.essim.network.RestClient;
import com.example.administrator.essim.response.BookmarkDetailResponse;
import com.example.administrator.essim.response_re.IllustsBean;
import com.example.administrator.essim.utils.PixivOperate;
import com.example.administrator.essim.utils_re.LocalData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class FragmentDialog {

    private View view;
    private Context mContext;
    private IllustsBean mIllustsBean;
    private IllustTagAdapter illustTagAdapter;

    public FragmentDialog(Context context, View v, IllustsBean illustsBean) {
        mContext = context;
        view = v;
        mIllustsBean = illustsBean;
    }

    private void getBookedTag(RecyclerView recyclerView, ProgressBar progressBar, List<String> tagList) {
        Call<BookmarkDetailResponse> call = RestClient.retrofit_AppAPI
                .create(AppApiPixivService.class)
                .getBookmarkDetail(LocalData.getToken(), mIllustsBean.getId());
        call.enqueue(new Callback<BookmarkDetailResponse>() {
            @Override
            public void onResponse(Call<BookmarkDetailResponse> call, retrofit2.Response<BookmarkDetailResponse> response) {
                illustTagAdapter = new IllustTagAdapter(response.body().bookmark_detail.tags, mContext);
                illustTagAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(@NotNull View view, int position, int viewType) {
                        if (viewType == 0) {
                            tagList.add(response.body().bookmark_detail.tags.get(position).name);
                        } else {
                            tagList.remove(response.body().bookmark_detail.tags.get(position).name);
                        }
                    }

                    @Override
                    public void onItemLongClick(@NotNull View view, int position) {

                    }
                });
                progressBar.setVisibility(View.INVISIBLE);
                recyclerView.setAdapter(illustTagAdapter);
            }

            @Override
            public void onFailure(Call<BookmarkDetailResponse> call, Throwable throwable) {

            }
        });
    }

    public void showDialog() {
        Dialog mDialog;
        mDialog = new Dialog(mContext);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View dialogView = inflater.inflate(R.layout.view_dialog, null);
        ImageView close = dialogView.findViewById(R.id.close);
        close.setOnClickListener(v -> mDialog.dismiss());
        RecyclerView recyclerView = dialogView.findViewById(R.id.tag_recy);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setHasFixedSize(true);
        List<String> tagList = new ArrayList<>();
        getBookedTag(recyclerView, dialogView.findViewById(R.id.mProgressbar), tagList);
        mDialog.setContentView(dialogView);

        Switch swit = dialogView.findViewById(R.id.private_switch);
        TextView likeIllust = dialogView.findViewById(R.id.post_like_illust);
        likeIllust.setOnClickListener(v -> {
            PixivOperate.postStarIllust(mIllustsBean, tagList,
                    mContext, swit.isChecked() ? "private" : "public");
            mDialog.dismiss();
            ((ImageView) view).setImageResource(R.drawable.ic_favorite_white_24dp);
        });
        EditText editText = dialogView.findViewById(R.id.mEditText);
        ImageButton button = dialogView.findViewById(R.id.add_tag);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().trim().length() != 0) {
                    illustTagAdapter.addData(editText.getText().toString());
                    tagList.add(editText.getText().toString());
                    editText.setText("");
                    editText.setHint("添加标签");
                }
            }
        });
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = mDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mDialog.show();
        window.setAttributes(lp);
        mDialog.setCancelable(true);
    }
}
