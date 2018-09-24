package com.example.administrator.essim.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.administrator.essim.R
import com.example.administrator.essim.activities.SearchResultActivity
import com.example.administrator.essim.adapters.TrendingtagAdapter
import com.example.administrator.essim.interf.OnItemClickListener
import com.example.administrator.essim.network.AppApiPixivService
import com.example.administrator.essim.network.RestClient
import com.example.administrator.essim.response.TrendingtagResponse
import com.example.administrator.essim.utils.GridItemDecoration
import com.example.administrator.essim.utils.Common
import com.example.administrator.essim.utils.DensityUtil
import kotlinx.android.synthetic.main.fragment_pixiv_right.*
import retrofit2.Call
import retrofit2.Callback

class FragmentPixivRight : BaseFragment() {

    private var mPixivAdapter: TrendingtagAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pixiv_right, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        val gridLayoutManager = GridLayoutManager(mContext, 3)
        mRecyclerView.layoutManager = gridLayoutManager
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.addItemDecoration(GridItemDecoration(
                3, DensityUtil.dip2px(mContext, 6.0f), true));
        no_data.setOnClickListener {
            getHotTags()
        }
    }

    fun getHotTags() {
        mProgressbar.visibility = View.VISIBLE
        no_data.visibility = View.INVISIBLE
        val call = RestClient()
                .retrofit_AppAPI
                .create(AppApiPixivService::class.java)
                .getIllustTrendTags(Common.getLocalDataSet().getString("Authorization", "")!!)
        call.enqueue(object : Callback<TrendingtagResponse> {
            override fun onResponse(call: Call<TrendingtagResponse>, response: retrofit2.Response<TrendingtagResponse>) {
                try {
                    if(response.body()!!.trend_tags.size > 0) {
                        mPixivAdapter = TrendingtagAdapter(response.body()!!.trend_tags, mContext)
                        mPixivAdapter!!.setOnItemClickListener(object : OnItemClickListener {
                            override fun onItemClick(view: View, position: Int, viewType: Int) {
                                val intent = Intent(mContext, SearchResultActivity::class.java)
                                intent.putExtra("what is the keyword", response.body()!!.trend_tags[position].tag)
                                mContext.startActivity(intent)
                            }

                            override fun onItemLongClick(view: View, position: Int) {
                                //长按标签，可以跳转至标签封面的出处
                                Common.getSingleIllust(mProgressbar, mContext,
                                        response.body()!!.trend_tags[position].illust.id.toLong())
                            }
                        })
                        mRecyclerView.adapter = mPixivAdapter
                        no_data.visibility = View.INVISIBLE
                        mRecyclerView.visibility = View.VISIBLE
                        mProgressbar.visibility = View.INVISIBLE
                    }
                    else{
                        mRecyclerView.visibility = View.INVISIBLE
                        no_data.visibility = View.VISIBLE
                        mProgressbar.visibility = View.INVISIBLE
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    mRecyclerView.visibility = View.INVISIBLE
                    no_data.visibility = View.VISIBLE
                    mProgressbar.visibility = View.INVISIBLE
                }
            }

            override fun onFailure(call: Call<TrendingtagResponse>, throwable: Throwable) {
                no_data.visibility = View.VISIBLE
                mRecyclerView.visibility = View.INVISIBLE
                mProgressbar.visibility = View.INVISIBLE
                Common.showToast(mContext, getString(R.string.no_proxy));
            }
        })
    }
}
