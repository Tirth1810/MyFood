package com.example.myapp.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapp.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_introduction.*

class IntroductionActivity : AppCompatActivity() {
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introduction)
        window.statusBarColor= Color.BLACK
        val user = FirebaseAuth.getInstance()
        if (user.currentUser != null) {
            val intent = Intent(this, LoginSignupActivity::class.java)
            startActivity(intent)
        }
    }
}
