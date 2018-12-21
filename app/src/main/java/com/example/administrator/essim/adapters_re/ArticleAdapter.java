package com.example.administrator.essim.adapters_re;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.essim.R;
import com.example.administrator.essim.interf.OnItemClickListener;
import com.example.administrator.essim.response_re.ArticleResponse;
import com.example.administrator.essim.response_re.IllustsBean;
import com.example.administrator.essim.utils.PixivOperate;
import com.example.administrator.essim.utils_re.GlideUtil;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private OnItemClickListener mOnItemClickListener;
    private int imgHeight;
    private List<ArticleResponse.SpotlightArticlesBean> allIllust;

    public ArticleAdapter(List<ArticleResponse.SpotlightArticlesBean> list, Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        imgHeight = (mContext.getResources().getDisplayMetrics().widthPixels -
                mContext.getResources().getDimensionPixelSize(R.dimen.thirty_two_dp)) / 3 * 2;
        allIllust = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.recy_special_collec, parent, false);
        return new TagHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((TagHolder) holder).mImageView.getLayoutParams().height = imgHeight;
        Glide.with(mContext).load(GlideUtil.getArticle(allIllust.get(position).getThumbnail())).into(((TagHolder) holder).mImageView);
        ((TagHolder) holder).mTextView.setText(allIllust.get(position).getTitle());
        ((TagHolder) holder).mTextView2.setText(allIllust.get(position).getPublish_date().substring(0, 10));
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
        private ImageView mImageView;
        private TextView mTextView, mTextView2;

        TagHolder(View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.imageView);
            mTextView = itemView.findViewById(R.id.text_title);
            mTextView2 = itemView.findViewById(R.id.text_date);
        }
    }
}
