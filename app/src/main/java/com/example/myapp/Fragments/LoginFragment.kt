package com.example.myapp.Fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.navigation.Navigation
import com.example.myapp.MainActivity
import com.example.myapp.R
import com.example.myapp.Validation
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_signup.*


class LoginFragment : Fragment() {
    private lateinit var signUpText: TextView
    private lateinit var loginBtn: Button
    private lateinit var firebaseauth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private companion object {
        private const val RC_SIGN_IN = 100
        private const val TAG = "GOOGLE_SIGN_IN_TAG"
    }

    @SuppressLint("MissingInflatedId", "NewApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        firebaseauth = FirebaseAuth.getInstance()
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
        val phoneauth = view?.findViewById<ImageFilterView>(R.id.login_phone)
        phoneauth?.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_otpVerify2)
        }

        val google = view.findViewById<ImageFilterView>(R.id.google_signin)
        google.setOnClickListener {
            SignInWithGoogle()
        }
        signUpText = view.findViewById(R.id.signup_text)
        signUpText.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_signupFragment)
        }

        loginBtn = view.findViewById(R.id.login_btn)
        val phoneNumber = view.findViewById<TextInputEditText>(R.id.login_number)
        val name = view.findViewById<TextInputEditText>(R.id.login_name)
        loginBtn.setOnClickListener {
            val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences(
                "Text",
                Context.MODE_PRIVATE
            )
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString("Email", login_email.text.toString().trim())
            editor.putString("Number", phoneNumber.text.toString().trim())
            editor.putString("Name", name.text.toString().trim())
            editor.commit()
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
            } else if (!Validation.iSValidPassword(phoneNumber.text.toString())) {
                Toast.makeText(activity, "Phone Number is required ", Toast.LENGTH_SHORT).show()

            } else {
                val progressDialog = ProgressDialog(requireContext())
                progressDialog.setTitle("Login")
                progressDialog.setMessage("Wait for some time")
                progressDialog.setCancelable(false)
                progressDialog.show()

                firebaseauth.signInWithEmailAndPassword(
                    login_email.text.toString().trim(),
                    login_password.text.toString()
                )
                    .addOnCompleteListener(requireActivity()) { task ->

                        if (task.isSuccessful) {
                            progressDialog.hide()
                            val intent = Intent(activity, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            progressDialog.hide()
                            Toast.makeText(
                                requireContext(),
                                task.exception.toString(),
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }

                    }


            }


        }
        return view
    }

    private fun SignInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        luncher.launch(signInIntent)
    }

    private val luncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleresult(task)
            }
        }

    private fun handleresult(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            if (account != null) {
                updateUI(account)

            }

        } else {
            Toast.makeText(requireContext(), task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseauth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {

                val goolesharedPreferences: SharedPreferences =
                    requireContext().getSharedPreferences(
                        "google",
                        Context.MODE_PRIVATE

                    )
                val editor: SharedPreferences.Editor = goolesharedPreferences.edit()
                editor.putString("Name", account.displayName)
                editor.commit()
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(requireContext(), it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

}