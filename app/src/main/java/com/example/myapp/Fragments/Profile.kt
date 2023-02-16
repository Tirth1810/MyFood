package com.example.myapp.Fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
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
        val profileName = view.findViewById<EditText>(R.id.profile_Name)
        val logOut = view.findViewById<Button>(R.id.logout_btn)
        logOut.setOnClickListener {
            val user = FirebaseAuth.getInstance()
            user.signOut()
            val intent = Intent(activity, LoginSignup::class.java)
            startActivity(intent)

        }
        val googlesharedPreferences =
            requireActivity().getSharedPreferences("google", Context.MODE_PRIVATE)
        val name = googlesharedPreferences.getString("Name", null)
        if (name != null) {
            profileName.setText(name)
        }
        val sharedPreferences =
            requireActivity().getSharedPreferences("Text", AppCompatActivity.MODE_PRIVATE)
        val Email = sharedPreferences.getString("Email", null)
        if (Email != null) {
            profileName.setText(Email)
        }

        return view
    }

}