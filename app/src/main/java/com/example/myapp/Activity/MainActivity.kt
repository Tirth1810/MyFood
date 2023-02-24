package com.example.myapp.Activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.ACTION_SETTINGS
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.myapp.R
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private var readStoragePermissionGranted = false
    private var CameraPermissionGranted = false
    private var backPressedOnce = false
    val mauth = FirebaseAuth.getInstance()
    private lateinit var dref: DatabaseReference
    val requestcode: Int = 1234
    var image: String? = ""


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.statusBarColor = Color.BLACK
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                readStoragePermissionGranted =
                    permissions[android.Manifest.permission.READ_EXTERNAL_STORAGE]
                        ?: readStoragePermissionGranted
                CameraPermissionGranted =
                    permissions[android.Manifest.permission.READ_EXTERNAL_STORAGE]
                        ?: CameraPermissionGranted

                if (readStoragePermissionGranted == false) {
                    val dialog = Dialog(this)
                    dialog.window?.requestFeature(Window.FEATURE_NO_TITLE) // if you have blue line on top of your dialog, you need use this code
                    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    dialog.setCancelable(false)
                    dialog.setContentView(R.layout.customdialog)
                    dialog.window?.setWindowAnimations(R.style.dialogAnimation)
                    val ok = dialog.findViewById<Button>(R.id.custom_dialog_ok)
                    ok.setOnClickListener {
                        val i = Intent(ACTION_SETTINGS)
                        startActivity(i)
                    }
                    val cancel = dialog.findViewById<ImageView>(R.id.custom_dialog_cancle)
                    cancel.setOnClickListener {
                        readStoragePermissionGranted = true
                        Toast.makeText(this, "Grant Permission From Settings", Toast.LENGTH_SHORT)
                            .show()
                        dialog.dismiss()
                    }
                    dialog.show()
                }
                if (CameraPermissionGranted == false) {
                    val dialog = Dialog(this)
                    dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
                    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    dialog.setCancelable(false)
                    dialog.setContentView(R.layout.customdialog)
                    dialog.window?.setWindowAnimations(R.style.dialogAnimation)
                    val ok = dialog.findViewById<Button>(R.id.custom_dialog_ok)
                    ok.setOnClickListener {
                        val i = Intent(ACTION_SETTINGS)
                        startActivity(i)
                    }
                    val cancel = dialog.findViewById<ImageView>(R.id.custom_dialog_cancle)
                    cancel.setOnClickListener {
                        CameraPermissionGranted = true
                        Toast.makeText(this, "Grant Permission From Settings", Toast.LENGTH_SHORT)
                            .show()
                        dialog.dismiss()
                    }
                    dialog.show()
                }

            }
        requestPermission()
        val navDrawer = findViewById<DrawerLayout>(R.id.main_nav)

        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        val headerView = navigationView.getHeaderView(0)
        val uername = headerView.findViewById<View>(R.id.nav_email) as TextView
        val sortPhoto = headerView.findViewById<CircleImageView>(R.id.circleImageView9)
        val profilephoto = headerView.findViewById<ImageView>(R.id.circleImageView8)
        profilephoto.setOnClickListener {
            val mtintent = Intent(Intent.ACTION_GET_CONTENT)
            mtintent.setType("image/*")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                activityResultLauncher.launch(mtintent)
            }

        }

        val currentUser = FirebaseAuth.getInstance().currentUser
        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    findNavController(R.id.fragmentContainerView).navigate(R.id.deashBoard)
                    navDrawer.closeDrawer(GravityCompat.START);
                }
                R.id.searchFragment -> {
                    findNavController(R.id.fragmentContainerView).navigate(R.id.searchFragment2)
                    navDrawer.closeDrawer(GravityCompat.START);
                }
                R.id.nav_recipes -> {
                    findNavController(R.id.fragmentContainerView).navigate(R.id.recipeFragment)
                    navDrawer.closeDrawer(GravityCompat.START);
                }
                R.id.nav_profile -> {
                    findNavController(R.id.fragmentContainerView).navigate(R.id.profile2)
                    navDrawer.closeDrawer(GravityCompat.START);
                }
                R.id.nav_add -> {
                    findNavController(R.id.fragmentContainerView).navigate(R.id.personalDetailsFragment2)
                    navDrawer.closeDrawer(GravityCompat.START);
                }
            }
            true
        }
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(
            "Text",
            Context.MODE_PRIVATE
        )
        val profile = sharedPreferences.getString("Email", "").toString()


        val rootRef = FirebaseDatabase.getInstance().reference
        val userRef = rootRef.child("Users")
        val eventListener: ValueEventListener = object : ValueEventListener {
            @SuppressLint("CommitPrefEdits")
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (ds in dataSnapshot.children) {
                    val userEmail = ds.child("email").getValue(String::class.java)
                    if (userEmail!!.contains(profile.toString())) {
                        val name = ds.child("name").getValue().toString()
                        val usetimage = ds.child("imageurl").getValue().toString()
                        val coverPhoto = ds.child("CoverPhoto").getValue().toString()
                        uername.text = name
                        if (usetimage.toUri() != null) {
                            val bytes =
                                android.util.Base64.decode(usetimage, android.util.Base64.DEFAULT)
                            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                            sortPhoto.setImageBitmap(bitmap)

                        }
                        if (coverPhoto.toUri() != null) {
                            val bytes =
                                android.util.Base64.decode(coverPhoto, android.util.Base64.DEFAULT)
                            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                            profilephoto.setImageBitmap(bitmap)

                        }
                        val parent = ds.key
                        val keyvaluesharedPreferences: SharedPreferences =
                            this@MainActivity.getSharedPreferences(
                                "userid",
                                Context.MODE_PRIVATE
                            )
                        val editor: SharedPreferences.Editor = keyvaluesharedPreferences.edit()
                        editor.putString("this", parent)
                        editor.commit()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        userRef.addListenerForSingleValueEvent(eventListener)

    }

//    override fun onBackPressed() {
//        val navDrawer = findViewById<DrawerLayout>(R.id.main_nav)
//        navDrawer.closeDrawer(GravityCompat.START);
//
//    }

    private fun requestPermission() {
        readStoragePermissionGranted = ContextCompat.checkSelfPermission(
            this, android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        CameraPermissionGranted = ContextCompat.checkSelfPermission(
            this, android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        val permissionList: MutableList<String> = ArrayList()
        if (!readStoragePermissionGranted) {
            permissionList.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (!CameraPermissionGranted) {
            permissionList.add(android.Manifest.permission.CAMERA)
        }
        if (permissionList.isNotEmpty()) {
            permissionLauncher.launch(permissionList.toTypedArray())
        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private val activityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->

        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data!!.data
            try {
                val inputSteam = this.contentResolver?.openInputStream(uri!!)
                val bitMap = BitmapFactory.decodeStream(inputSteam)
                val stream = ByteArrayOutputStream()
                bitMap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val byte = stream.toByteArray()
                image = Base64.getEncoder().encodeToString(byte)
                val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
                val headerView = navigationView.getHeaderView(0)
                val profilephoto = headerView.findViewById<ImageView>(R.id.circleImageView8)
                profilephoto?.setImageBitmap(bitMap)
                inputSteam!!.close()
                val keyvaluePreferences: SharedPreferences =
                    this.getSharedPreferences(
                        "userid",
                        Context.MODE_PRIVATE
                    )
                val user = keyvaluePreferences.getString("this", "")
                dref = FirebaseDatabase.getInstance().getReference("Users")
                dref.child(user.toString()).child("CoverPhoto").setValue(image.toString())

            } catch (ex: java.lang.Exception) {
                Toast.makeText(this, ex.message.toString(), Toast.LENGTH_SHORT).show()
            }

        }
    }

}