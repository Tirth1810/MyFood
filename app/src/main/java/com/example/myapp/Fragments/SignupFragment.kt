package com.example.myapp.Fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.navigation.Navigation
import com.example.myapp.DataClass.Users
import com.example.myapp.R
import com.example.myapp.Validation.Validation
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_signup.*


class SignupFragment : Fragment() {

    private lateinit var signUpBtn: Button
    private lateinit var loginText: TextView
    private lateinit var fauth: FirebaseAuth
    private lateinit var dref: DatabaseReference

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_signup, container, false)
        val name = view.findViewById<TextInputEditText>(R.id.signup_name)
        val number = view.findViewById<TextInputEditText>(R.id.signuup_number)
        loginText = view.findViewById(R.id.login_text)
        val animation=AnimationUtils.loadAnimation(requireContext(),R.anim.dialog)

        loginText.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_signupFragment_to_loginFragment)
        }
        fauth = FirebaseAuth.getInstance()
        signUpBtn = view.findViewById(R.id.signup_btn)
        signUpBtn.setOnClickListener {

            if (!Validation.isValidEmail(signup_email.text.toString().trim())) {
                Toast.makeText(
                    activity,
                    "Invalid Email ",
                    Toast.LENGTH_SHORT
                ).show()
                signup_email.focusable
            } else if (!Validation.iSValidPassword(signup_password.text.toString().trim())) {
                Toast.makeText(activity, "Password Is Empty ", Toast.LENGTH_SHORT).show()
                signup_password.focusable
            } else if (!Validation.iSValidPassword(name.text.toString().trim())) {
                Toast.makeText(activity, "Name is Empty", Toast.LENGTH_SHORT).show()
                name.focusable
            } else if (!Validation.iSValidPassword(number.text.toString().trim())) {
                Toast.makeText(activity, "Number is Empty", Toast.LENGTH_SHORT).show()
                number.focusable
            } else {
                val signupName = name.text.toString().trim()
                val signupEmail = signup_email.text.toString().trim()
                val signupPassword = signup_password.text.toString().trim()
                val signupNumber = number.text.toString().trim()
                dref = FirebaseDatabase.getInstance().getReference("Users")
                val user = Users(signupName, signupEmail, signupPassword, signupNumber,null,null)
                val userid = dref.push().key!!
                val Sharedpref = requireContext().getSharedPreferences("Cred", Context.MODE_PRIVATE)
                val editor: SharedPreferences.Editor = Sharedpref.edit()
                editor.putString("Key", userid)
                editor.commit()
                dref.child(userid).setValue(user).addOnCompleteListener {
                }.addOnCanceledListener {
                    Toast.makeText(requireContext(), "cancle", Toast.LENGTH_SHORT).show()
                }
                fauth.createUserWithEmailAndPassword(
                    signup_email.text.toString().trim(),
                    signup_password.text.toString()
                )
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {

                            Navigation.findNavController(view)
                                .navigate(R.id.action_signupFragment_to_loginFragment)
                        } else {

                            Toast.makeText(
                                requireContext(),
                                task.exception.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    Navigation.findNavController(view)
                        .navigate(R.id.action_signupFragment_to_loginFragment)
                }
            })
    }
}