package com.sbz.videocallasgmnt

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import com.sbz.videocallasgmnt.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil
            .setContentView(this, R.layout.activity_register)
        setStatusBarProperties()

        binding.btnSendOtp.setOnClickListener {
            val countryCode = binding.countryCodeHolder.selectedCountryCode
            val phoneNumber = binding.phoneNumber.text.toString()

            if (phoneNumber.isEmpty()) {
                Toast.makeText(this, "Phone Number Can't be empty!!", Toast.LENGTH_SHORT).show()
            } else if (phoneNumber.length < 10 || phoneNumber.length > 10) {
                Toast.makeText(this, "invalid Phone Number!!", Toast.LENGTH_SHORT).show()
            } else {
                launchVerifyOTP(countryCode, phoneNumber)
                Toast.makeText(this, "Otp Sent Successfully.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun launchVerifyOTP(countryCode: String?, phoneNumber: String) {
        intent = Intent(this, VerifyOTP::class.java)
        intent.putExtra("countryCode", countryCode)
        intent.putExtra("phoneNumber", phoneNumber)
        startActivity(intent)
    }

    private fun setStatusBarProperties() {
        val window: Window = window
        val decorView: View = window.decorView
        val wic = WindowInsetsControllerCompat(window, decorView)
        wic.isAppearanceLightStatusBars = true
        window.statusBarColor = Color.WHITE
    }
}