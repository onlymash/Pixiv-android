package com.example.administrator.essim.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.example.administrator.essim.R
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.activity_about.*


class AboutActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_about)

        val context = this
        you_are_free.setOnClickListener {
            val intent = Intent(context, FreeActivity::class.java)
            context.startActivity(intent)
        }
    }
}
