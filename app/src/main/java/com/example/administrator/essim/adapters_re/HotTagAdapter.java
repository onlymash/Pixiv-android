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
import com.example.administrator.essim.response.TrendingtagResponse;
import com.example.administrator.essim.utils_re.GlideUtil;

import java.util.List;

public class HotTagAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private OnItemClickListener mOnItemClickListener;
    private int imgHeight;
    private List<TrendingtagResponse.TrendTagsBean> allIllust;

    public HotTagAdapter(List<TrendingtagResponse.TrendTagsBean> list, Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        imgHeight = (mContext.getResources().getDisplayMetrics().widthPixels -
                2 * mContext.getResources().getDimensionPixelSize(R.dimen.one_dp)) / 3;
        allIllust = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.recy_hot_tags, parent, false);
        return new TagHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(position == 0){
            Glide.with(mContext).load(GlideUtil.getLargeImage(allIllust.get(position).getIllust())).into(((TagHolder) holder).mImageView);
            ((TagHolder) holder).mImageView.getLayoutParams().height = 2 * imgHeight;
        }else {
            Glide.with(mContext).load(GlideUtil.getSquare(allIllust.get(position).getIllust())).into(((TagHolder) holder).mImageView);
            ((TagHolder) holder).mImageView.getLayoutParams().height = imgHeight;
        }
        ((TagHolder) holder).mTextView.setText(allIllust.get(position).getTag());
        if(mOnItemClickListener != null){
            holder.itemView.setOnClickListener(v -> mOnItemClickListener.onItemClick(v, position, 0));
            holder.itemView.setOnLongClickListener(v -> {
                mOnItemClickListener.onItemLongClick(v, position);
                return true;
            });
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
        private ImageView mImageView;
        private TextView mTextView;

        TagHolder(View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.imageView);
            mTextView = itemView.findViewById(R.id.tag_name);
        }
    }
}
