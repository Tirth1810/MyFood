package com.example.myapp.Activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Button
import com.example.myapp.R
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(1)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        setContentView(R.layout.activity_splash)
        val animation = AnimationUtils.loadAnimation(this, R.anim.dialog)
        textView27.startAnimation(animation)
        tv.startAnimation(animation)
        window.statusBarColor = Color.TRANSPARENT
        val handler = android.os.Handler()
        handler.postDelayed({
            val intent = Intent(this, IntroductionActivity::class.java)
            startActivity(intent)
        }, 1500)
    }

    override fun onBackPressed() {
        val dialog = Dialog(this)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_exit_dialog)
        dialog.window?.setWindowAnimations(R.style.dialogAnimation)
        val ok = dialog.findViewById<Button>(R.id.custom_exit_yes)
        ok.setOnClickListener {
            finishAffinity()
        }
        val cancel = dialog.findViewById<Button>(R.id.custom_exit_cancle)
        cancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}