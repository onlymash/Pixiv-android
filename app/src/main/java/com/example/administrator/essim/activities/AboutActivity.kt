package com.example.administrator.essim.activities

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import com.example.administrator.essim.R
import kotlinx.android.synthetic.main.activity_about.*


class AboutActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_about)

        val context = this
    }
}
