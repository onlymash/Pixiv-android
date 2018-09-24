package com.example.administrator.essim.fragments

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.ImageView
import com.example.administrator.essim.R
import com.example.administrator.essim.activities.BatchDownloadActivity
import com.example.administrator.essim.activities.MainActivity
import com.example.administrator.essim.activities.SearchActivity
import com.example.administrator.essim.activities.ViewPagerActivity
import com.example.administrator.essim.adapters.PixivAdapterGrid
import com.example.administrator.essim.interf.OnItemClickListener
import com.example.administrator.essim.network.AppApiPixivService
import com.example.administrator.essim.network.RestClient
import com.example.administrator.essim.response.*
import com.example.administrator.essim.utils.*
import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton
import com.nightonke.boommenu.Util
import kotlinx.android.synthetic.main.fragment_rank.*
import retrofit2.Call
import retrofit2.Callback
import java.util.*


/**
 * Created by Administrator on 2018/1/15 0015.
 */

class FragmentRank : BaseFragment() {

    private var isLoadingMore = false
    private var gridLayoutManager = GridLayoutManager(mContext, 2)
    private val arrayOfRankMode = arrayOf("动态", "日榜", "周榜", "月榜", "新人", "原创", "男性向", "女性向", "R-80")
    private val modeList = arrayOf("day", "week", "month", "week_rookie", "week_original", "day_male", "day_female", "day_r18")
    private var currentDataType = -1
    private var nextDataUrl: String? = null
    private var mPixivAdapter: PixivAdapterGrid? = null
    private var mIllustsBeanList = ArrayList<IllustsBean>()
    private val clickListener = { index: Int ->
        when (index) {
            0 -> if (currentDataType != -1) {
                currentDataType = -1
                getFollowUserNewIllust()
            }
            1 -> if (currentDataType != 0) {
                currentDataType = 0
                getRankList(currentDataType)
            }
            2 -> if (currentDataType != 1) {
                currentDataType = 1
                getRankList(currentDataType)
            }
            3 -> if (currentDataType != 2) {
                currentDataType = 2
                getRankList(currentDataType)
            }
            4 -> if (currentDataType != 3) {
                currentDataType = 3
                getRankList(currentDataType)
            }
            5 -> if (currentDataType != 4) {
                currentDataType = 4
                getRankList(currentDataType)
            }
            6 -> if (currentDataType != 5) {
                currentDataType = 5
                getRankList(currentDataType)
            }
            7 -> if (currentDataType != 6) {
                currentDataType = 6
                getRankList(currentDataType)
            }
            8 -> if (currentDataType != 7) {
                currentDataType = 7
                getRankList(currentDataType)
            }
            else -> {
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_rank, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        getFollowUserNewIllust()
    }

    private fun initView() {
        mRecyclerView.layoutManager = gridLayoutManager
        mRecyclerView.setOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition()
                val totalItemCount = mPixivAdapter!!.itemCount
                when {
                    lastVisibleItem >= totalItemCount - 4 && dy > 0 && !isLoadingMore -> {
                        getNextData()
                        isLoadingMore = true
                    }
                }
            }
        })
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.addItemDecoration(GridItemDecoration(
                2, DensityUtil.dip2px(mContext, 8.0f), true));
        (activity as AppCompatActivity).setSupportActionBar(mToolbar)
        mToolbar.setNavigationOnClickListener { (activity as MainActivity).drawer.openDrawer(Gravity.START, true) }
        mToolbar.title = "动态"
        mRecyclerView.layoutManager = gridLayoutManager
        mRecyclerView.setHasFixedSize(true)
        mFab.showDuration = 400
        mFab.hideDuration = 400
        mFab.frames = 60
        mFab.normalColor = resources.getColor(R.color.colorAccent)
        for (i in 0 until mFab.piecePlaceEnum.pieceNumber()) {
            val builder = TextInsideCircleButton.Builder()
                    .normalImageRes(R.drawable.ic_card_giftcard_black_24dp)
                    .imagePadding(Rect(20, 20, 20, 60))
                    .normalText(arrayOfRankMode[i])
                    .textRect(Rect(Util.dp2px(15f), Util.dp2px(42f), Util.dp2px(65f), Util.dp2px(72f)))
                    .textSize(16)
                    .listener(clickListener)
            mFab.addBuilder(builder)
        }
        no_data.setOnClickListener {
            getFollowUserNewIllust()
        }
    }

    private fun getFollowUserNewIllust() {
        mProgressbar.visibility = View.VISIBLE
        no_data.visibility = View.INVISIBLE
        val call = RestClient()
                .retrofit_AppAPI
                .create(AppApiPixivService::class.java)
                .getFollowIllusts(Common.getLocalDataSet().getString("Authorization", "")!!, "all")
        call.enqueue(object : Callback<IllustfollowResponse> {
            override fun onResponse(call: Call<IllustfollowResponse>,
                                    response: retrofit2.Response<IllustfollowResponse>) {
                if(response.body()?.illusts != null && response.body()!!.illusts.size > 0) {
                    nextDataUrl = response.body()!!.next_url
                    mIllustsBeanList.clear()
                    mIllustsBeanList.addAll(response.body()!!.illusts)
                    mPixivAdapter = PixivAdapterGrid(mIllustsBeanList, mContext)
                    mPixivAdapter!!.setOnItemClickListener(object : OnItemClickListener {
                        override fun onItemClick(view: View, position: Int, viewType: Int) {
                            when {
                                viewType == 0 -> {
                                    Reference.sIllustsBeans = mIllustsBeanList
                                    val intent = Intent(mContext, ViewPagerActivity::class.java)
                                    intent.putExtra("which one is selected", position)
                                    mContext.startActivity(intent)
                                }
                                viewType == 1 -> if (!mIllustsBeanList[position].isIs_bookmarked) {
                                    (view as ImageView).setImageResource(R.drawable.ic_favorite_white_24dp)
                                    view.startAnimation(Common.getAnimation())
                                    mIllustsBeanList[position].isIs_bookmarked = true;
                                    PixivOperate.postStarIllust(mIllustsBeanList[position].id, mContext, "public")
                                } else {
                                    (view as ImageView).setImageResource(R.drawable.no_favor)
                                    view.startAnimation(Common.getAnimation())
                                    mIllustsBeanList[position].isIs_bookmarked = false;
                                    PixivOperate.postUnstarIllust(mIllustsBeanList[position].id, mContext)
                                }
                            }
                        }

                        override fun onItemLongClick(view: View, position: Int) {
                            FragmentDialog(mContext, view, mIllustsBeanList[position]).showDialog()
                        }
                    })
                    mRecyclerView.adapter = mPixivAdapter
                    mToolbar.title = "动态"
                    mRecyclerView.visibility = View.VISIBLE
                    no_data.visibility = View.INVISIBLE
                    mProgressbar.visibility = View.INVISIBLE
                }else{
                    mProgressbar.visibility = View.INVISIBLE
                    no_data.visibility = View.VISIBLE
                    mRecyclerView.visibility = View.INVISIBLE
                    Common.showToast(mContext, getString(R.string.no_proxy));
                }
            }

            override fun onFailure(call: Call<IllustfollowResponse>, throwable: Throwable) {
                mProgressbar.visibility = View.INVISIBLE
                no_data.visibility = View.VISIBLE;
                mRecyclerView.visibility = View.INVISIBLE
                Common.showToast(mContext, getString(R.string.no_proxy));
            }
        })
    }

    private fun getRankList(dataType: Int) {
        mProgressbar.visibility = View.VISIBLE
        no_data.visibility = View.INVISIBLE
        val call = RestClient()
                .retrofit_AppAPI
                .create(AppApiPixivService::class.java)
                .getIllustRanking(Common.getLocalDataSet().getString("Authorization", "")!!,
                        modeList[dataType], null)
        call.enqueue(object : Callback<IllustRankingResponse> {
            override fun onResponse(call: Call<IllustRankingResponse>,
                                    response: retrofit2.Response<IllustRankingResponse>) {
                nextDataUrl = response.body()!!.next_url
                mIllustsBeanList.clear()
                mIllustsBeanList.addAll(response.body()!!.illusts)
                mToolbar.title = arrayOfRankMode[currentDataType + 1]
                mPixivAdapter!!.notifyDataSetChanged()
                mRecyclerView.scrollToPosition(0)
                mRecyclerView.visibility = View.VISIBLE
                mProgressbar.visibility = View.INVISIBLE
                no_data.visibility = View.INVISIBLE
            }

            override fun onFailure(call: Call<IllustRankingResponse>, throwable: Throwable) {
                mProgressbar.visibility = View.INVISIBLE
                mRecyclerView.visibility = View.INVISIBLE
                no_data.visibility = View.VISIBLE;
                Common.showToast(mContext, getString(R.string.no_proxy));
            }
        })
    }

    private fun getNextData() {
        if (nextDataUrl != null) {
            mProgressbar.visibility = View.VISIBLE
            val call = RestClient()
                    .retrofit_AppAPI
                    .create(AppApiPixivService::class.java)
                    .getNext(LocalData.getToken(), nextDataUrl!!)
            call.enqueue(object : Callback<RecommendResponse> {
                override fun onResponse(call: Call<RecommendResponse>,
                                        response: retrofit2.Response<RecommendResponse>) {
                    nextDataUrl = response.body()!!.next_url
                    mIllustsBeanList.addAll(response.body()!!.illusts)
                    mPixivAdapter!!.notifyDataSetChanged()
                    isLoadingMore = false
                    mProgressbar.visibility = View.INVISIBLE
                }

                override fun onFailure(call: Call<RecommendResponse>, throwable: Throwable) {}
            })
        } else {
            Snackbar.make(mProgressbar, getString(R.string.no_more_data), Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.main_pixiv, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val intent: Intent
        return when (item!!.itemId) {
            R.id.action_search -> {
                intent = Intent(mContext, SearchActivity::class.java)
                mContext.startActivity(intent)
                true
            }
            R.id.action_download -> {
                Reference.sIllustsBeans = mIllustsBeanList
                intent = Intent(mContext, BatchDownloadActivity::class.java)
                intent.putExtra("scroll dist", gridLayoutManager
                        .findFirstVisibleItemPosition())
                mContext.startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        when (mPixivAdapter) {
            null -> return
            else -> mPixivAdapter!!.notifyDataSetChanged()
        }
    }
}