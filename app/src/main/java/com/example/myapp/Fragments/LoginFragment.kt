package com.example.myapp.Fragments

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.myapp.MainActivity
import com.example.myapp.R
import com.example.myapp.Validation
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_signup.*


class LoginFragment : Fragment() {
    private lateinit var signUpText: TextView
    private lateinit var loginBtn: Button
    private lateinit var firebaseauth: FirebaseAuth
    @SuppressLint("MissingInflatedId", "NewApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_login, container, false)

        signUpText = view.findViewById(R.id.signup_text)
        signUpText.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_signupFragment)
        }
        firebaseauth= FirebaseAuth.getInstance()
        loginBtn = view.findViewById(R.id.login_btn)
        loginBtn.setOnClickListener {
            if (!Validation.isValidEmail(login_email.text.toString())) {
                Toast.makeText(
                    activity,
                    "This Field Is Empty Or Invalid Email ",
                    Toast.LENGTH_SHORT
                ).show()
                login_email.focusable
            } else if (!Validation.iSValidPassword(login_password.text.toString())) {
                Toast.makeText(activity, "This Field Is Empty ", Toast.LENGTH_SHORT).show()
                login_password.focusable
            } else {
                val progressDialog = ProgressDialog(requireContext())
                progressDialog.setTitle("Login")
                progressDialog.setMessage("Wait for some time")
                progressDialog.setCancelable(false)
                progressDialog.show()
                firebaseauth.signInWithEmailAndPassword(login_email.text.toString().trim(),login_password.text.toString())
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            progressDialog.hide()
                            val intent = Intent(activity, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            progressDialog.hide()
                            Toast.makeText(requireContext(), "Login Fail", Toast.LENGTH_SHORT).show()
                        }
                    }


            }


        }
        return view
    }

}