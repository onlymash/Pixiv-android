package com.example.administrator.essim.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.example.administrator.essim.R
import kotlinx.android.synthetic.main.activity_about.*


class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_about)

        val context = this
        getDonaterList.setOnClickListener {
            val intent = Intent(context, AboutDonationActivity::class.java)
            intent.putExtra("dataType", "donation")
            context.startActivity(intent)
        }
        getUsedLibraries.setOnClickListener {
            val intent = Intent(context, AboutDonationActivity::class.java)
            intent.putExtra("dataType", "libraries")
            context.startActivity(intent)
        }
    }
}
