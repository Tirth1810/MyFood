package com.example.myapp

import android.app.ProgressDialog
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_filter_recipe_show.*
import kotlinx.android.synthetic.main.activity_user_posted_recipe.*


class UserPostedRecipe : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_posted_recipe)
        val data = intent.getStringExtra("Name")
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show()
        window.statusBarColor = Color.BLACK
//        database = FirebaseDatabase.getInstance().getReference("Post Recipes")
//        database.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot.) {
//                val recipeName = snapshot.
//                if (true) {
//                    val personame = snapshot.child("personName").getValue().toString()
//                    val chefemail = snapshot.child("email").getValue().toString()
//                    val recipename = snapshot.child("recipeName").getValue().toString()
//                    val recipeTime = snapshot.child("recipeTime").getValue().toString()
//                    val recipeimg = snapshot.child("recipeimg").getValue().toString()
//                    val recipeing = snapshot.child("recipeIng").getValue().toString()
//                    val recipedesc = snapshot.child("recipeDesc").getValue().toString()
//                    chef_name.text = personame.toString()
//                    chef_email.text = chefemail.toString()
//                    cooking_time.text = recipeTime.toString()
//                    recipe_name_userpost.text = recipename.toString()
//                    userposted_ing.text = recipeing.toString()
//                    userpoist_desc.text = recipedesc.toString()
//                    val bytes = android.util.Base64.decode(recipeimg, android.util.Base64.DEFAULT)
//                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
//                    userposted_image.setImageBitmap(bitmap)
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//        })
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading")
        progressDialog.setCancelable(false)
        progressDialog.show()
        val rootRef = FirebaseDatabase.getInstance().reference
        val recipessRef = rootRef.child("Post Recipes")
        val eventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                progressDialog.hide()
                var found: Boolean
                val search = data.toString()
                for (ds in dataSnapshot.children) {
                    val recipeName = ds.child("recipeName").getValue(String::class.java)
                    if (recipeName?.contains(data.toString()) == true) {
                        val personame = ds.child("personName").getValue().toString()
                        val chefemail = ds.child("email").getValue().toString()
                        val recipename = ds.child("recipeName").getValue().toString()
                        val recipeTime = ds.child("recipeTime").getValue().toString()
                        val recipeimg = ds.child("recipeimg").getValue().toString()
                        val recipeing = ds.child("recipeIng").getValue().toString()
                        val recipedesc = ds.child("recipeDesc").getValue().toString()
                        chef_name.text = personame.toString()
                        chef_email.text = chefemail.toString()
                        cooking_time.text = recipeTime.toString()
                        recipe_name_userpost.text = recipename.toString()
                        userposted_ing.text = recipeing.toString()
                        userpoist_desc.text = recipedesc.toString()
                        val bytes =
                            android.util.Base64.decode(recipeimg, android.util.Base64.DEFAULT)
                        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        userposted_image.setImageBitmap(bitmap)
                    }
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }
        recipessRef.addListenerForSingleValueEvent(eventListener)
    }
}