package com.example.myapp.Fragments

import android.annotation.SuppressLint
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
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.myapp.DataClass.Users
import com.example.myapp.LoginSignup
import com.example.myapp.MainActivity
import com.example.myapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_profile.*


class Profile : Fragment() {
    val mauth = FirebaseAuth.getInstance()
    private lateinit var dref: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val back = view.findViewById<ImageView>(R.id.profile_back)
        val status = view.findViewById<EditText>(R.id.editText2)
//        if (status.text.isEmpty()) {
//            status.error = "Enter Staus"
//        } else {
//            val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences(
//                "parent",
//                Context.MODE_PRIVATE
//            )
//            val userId = sharedPreferences.getString("key", "")
//            dref = FirebaseDatabase.getInstance().getReference("Users")
//            val parent = Users(Status = status.toString(),nu)
//            dref.child(userId.toString()).setValue(parent).addOnCompleteListener {
//            }.addOnCanceledListener {
//                Toast.makeText(requireContext(), "cancle", Toast.LENGTH_SHORT).show()
//            }
//
//        }
      back.setOnClickListener {
            findNavController().navigate(R.id.action_profile2_to_deashBoard)
        }
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
        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences(
            "Text",
            Context.MODE_PRIVATE
        )
        val email = sharedPreferences.getString("Email", "").toString()

        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Loading")
        progressDialog.setCancelable(false)
        progressDialog.show()
        val rootRef = FirebaseDatabase.getInstance().reference
        val userRef = rootRef.child("Users")
        val eventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                progressDialog.hide()
                for (ds in dataSnapshot.children) {
                    val userEmail = ds.child("email").getValue(String::class.java)
                    if (userEmail!!.contains(email.toString())) {
                        val userName = ds.child("name").getValue().toString()
                        val userNumber = ds.child("number").getValue().toString()
                        profileName.setText(userName)
                        phonenumber.setText(userNumber)
                        val parent = ds.key
                        Toast.makeText(requireContext(), parent, Toast.LENGTH_SHORT).show()
                        val keysharedPreferences: SharedPreferences =
                            requireContext().getSharedPreferences(
                                "parent",
                                Context.MODE_PRIVATE
                            )
                        val editor: SharedPreferences.Editor = keysharedPreferences.edit()
                        editor.putString("Key", parent)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        userRef.addListenerForSingleValueEvent(eventListener)
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

