package com.example.administrator.essim.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.essim.R;

public class DonaterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private String[] mDonaterNames;

    public DonaterAdapter(String[] list, Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mDonaterNames = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.recy_donate_item, parent, false);
        return new TagHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((TagHolder) holder).mTextView.setText(mDonaterNames[position]);
    }

    @Override
    public int getItemCount() {
        return mDonaterNames.length;
    }

    public class TagHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;

        TagHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.donater_name);
        }
    }
}
