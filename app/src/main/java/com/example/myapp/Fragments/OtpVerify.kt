package com.example.myapp.Fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.navigation.fragment.findNavController
import com.example.myapp.Navigation
import com.example.myapp.R
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit


class OtpVerify : Fragment() {
    val auth = FirebaseAuth.getInstance()
    lateinit var number: String
    lateinit var progress: ProgressBar

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_otp_verify, container, false)
        val verify = view.findViewById<Button>(R.id.otp_verify)
        val phone_no = view.findViewById<EditText>(R.id.phonenumber_otp)
        progress = view.findViewById(R.id.progressBar)
        verify.setOnClickListener {
            progress.visibility = View.VISIBLE
            if (phone_no.text.toString().trim().isEmpty()) {
                phone_no.error = "Enter Phone No"

            } else {
                number = phone_no.text.toString().trim()
                if (number.length == 10) {
                    number = "+91$number"
                    val options = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(number)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(requireActivity())
                        .setCallbacks(callbacks)
                        .build()
                    PhoneAuthProvider.verifyPhoneNumber(options)
                } else {
                    phone_no.error = "Enter the Correct Number"
                }
            }
        }
        return view
    }

    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {

            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {


            if (e is FirebaseAuthInvalidCredentialsException) {
            } else if (e is FirebaseTooManyRequestsException) {

            }

        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {

            findNavController().navigate(R.id.action_otpVerify2_to_otpAuth2)
        }
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = task.result?.user
                } else {

                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    }
                }
            }
    }
}