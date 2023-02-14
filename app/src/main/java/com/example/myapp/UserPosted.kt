package com.example.myapp

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class UserPosted : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_posted2)
        window.statusBarColor=Color.BLACK
    }
}