package com.example.myapp

import android.util.Patterns

object Validation {
    fun isValidEmail(email:String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    fun iSValidPassword(password:String):Boolean{
        return password.isNotEmpty()

    }
}