package com.example.administrator.essim.activities

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import com.ToxicBakery.viewpager.transforms.*
import com.example.administrator.essim.R
import com.example.administrator.essim.activities_re.PixivApp
import com.example.administrator.essim.fragments.FragmentPixivItem
import com.example.administrator.essim.response.Reference
import com.example.administrator.essim.response_re.IllustsBean
import kotlinx.android.synthetic.main.activity_view_pager.*
import java.util.ArrayList


class ViewPagerActivity : BaseActivity() {

    lateinit var mViewPager: ViewPager
    var allIllust = ArrayList<IllustsBean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.TRANSPARENT
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        setContentView(R.layout.activity_view_pager)

        val intent = intent
        val index = intent.getIntExtra("which one is selected", 0)
        allIllust.clear()
        allIllust.addAll(PixivApp.sIllustsBeans)

        mToolbar.setNavigationOnClickListener { finish() }
        val fragmentManager = supportFragmentManager
        mViewPager = findViewById(R.id.mViewPager)
        mViewPager.adapter = object : FragmentStatePagerAdapter(fragmentManager) {
            override fun getItem(position: Int): Fragment = FragmentPixivItem.newInstance(position)

            override fun getCount(): Int = allIllust.size
        }
        //mViewPager.setPageTransformer(true, AccordionTransformer())
        mViewPager.currentItem = index
    }

    fun changeTitle() {
        mToolbar.title = allIllust[mViewPager.currentItem].title
    }
}
