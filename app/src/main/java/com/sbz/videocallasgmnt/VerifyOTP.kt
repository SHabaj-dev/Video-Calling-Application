package com.sbz.videocallasgmnt

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.sbz.videocallasgmnt.databinding.ActivityVerifyOtpBinding
import java.util.concurrent.TimeUnit

class VerifyOTP : AppCompatActivity() {
    private lateinit var binding: ActivityVerifyOtpBinding
    private lateinit var countryCode: String
    private lateinit var phoneNumber: String
    private lateinit var fullPhoneNumber: String
    private lateinit var auth: FirebaseAuth
    private lateinit var verificationId: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil
            .setContentView(this, R.layout.activity_verify_otp)
        setStatusBarProperties()

        val extras = intent.extras
        countryCode = extras!!.getString("countryCode").toString()
        phoneNumber = extras.getString("phoneNumber").toString()
        fullPhoneNumber = "+$countryCode$phoneNumber"
        binding.tvPhoneNumber.text = fullPhoneNumber
        verifyOTP()

        binding.btnVerifyOtp.setOnClickListener {
            val otpCode = binding.pinView.text.toString().trim()
            if (otpCode.isEmpty()) {
                Toast.makeText(this, "Please enter OTP code", Toast.LENGTH_SHORT).show()
            } else {
                val credential = PhoneAuthProvider.getCredential(verificationId, otpCode)
                signInWithPhoneAuthCredential(credential)
            }
        }

//        binding.btnResendOtp.setOnClickListener {
//            resendVerificationCode(fullPhoneNumber, resendToken)
//        }
    }

    private fun setStatusBarProperties() {
        val window: Window = window
        val decorView: View = window.decorView
        val wic = WindowInsetsControllerCompat(window, decorView)
        wic.isAppearanceLightStatusBars = true
        window.statusBarColor = Color.WHITE
    }

    private fun verifyOTP() {
        auth = FirebaseAuth.getInstance()
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(fullPhoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(exception: FirebaseException) {
            Toast.makeText(this@VerifyOTP, exception.message, Toast.LENGTH_SHORT).show()
            Log.d("ERROR_HAI_BHAI", exception.message.toString())
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            super.onCodeSent(verificationId, token)
            this@VerifyOTP.verificationId = verificationId
            resendToken = token
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Verification successful, do something here
//                    Toast.makeText(this, "Verification successful!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@VerifyOTP, MainActivity::class.java))
                    finish()
                } else {
                    // Verification failed, show error message
                    Toast.makeText(this, "Verification failed!", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun resendVerificationCode(
        phoneNumber: String,
        token: PhoneAuthProvider.ForceResendingToken
    ) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .setForceResendingToken(token)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}

