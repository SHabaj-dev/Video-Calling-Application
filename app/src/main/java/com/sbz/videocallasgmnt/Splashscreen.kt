package com.sbz.videocallasgmnt

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class Splashscreen : AppCompatActivity() {
    private val SCREEN_TIME_OUT = 2000L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)
        supportActionBar?.hide()
        window.statusBarColor = Color.WHITE

        Handler(Looper.getMainLooper()).postDelayed(
            {
                startActivity(Intent(this@Splashscreen, LoginActivity::class.java))
                finish()
            },
            SCREEN_TIME_OUT
        )
    }
}