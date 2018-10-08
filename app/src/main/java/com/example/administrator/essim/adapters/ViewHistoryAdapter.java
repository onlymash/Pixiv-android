package com.example.administrator.essim.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.essim.R;
import com.example.administrator.essim.response.ViewHistory;
import com.example.administrator.essim.utils.Common;

import java.util.List;

public class ViewHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<ViewHistory> mDonaterNames;

    public ViewHistoryAdapter(List<ViewHistory> list, Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mDonaterNames = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.recy_view_history, parent, false);
        return new TagHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((TagHolder) holder).mTextView.setText(mDonaterNames.get(position).getTitle());
        ((TagHolder) holder).mTextView2.setText(mDonaterNames.get(position).getAuthor());
        ((TagHolder) holder).mTextView3.setText(Common.getTime(mDonaterNames.get(position).getView_time()));
        if(mDonaterNames.get(position).getIllustSize().equals("1")){
            ((TagHolder) holder).mTextView4.setVisibility(View.INVISIBLE);
        }else {
            ((TagHolder) holder).mTextView4.setVisibility(View.VISIBLE);
            ((TagHolder) holder).mTextView4.setText(String.format("%sP", mDonaterNames.get(position).getIllustSize()));
        }
    }

    @Override
    public int getItemCount() {
        return mDonaterNames.size();
    }

    public class TagHolder extends RecyclerView.ViewHolder {
        private TextView mTextView, mTextView2, mTextView3, mTextView4;

        TagHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.title);
            mTextView2 = itemView.findViewById(R.id.user);
            mTextView3 = itemView.findViewById(R.id.time);
            mTextView4 = itemView.findViewById(R.id.all_item_size);
        }
    }
}
