package com.example.administrator.essim.adapters_re;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.essim.R;
import com.example.administrator.essim.interf.OnItemClickListener;
import com.example.administrator.essim.response_re.UserPreviewsBean;
import com.example.administrator.essim.utils.PixivOperate;
import com.example.administrator.essim.utils_re.GlideUtil;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserFollowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private OnItemClickListener mOnItemClickListener;
    private List<UserPreviewsBean> allUsers = new ArrayList<>();

    public UserFollowAdapter(List<UserPreviewsBean> users, Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        allUsers = users;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.recy_user_follow, parent, false);
        return new TagHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((TagHolder) holder).mTextView.setText(allUsers.get(position).getUser().getName());
        Glide.with(mContext).load(GlideUtil.getUserHead(allUsers.get(position))).into(((TagHolder) holder).mCircleImageView);
        if (allUsers.get(position).getIllusts().size() == 3) {
            Glide.with(mContext).load(GlideUtil.getMediumImg(allUsers.get(position).getIllusts().get(0)))
                    .into(((TagHolder) holder).mImageView);
            Glide.with(mContext).load(GlideUtil.getMediumImg(allUsers.get(position).getIllusts().get(1)))
                    .into(((TagHolder) holder).mImageView2);
            Glide.with(mContext).load(GlideUtil.getMediumImg(allUsers.get(position).getIllusts().get(2)))
                    .into(((TagHolder) holder).mImageView3);
        }
        if (allUsers.get(position).getUser().isIs_followed()) {
            ((TagHolder) holder).mButton.setText(mContext.getString(R.string.cancel_follow_him));
        } else {
            ((TagHolder) holder).mButton.setText(mContext.getString(R.string.follow_him));
        }
        ((TagHolder) holder).mButton.setOnClickListener(v -> {
            if (allUsers.get(position).getUser().isIs_followed()) {
                PixivOperate.postUnFollowUser(allUsers.get(position).getUser().getId());
                ((TagHolder) holder).mButton.setText(mContext.getString(R.string.follow_him));
                allUsers.get(position).getUser().setIs_followed(false);
            } else {
                PixivOperate.postFollowUser(allUsers.get(position).getUser().getId(), "public");
                ((TagHolder) holder).mButton.setText(mContext.getString(R.string.cancel_follow_him));
                allUsers.get(position).getUser().setIs_followed(true);
            }
        });

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(v -> {
                mOnItemClickListener.onItemClick(holder.itemView, position, 0);
            });
        }
    }

    @Override
    public int getItemCount() {
        return allUsers.size();
    }

    public OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }


    public static class TagHolder extends RecyclerView.ViewHolder {
        private Button mButton;
        private TextView mTextView;
        private CircleImageView mCircleImageView;
        private ImageView mImageView, mImageView2, mImageView3;

        TagHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.user_name);
            mImageView = itemView.findViewById(R.id.user_show_one);
            mImageView2 = itemView.findViewById(R.id.user_show_two);
            mImageView3 = itemView.findViewById(R.id.user_show_three);
            mCircleImageView = itemView.findViewById(R.id.user_head);
            mButton = itemView.findViewById(R.id.post_like_user);
        }
    }
}
