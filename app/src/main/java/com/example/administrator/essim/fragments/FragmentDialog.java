package com.example.administrator.essim.fragments;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.example.administrator.essim.R;
import com.example.administrator.essim.adapters.BookmarkTagAdapter;
import com.example.administrator.essim.interf.OnItemClickListener;
import com.example.administrator.essim.network.AppApiPixivService;
import com.example.administrator.essim.network.RestClient;
import com.example.administrator.essim.response.BookmarkDetailResponse;
import com.example.administrator.essim.response.IllustsBean;
import com.example.administrator.essim.response.Reference;
import com.example.administrator.essim.utils.Common;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class FragmentDialog {

    private Context mContext;
    private IllustsBean mIllustsBean;
    private BookmarkTagAdapter bookmarkTagAdapter;

    public FragmentDialog(Context context, IllustsBean i){
        mContext = context;
        mIllustsBean = i;
    }

    private void getBookedTag(RecyclerView recyclerView, ProgressBar progressBar, List<String> tagList) {
        Call<BookmarkDetailResponse> call = new RestClient()
                .getRetrofit_AppAPI()
                .create(AppApiPixivService.class)
                .getBookmarkDetail(Common.getLocalDataSet().getString("Authorization", ""),
                        mIllustsBean.getId());
        call.enqueue(new Callback<BookmarkDetailResponse>() {
            @Override
            public void onResponse(Call<BookmarkDetailResponse> call, retrofit2.Response<BookmarkDetailResponse> response) {
                bookmarkTagAdapter = new BookmarkTagAdapter(response.body().bookmark_detail.tags, mContext);
                bookmarkTagAdapter.setOnItemClickListener(new OnItemClickListener() {
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
                recyclerView.setAdapter(bookmarkTagAdapter);
            }

            @Override
            public void onFailure(Call<BookmarkDetailResponse> call, Throwable throwable) {

            }
        });
    }

    public void showDialog(Context context) {
        Dialog mDialog;
        mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.view_dialog, null);
        RecyclerView recyclerView = dialogView.findViewById(R.id.tag_recy);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setHasFixedSize(true);
        List<String> tagList = new ArrayList<>();
        getBookedTag(recyclerView, dialogView.findViewById(R.id.mProgressbar), tagList);
        mDialog.setContentView(dialogView);

        Switch swit = dialogView.findViewById(R.id.private_switch);
        TextView likeIllust = dialogView.findViewById(R.id.post_like_illust);
        likeIllust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.postStarIllust(mIllustsBean, tagList,
                        mContext, swit.isChecked() ? "private" : "public");
                mDialog.dismiss();
            }
        });
        EditText editText = dialogView.findViewById(R.id.mEditText);
        ImageButton button = dialogView.findViewById(R.id.add_tag);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().trim().length() != 0) {
                    bookmarkTagAdapter.addData(editText.getText().toString());
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
