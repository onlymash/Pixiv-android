package com.example.administrator.essim.activities

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.administrator.essim.R


class UserDetailActivity : BaseActivity() {

    var userID: Int = 0
    var showFavoriteIllust = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.TRANSPARENT
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        val intent = intent
        userID = intent.getIntExtra("user id", 0)
        showFavoriteIllust = intent.getBooleanExtra("show favorite illust", false)
        setContentView(R.layout.activity_myself)
    }
}
