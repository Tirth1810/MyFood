package com.example.myapp.Fragments

import android.app.ProgressDialog
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.myapp.R
import com.example.myapp.Validation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import kotlinx.android.synthetic.main.fragment_signup.*


class SignupFragment : Fragment() {

    private lateinit var signUpBtn: Button
    private lateinit var loginText: TextView
    private lateinit var fauth: FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_signup, container, false)
        loginText = view.findViewById(R.id.login_text)
        loginText.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_signupFragment_to_loginFragment)
        }
        fauth = FirebaseAuth.getInstance()
        signUpBtn = view.findViewById(R.id.signup_btn)
        signUpBtn.setOnClickListener {

            if (!Validation.isValidEmail(signup_email.text.toString().trim())) {
                Toast.makeText(
                    activity,
                    "This Field Is Empty Or Invalid Email ",
                    Toast.LENGTH_SHORT
                ).show()
                signup_email.focusable
            } else if (!Validation.iSValidPassword(signup_password.text.toString().trim())) {
                Toast.makeText(activity, "This Field Is Empty ", Toast.LENGTH_SHORT).show()
                signup_password.focusable
            } else {
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