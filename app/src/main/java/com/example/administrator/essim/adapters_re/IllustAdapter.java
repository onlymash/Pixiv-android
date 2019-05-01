package com.example.administrator.essim.adapters_re;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.essim.R;
import com.example.administrator.essim.interf.OnItemClickListener;
import com.example.administrator.essim.response_re.IllustsBean;
import com.example.administrator.essim.utils.PixivOperate;
import com.example.administrator.essim.utils_re.GlideUtil;

import java.util.List;

public class IllustAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private OnItemClickListener mOnItemClickListener;
    private int imgHeight;
    private List<IllustsBean> allIllust;

    public IllustAdapter(List<IllustsBean> list, Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        imgHeight = (mContext.getResources().getDisplayMetrics().widthPixels -
                mContext.getResources().getDimensionPixelSize(R.dimen.four_dp)) / 2;
        allIllust = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.recy_login_bg, parent, false);
        return new TagHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((TagHolder) holder).mImageView.getLayoutParams().height = imgHeight;
        Glide.with(mContext).load(GlideUtil.getMediumImg(allIllust.get(position))).into(((TagHolder) holder).mImageView);
        if(allIllust.get(position).getPage_count() != 1){
            ((TagHolder) holder).mTextView.setVisibility(View.VISIBLE);
            ((TagHolder) holder).mTextView.setText(String.format("%sP", String.valueOf(allIllust.get(position).getPage_count())));
        }else {
            ((TagHolder) holder).mTextView.setVisibility(View.INVISIBLE);
        }
        ((TagHolder) holder).star.setImageResource(allIllust.get(position).isIs_bookmarked() ?
                R.drawable.ic_favorite_white_24dp : R.drawable.no_favor);
        ((TagHolder) holder).star.setOnClickListener(v -> {
            if(allIllust.get(position).isIs_bookmarked()){
                allIllust.get(position).setIs_bookmarked(false);
                ((TagHolder) holder).star.setImageResource(R.drawable.no_favor);
                PixivOperate.postUnstarIllust(allIllust.get(position).getId(), mContext);
            }else {
                allIllust.get(position).setIs_bookmarked(true);
                ((TagHolder) holder).star.setImageResource(R.drawable.ic_favorite_white_24dp);
                PixivOperate.postStarIllust(allIllust.get(position).getId(), mContext, "public");
            }
        });
        ((TagHolder) holder).star.setOnLongClickListener(v -> {
            mOnItemClickListener.onItemLongClick(((TagHolder) holder).star, position);
            return true;
        });
        if(mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(v -> mOnItemClickListener.onItemClick(v, position, 0));
        }
    }

    @Override
    public int getItemCount() {
        return allIllust.size();
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    public static class TagHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView, star;
        private TextView mTextView;

        TagHolder(View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.imageView);
            star = itemView.findViewById(R.id.star);
            mTextView = itemView.findViewById(R.id.pixiv_item_size);
        }
    }
}
