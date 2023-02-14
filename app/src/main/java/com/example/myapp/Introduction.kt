package com.example.myapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapp.Adapters.ViewPagerAdapter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_introduction.*

class Introduction : AppCompatActivity() {
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introduction)
        window.statusBarColor= Color.BLACK
        val user = FirebaseAuth.getInstance()
        if (user.currentUser != null) {
            val intent = Intent(this, LoginSignup::class.java)
            startActivity(intent)
        }
    }
}
