package com.example.administrator.essim.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.administrator.essim.R
import com.example.administrator.essim.interf.OnItemClickListener
import com.example.administrator.essim.response.IllustsBean
import com.example.administrator.essim.utils.GlideUtil
import kotlinx.android.synthetic.main.pixiv_item_grid.view.*

/**
 * Created by Administrator on 2018/3/23 0023.
 */
class PixivAdapterGrid(private val mPixivRankItem: List<IllustsBean>,
                       private val mContext: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mOnItemClickListener: OnItemClickListener? = null
    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(mContext)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            PhotoHolder(mLayoutInflater.inflate(R.layout.pixiv_item_grid, parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Glide.with(mContext).load(GlideUtil().getMediumImageUrl(mPixivRankItem[position]))
                .into(holder.itemView.pixiv_image)
        when {
            mPixivRankItem[position].isIs_bookmarked -> holder.itemView.post_like.setImageResource(R.drawable.ic_favorite_white_24dp)
            else -> holder.itemView.post_like.setImageResource(R.drawable.ic_favorite_border_black_24dp)
        }
        when {
            mPixivRankItem[position].page_count > 1 -> {
                holder.itemView.pixiv_item_size.visibility = View.VISIBLE
                holder.itemView.pixiv_item_size.text = String.format("%sP", mPixivRankItem[position].page_count.toString())
            }
            else -> holder.itemView.pixiv_item_size.visibility = View.INVISIBLE
        }
        //为收藏按钮设置点击事件
        holder.itemView.setOnClickListener { mOnItemClickListener!!.onItemClick(holder.itemView, position, 0) }
        holder.itemView.post_like.setOnClickListener { v ->
            mOnItemClickListener!!.onItemClick(holder.itemView.post_like, position, 1)
            val anim = ViewAnimationUtils.createCircularReveal(holder.itemView,
                    v.x.toInt(),
                    v.y.toInt(),
                    0f, Math.hypot(holder.itemView.width.toDouble(),
                    holder.itemView.height.toDouble()).toFloat())
            anim.duration = 400
            anim.start()
        }
        holder.itemView.post_like.setOnLongClickListener { v ->
            if (!mPixivRankItem[position].isIs_bookmarked) {
                mOnItemClickListener!!.onItemLongClick(holder.itemView.post_like, position)
            }
            true
        }
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        mOnItemClickListener = onItemClickListener
    }

    override fun getItemCount(): Int = mPixivRankItem.size

    private inner class PhotoHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
