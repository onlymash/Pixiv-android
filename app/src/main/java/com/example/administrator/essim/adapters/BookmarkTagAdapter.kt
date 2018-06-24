package com.example.administrator.essim.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.administrator.essim.R
import com.example.administrator.essim.interf.OnItemClickListener
import com.example.administrator.essim.response.BookmarkDetailResponse
import com.example.administrator.essim.response.BookmarkTagResponse
import com.example.administrator.essim.response.PixivResponse
import kotlinx.android.synthetic.main.bookmark_tag_item.view.*
import kotlinx.android.synthetic.main.item_custom_suggestion.view.*

class BookmarkTagAdapter(private var mPixivRankItem: ArrayList<BookmarkTagResponse>,
                         context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(context)
    private var mOnItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            TagHolder(mLayoutInflater.inflate(R.layout.bookmark_tag_item, parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as TagHolder).itemView.tag_text.text = mPixivRankItem[position].name
        holder.itemView.tag_check_box.isChecked = mPixivRankItem[position].isIs_registered
        holder.itemView.setOnClickListener {
            when {
                !mPixivRankItem[position].isIs_registered -> {
                    mOnItemClickListener!!.onItemClick(holder.itemView, position, 0)
                    mPixivRankItem[position].isIs_registered = true
                    holder.itemView.tag_check_box.isChecked = true
                }
                else -> {
                    mOnItemClickListener!!.onItemClick(holder.itemView, position, 1)
                    mPixivRankItem[position].isIs_registered = false
                    holder.itemView.tag_check_box.isChecked = false
                }
            }
        }
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        mOnItemClickListener = onItemClickListener
    }

    fun addData(tag: String) {
        mPixivRankItem.add(0, BookmarkTagResponse(tag, true));
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = mPixivRankItem.size

    private class TagHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
