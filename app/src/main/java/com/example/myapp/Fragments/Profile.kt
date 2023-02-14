package com.example.myapp.Fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
import com.example.myapp.LoginSignup
import com.example.myapp.MainActivity
import com.example.myapp.R
import com.google.firebase.auth.FirebaseAuth


class Profile : Fragment() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val logOut = view.findViewById<Button>(R.id.logout_btn)
        logOut.setOnClickListener {
            val user = FirebaseAuth.getInstance()
            user.signOut()
            val intent = Intent(activity, LoginSignup::class.java)
            startActivity(intent)
        }
        return view
    }

}