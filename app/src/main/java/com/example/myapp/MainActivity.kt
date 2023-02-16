package com.example.myapp

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
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.google.android.material.navigation.NavigationBarView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private var readStoragePermissionGranted = false
    private var CameraPermissionGranted = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.statusBarColor = Color.BLACK
        permissionLauncher=registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){permissions->
            readStoragePermissionGranted=permissions[android.Manifest.permission.READ_EXTERNAL_STORAGE]?:readStoragePermissionGranted
            CameraPermissionGranted=permissions[android.Manifest.permission.READ_EXTERNAL_STORAGE]?:CameraPermissionGranted
            if (readStoragePermissionGranted==false){
                val dialog = Dialog(this)
                dialog.window?.requestFeature(Window.FEATURE_NO_TITLE) // if you have blue line on top of your dialog, you need use this code
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.setCancelable(false)
                dialog.setContentView(R.layout.customdialog)
                dialog.window?.setWindowAnimations(R.style.dialogAnimation)
                val ok=dialog.findViewById<Button>(R.id.custom_dialog_ok)
                ok.setOnClickListener {
                    val i = Intent(ACTION_SETTINGS)
                    startActivity(i)
                }
                val cancel=dialog.findViewById<ImageView>(R.id.custom_dialog_cancle)
                cancel.setOnClickListener {
                    readStoragePermissionGranted=true
                    Toast.makeText(this,"Grant Permission From Settings",Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                dialog.show()
            }
            }
        requestPermission()
    }

    override fun onBackPressed() {
        val dialog = Dialog(this)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE) // if you have blue line on top of your dialog, you need use this code
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_exit_dialog)
        dialog.window?.setWindowAnimations(R.style.dialogAnimation)
        val ok=dialog.findViewById<Button>(R.id.custom_exit_yes)
        ok.setOnClickListener {
            finishAffinity()
        }
        val cancel=dialog.findViewById<ImageView>(R.id.custom_exit_cancle)
        cancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

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
        if(permissionList.isNotEmpty()){
            permissionLauncher.launch(permissionList.toTypedArray())
        }
    }
}