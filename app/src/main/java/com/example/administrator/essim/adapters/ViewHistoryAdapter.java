package com.example.administrator.essim.adapters;

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
import com.example.administrator.essim.response.ViewHistory;
import com.example.administrator.essim.utils.Common;
import com.example.administrator.essim.utils_re.GlideUtil;

import java.util.List;

public class ViewHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<ViewHistory> mDonaterNames;
    private OnItemClickListener mOnItemClickListener;
    private int imageWidth, imageHeight;

    public OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public ViewHistoryAdapter(List<ViewHistory> list, Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mDonaterNames = list;
        imageWidth = (mContext.getResources().getDisplayMetrics().widthPixels)*5/ 12;
        imageHeight = imageWidth * 4/ 5;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.recy_view_history, parent, false);
        return new TagHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((TagHolder) holder).mNiceImageView.getLayoutParams().width = imageWidth;
        ((TagHolder) holder).mNiceImageView.getLayoutParams().height = imageHeight;
        if(mDonaterNames.get(position).getImg_url() != null) {
            Glide.with(mContext).load(
                    GlideUtil.getMediumImg(mDonaterNames.get(position).getImg_url()))
                    .into(((TagHolder) holder).mNiceImageView);
        }
        ((TagHolder) holder).mTextView.setText(mDonaterNames.get(position).getIllust_title());
        ((TagHolder) holder).mTextView2.setText(String.format("by: %s",
                mDonaterNames.get(position).getIllust_author()));
        ((TagHolder) holder).mTextView3.setText(Common.getTime(String.valueOf(
                mDonaterNames.get(position).getView_time())));
        Common.showLog("这是查看时间" + mDonaterNames.get(position).getView_time());
        if(mDonaterNames.get(position).getIllust_size().equals("1")){
            ((TagHolder) holder).mTextView4.setVisibility(View.INVISIBLE);
        }else {
            ((TagHolder) holder).mTextView4.setVisibility(View.VISIBLE);
            ((TagHolder) holder).mTextView4.setText(String.format("%sP",
                    mDonaterNames.get(position).getIllust_size()));
        }
        if(mOnItemClickListener != null){
            holder.itemView.setOnClickListener(view ->
                    mOnItemClickListener.onItemClick(holder.itemView, position, 0));
        }
    }

    @Override
    public int getItemCount() {
        return mDonaterNames.size();
    }

    public class TagHolder extends RecyclerView.ViewHolder {
        private ImageView mNiceImageView;
        private TextView mTextView, mTextView2, mTextView3, mTextView4;

        TagHolder(View itemView) {
            super(itemView);
            mNiceImageView = itemView.findViewById(R.id.image);
            mTextView = itemView.findViewById(R.id.title);
            mTextView2 = itemView.findViewById(R.id.user);
            mTextView3 = itemView.findViewById(R.id.time);
            mTextView4 = itemView.findViewById(R.id.all_item_size);
        }
    }
}
