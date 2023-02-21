package com.example.myapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.ACTION_BLUETOOTH_SETTINGS
import android.provider.Settings.ACTION_SETTINGS
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.jar.Attributes.Name

class MainActivity : AppCompatActivity() {
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private var readStoragePermissionGranted = false
    private var CameraPermissionGranted = false
    private var backPressedOnce = false

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
        val email = headerView.findViewById<View>(R.id.nav_email) as TextView
        val photo = headerView.findViewById<CircleImageView>(R.id.circleImageView8)
        val currentUser = FirebaseAuth.getInstance().currentUser
        Glide.with(this).load(currentUser?.photoUrl).into(photo)

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
        val sharedPreferences = getSharedPreferences("Text", MODE_PRIVATE)
        val Email = sharedPreferences.getString("Email", null)
        if (Email != null) {
            email.text = Email
        }
        val googleSharedPreferences = getSharedPreferences("google", MODE_PRIVATE)
        val Name = googleSharedPreferences.getString("Name", null)
        if (Name != null) {
            email.text = Name
        }

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
}