package com.example.administrator.essim.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.essim.R;
import com.example.administrator.essim.interf.OnItemClickListener;
import com.example.administrator.essim.response.IllustsBean;
import com.example.administrator.essim.utils.GlideUtil;

import java.util.List;

public class BatchSelectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<IllustsBean> allIllust;
    private OnItemClickListener mOnItemClickListener = null;
    private int imageHeight;

    public BatchSelectAdapter(List<IllustsBean> list, Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        allIllust = list;
        imageHeight = ((mContext.getResources().getDisplayMetrics().widthPixels -
                3 * mContext.getResources().getDimensionPixelSize(R.dimen.eight_dip)) / 2) * 6 / 5;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.pixiv_item_download, parent, false);
        return new TagHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((TagHolder) holder).mImageView.getLayoutParams().height = imageHeight;
        Glide.with(mContext).load(new GlideUtil().getMediumImageUrl(allIllust.get(position)))
                .into(((TagHolder) holder).mImageView);
        if (allIllust.get(position).getPage_count() == 1) {
            ((TagHolder) holder).mTextView.setVisibility(View.INVISIBLE);
        } else {
            ((TagHolder) holder).mTextView.setVisibility(View.VISIBLE);
            ((TagHolder) holder).mTextView.setText(String.format("%sP", allIllust.get(position).getPage_count()));
        }
        ((TagHolder) holder).mCheckBox.setChecked(allIllust.get(position).isSelected());
        ((TagHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allIllust.get(position).isSelected()) {
                    ((TagHolder) holder).mCheckBox.setChecked(false);
                    allIllust.get(position).setSelected(false);
                    mOnItemClickListener.onItemClick(view, position, 0);
                } else {
                    ((TagHolder) holder).mCheckBox.setChecked(true);
                    allIllust.get(position).setSelected(true);
                    mOnItemClickListener.onItemClick(view, position, 1);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return allIllust.size();
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    public class TagHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView mTextView;
        private CheckBox mCheckBox;

        TagHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.pixiv_item_size);
            mImageView = itemView.findViewById(R.id.pixiv_image);
            mCheckBox = itemView.findViewById(R.id.is_checked);
        }
    }
}
