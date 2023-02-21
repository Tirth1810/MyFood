package com.example.myapp.Fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.myapp.LoginSignup
import com.example.myapp.MainActivity
import com.example.myapp.R
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_profile.*


class Profile : Fragment() {
    val mauth = FirebaseAuth.getInstance()

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val profileName = view.findViewById<EditText>(R.id.profile_Name)
        val logOut = view.findViewById<Button>(R.id.logout_btn)
        val phonenumber = view.findViewById<TextView>(R.id.profile_phone)
        val currentUser = mauth.currentUser
        val progileimage = view.findViewById<CircleImageView>(R.id.profile_img)
        Glide.with(view).load(currentUser?.photoUrl).into(progileimage)
        logOut.setOnClickListener {
            profileName.text.clear()
            phonenumber.setText("")
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
        val profileLoginName = sharedPreferences.getString("Name", null)
        if (profileLoginName != null) {
            profileName.setText(profileLoginName.toString())
        }
        val phone = sharedPreferences.getString("Number", null)
        if (phone != null) {

            phonenumber.text = phone.toString()
            if (phonenumber != null) {
                val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences(
                    "Otp",
                    Context.MODE_PRIVATE
                )
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString("OtpPhone", phonenumber.text.toString())
            }
        }

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.action_profile2_to_deashBoard)
                }
            })
    }
}
