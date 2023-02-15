package com.example.myapp

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LoginSignup : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loginsignup)
        window.statusBarColor= Color.BLACK
        val user= FirebaseAuth.getInstance()
        if(user.currentUser!=null){
            val intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

    }
    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Exit")
        builder.setMessage("Are You Sure You Want To Exit")
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton("Yes"){dialogInterface, which ->
            finishAffinity()
        }
        builder.setNeutralButton("Cancel"){dialogInterface , which ->
           dialogInterface.cancel()
        }
        builder.setNegativeButton("No"){dialogInterface, which ->
          dialogInterface.cancel()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}