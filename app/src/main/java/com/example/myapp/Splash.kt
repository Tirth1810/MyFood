package com.example.myapp

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(1)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        setContentView(R.layout.activity_splash)
        window.statusBarColor=Color.TRANSPARENT
        val handler = android.os.Handler()
        handler.postDelayed({
            val intent= Intent(this,Introduction::class.java)
            startActivity(intent)
        }, 3000)
    }
}