package com.example.myapp.Fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.myapp.Activity.LoginSignup
import com.example.myapp.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.ByteArrayOutputStream
import java.util.*


class Profile : Fragment() {
    val mauth = FirebaseAuth.getInstance()
    private lateinit var dref: DatabaseReference
    val requestcode: Int = 1234
    var image: String? = ""

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val back = view.findViewById<ImageView>(R.id.profile_back)
        val status = view.findViewById<EditText>(R.id.editText2)
        val profileName = view.findViewById<EditText>(R.id.profile_Name)
        val logOut = view.findViewById<Button>(R.id.logout_btn)
        val phonenumber = view.findViewById<TextView>(R.id.profile_phone)
        val currentUser = mauth.currentUser
        val progileimage = view.findViewById<CircleImageView>(R.id.profile_img)
        val edit = view.findViewById<ImageView>(R.id.profileEdit)
        val editDone = view.findViewById<ImageView>(R.id.profileEditDone)
        edit.setOnClickListener {
            status.isEnabled = true
            editDone.visibility = View.VISIBLE
            edit.visibility = View.INVISIBLE
        }
        editDone.setOnClickListener {
            val userPreferences: SharedPreferences =
                requireContext().getSharedPreferences(
                    "userid",
                    Context.MODE_PRIVATE
                )
            val user = userPreferences.getString("aaaa", "")
            dref = FirebaseDatabase.getInstance().getReference("Users")
            dref.child(user.toString()).child("Status").setValue(status.text.toString())

            status.isEnabled = false
            editDone.visibility = View.INVISIBLE
            edit.visibility = View.VISIBLE
        }
        back.setOnClickListener {
            findNavController().navigate(R.id.action_profile2_to_deashBoard)
        }

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
            @SuppressLint("CommitPrefEdits")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                progressDialog.hide()
                for (ds in dataSnapshot.children) {
                    val userEmail = ds.child("email").getValue(String::class.java)
                    if (userEmail!!.contains(email.toString())) {
                        val userName = ds.child("name").getValue().toString()
                        val userNumber = ds.child("number").getValue().toString()
                        val usetimage = ds.child("imageurl").getValue().toString()
                        val statustext = ds.child("Status").getValue().toString()
                        status.setText(statustext)
                        profileName.setText(userName)
                        phonenumber.setText(userNumber)
                        if (usetimage.toUri() != null) {
                            val bytes =
                                android.util.Base64.decode(usetimage, android.util.Base64.DEFAULT)
                            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                            progileimage.setImageBitmap(bitmap)
                        }
                        val parent = ds.key
                        val keysharedPreferences: SharedPreferences =
                            requireContext().getSharedPreferences(
                                "userid",
                                Context.MODE_PRIVATE
                            )
                        val editor: SharedPreferences.Editor = keysharedPreferences.edit()
                        editor.putString("aaaa", parent)
                        editor.commit()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        userRef.addListenerForSingleValueEvent(eventListener)
        val camera = view.findViewById<FloatingActionButton>(R.id.camera)
        camera.setOnClickListener {
            val mtintent = Intent(Intent.ACTION_GET_CONTENT)
            mtintent.setType("image/*")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                activityResultLauncher.launch(mtintent)
            }

        }
        if (profile_img == null) {
            if (currentUser?.photoUrl != null) {
                Glide.with(view).load(currentUser?.photoUrl).into(progileimage)
            }
        } else {
        }

        return view


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private val activityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->

        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data!!.data
            try {
                val inputSteam = requireContext().contentResolver?.openInputStream(uri!!)
                val bitMap = BitmapFactory.decodeStream(inputSteam)
                val stream = ByteArrayOutputStream()
                bitMap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val byte = stream.toByteArray()
                image = Base64.getEncoder().encodeToString(byte)
                val progileimage = view?.findViewById<CircleImageView>(R.id.profile_img)
                progileimage?.setImageBitmap(bitMap)
                inputSteam!!.close()
                val userPreferences: SharedPreferences =
                    requireContext().getSharedPreferences(
                        "userid",
                        Context.MODE_PRIVATE
                    )
                val user = userPreferences.getString("aaaa", "")
                dref = FirebaseDatabase.getInstance().getReference("Users")
                dref.child(user.toString()).child("imageurl").setValue(image.toString())

            } catch (ex: java.lang.Exception) {
                Toast.makeText(requireContext(), ex.message.toString(), Toast.LENGTH_SHORT).show()
            }

        }
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

