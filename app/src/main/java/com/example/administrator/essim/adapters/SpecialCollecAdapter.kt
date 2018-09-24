package com.example.administrator.essim.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.administrator.essim.R
import com.example.administrator.essim.interf.OnItemClickListener
import com.example.administrator.essim.response.PixivCollectionResponse
import com.example.administrator.essim.response.SpecialCollectionResponse
import com.example.administrator.essim.utils.Common
import com.example.administrator.essim.utils.GlideUtil
import kotlinx.android.synthetic.main.bottom_refresh_widely.view.*
import kotlinx.android.synthetic.main.recy_special_collec.view.*

class SpecialCollecAdapter(private val mPixivRankItem: List<PixivCollectionResponse.SpotlightArticlesBean>,
                           private val mContext: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val itemBottom = 2
    private val itemContent = 1
    private var mOnItemClickListener: OnItemClickListener? = null
    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(mContext)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TagHolder(mLayoutInflater.inflate(R.layout.recy_special_collec, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        return when (holder) {
            is TagHolder -> {
                val params: ViewGroup.LayoutParams = holder.itemView.imageView.layoutParams
                params.height = (mContext.resources.displayMetrics.widthPixels - mContext.resources.getDimensionPixelSize(R.dimen.thirty_two_dp)) / 3 * 2
                holder.itemView.imageView.layoutParams = params
                Glide.with(mContext).load(GlideUtil().getHead(
                        mPixivRankItem[position].thumbnail))
                        .into(holder.itemView.imageView)
                holder.itemView.text_title.text =
                        mPixivRankItem[position].title
                holder.itemView.text_date.text =
                        mPixivRankItem[position].publish_date.substring(0, 10)
                holder.itemView.setOnClickListener { v -> mOnItemClickListener?.onItemClick(v, position, 0) }
            }
            else -> {
            }
        }
    }

    override fun getItemViewType(position: Int): Int = when {
        position >= mPixivRankItem.size -> itemBottom
        else -> itemContent
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        mOnItemClickListener = onItemClickListener
    }

    override fun getItemCount(): Int = mPixivRankItem.size

    private inner class TagHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
