package com.example.administrator.essim.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.administrator.essim.R
import com.example.administrator.essim.interf.OnItemClickListener
import com.example.administrator.essim.response.AllBookmarkTagResponse
import com.example.administrator.essim.response.PixivResponse
import com.example.administrator.essim.response.SingleTag
import kotlinx.android.synthetic.main.booked_tag_item.view.*
import kotlinx.android.synthetic.main.item_custom_suggestion.view.*

class BookedTagAdapter(private val mPixivRankItem: List<SingleTag>,
                       context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(context)
    private var mOnItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            TagHolder(mLayoutInflater.inflate(R.layout.booked_tag_item, parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as TagHolder).itemView.tag_item.text = mPixivRankItem[position].name
        holder.itemView.tag_count.text = mPixivRankItem[position].count
        holder.itemView.setOnClickListener { mOnItemClickListener!!.onItemClick(holder.itemView, position, 0) }
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        mOnItemClickListener = onItemClickListener
    }

    override fun getItemCount(): Int = mPixivRankItem.size

    private class TagHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
