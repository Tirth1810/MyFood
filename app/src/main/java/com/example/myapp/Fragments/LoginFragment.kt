package com.example.myapp.Fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.navigation.Navigation
import com.example.myapp.MainActivity
import com.example.myapp.R
import com.example.myapp.Validation
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
        val forgot = view.findViewById<TextView>(R.id.fogot_pass)
        val login_email = view.findViewById<TextInputEditText>(R.id.login_email)
        val login_password = view.findViewById<TextInputEditText>(R.id.login_password)
        val Remember: SharedPreferences = requireContext().getSharedPreferences(
            "Remember Me",
            Context.MODE_PRIVATE
        )
        val remeberMe = view?.findViewById<CheckBox>(R.id.remeberme)
        remberme(Remember, remeberMe)
        val rEmail = Remember.getString("Email", "").toString()
        login_email.setText(rEmail)
        val rPassword = Remember.getString("Password", "").toString()
        login_password.setText(rPassword)
        remeberMe!!.setOnCheckedChangeListener { buttonView, isChecked ->
            val sharedPrefeRember: SharedPreferences = requireContext().getSharedPreferences(
                "Remember Me",
                Context.MODE_PRIVATE
            )
            val editor: SharedPreferences.Editor = sharedPrefeRember.edit()
            if (isChecked) {
                remeberMe.isChecked = true
                editor.putString("Email", login_email.text.toString().trim())
                editor.putString("Password", login_password.text.toString().trim())
                editor.commit()

            } else {
                editor.putString("Email", "")
                editor.putString("Number", "")
                editor.putString("Name", "")
                editor.putString("Password", "")
                editor.commit()
            }
        }

        forgot.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.forgot_password_dialog)
            val Email = dialog.findViewById<EditText>(R.id.reset_email)
            val Reset = dialog.findViewById<Button>(R.id.resetButton)
            val cancel = dialog.findViewById<Button>(R.id.CANCEL_RESET)
            cancel.setOnClickListener {
                dialog.dismiss()
            }
            Reset.setOnClickListener {
                if (Email.text.toString().isEmpty()) {
                    Email.error = "Enter Email"
                } else if (
                    !Validation.isValidEmail(Email.text.toString()) == true
                ) {
                    Email.error = "Invalid Email"
                } else {
                    val progressDialog = ProgressDialog(requireContext())
                    progressDialog.setMessage("Loading")
                    progressDialog.setCancelable(false)
                    progressDialog.show()
                    firebaseauth.sendPasswordResetEmail(Email.text.toString())
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(requireContext(), "Email Sent", Toast.LENGTH_SHORT)
                                    .show()
                                progressDialog.dismiss()
                            }
                        }

                }
            }
            dialog.show()
        }
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
        loginBtn.setOnClickListener {
            val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences(
                "Text",
                Context.MODE_PRIVATE
            )
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString("Email", login_email.text.toString().trim())

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
            }  else {
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

    private fun remberme(remember: SharedPreferences, remeberMe: CheckBox?) {
        val checked = remember.getString("Email", "")
        if (checked == "") {
            return
        } else {
            remeberMe?.isChecked = true
        }
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val dialog = Dialog(requireContext())
                    dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
                    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    dialog.setCancelable(false)
                    dialog.setContentView(R.layout.custom_exit_dialog)
                    dialog.window?.setWindowAnimations(R.style.dialogAnimation)
                    val ok = dialog.findViewById<Button>(R.id.custom_exit_yes)
                    ok.setOnClickListener {
                        activity?.finishAffinity()
                    }
                    val cancel = dialog.findViewById<Button>(R.id.custom_exit_cancle)
                    cancel.setOnClickListener {
                        dialog.dismiss()
                    }
                    dialog.show()
                }
            })
    }
}