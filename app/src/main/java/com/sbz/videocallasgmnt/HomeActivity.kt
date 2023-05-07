package com.sbz.videocallasgmnt

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import com.sbz.videocallasgmnt.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private var selectedTab = 1
    private val homeFragment = HomeFragment()
    private val profileFragment = ProfileFragments()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        setStatusBarProperties()
        binding.homeLayout.setOnClickListener {
            if (selectedTab != 1) {

//                getUserName()
                setFirstFragment()

                binding.ivSettings.setImageResource(R.drawable.person_icon)
                binding.settingsLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))

                binding.tvHome.visibility = View.VISIBLE
                binding.ivHome.setImageResource(R.drawable.home_icon_selected)
                binding.homeLayout.setBackgroundResource(R.drawable.round_back_home_100)

                val scaleAnimation = ScaleAnimation(
                    0.8f,
                    1.0f,
                    1f,
                    1f,
                    Animation.RELATIVE_TO_SELF,
                    0.0f,
                    Animation.RELATIVE_TO_SELF,
                    0.0f
                )
                scaleAnimation.duration = 200
                scaleAnimation.isFillEnabled = true
                binding.homeLayout.startAnimation(scaleAnimation)

                selectedTab = 1
            }
        }

        binding.settingsLayout.setOnClickListener {
            if (selectedTab != 2) {

                setSecondFragment()
                binding.tvSettings.visibility = View.VISIBLE

                //setting the original params
                /*   binding.ivUserProfile.layoutParams = originalParams1
                   binding.hiMsg.layoutParams = originalParams2
                   binding.tvUserName.layoutParams = originalParams3*/




                binding.tvHome.visibility = View.GONE

                binding.ivHome.setImageResource(R.drawable.home_icon)
                binding.homeLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))

                binding.tvSettings.visibility = View.VISIBLE
                binding.ivSettings.setImageResource(R.drawable.person_icon_selected)
                binding.settingsLayout.setBackgroundResource(R.drawable.round_back_settings_100)

                val scaleAnimation = ScaleAnimation(
                    0.8f,
                    1.0f,
                    1f,
                    1f,
                    Animation.RELATIVE_TO_SELF,
                    0.0f,
                    Animation.RELATIVE_TO_SELF,
                    0.0f
                )
                scaleAnimation.duration = 200
                scaleAnimation.isFillEnabled = true
                binding.settingsLayout.startAnimation(scaleAnimation)

                selectedTab = 2
            }
        }
    }

    private fun setFirstFragment() {
        binding.tvSettings.visibility = View.GONE
        binding.tvAllCourse.text = "Home"
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_fragment, homeFragment)
            commit()
        }
    }

    private fun setSecondFragment() {
        binding.tvAllCourse.text = "Profile"
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_fragment, profileFragment)
            commit()
        }
    }

    private fun setStatusBarProperties() {
        val window: Window = window
        val decorView: View = window.decorView
        val wic = WindowInsetsControllerCompat(window, decorView)
        wic.isAppearanceLightStatusBars = true
        window.statusBarColor = Color.WHITE
    }
}