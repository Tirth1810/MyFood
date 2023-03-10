package com.example.myapp.Activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.myapp.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_home.*

class   HomeActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        window.statusBarColor= Color.BLACK
        val ss: String = intent.getStringExtra("Name").toString()
        database = FirebaseDatabase.getInstance().getReference("Recipes")
        database.child(intent.getStringExtra("Name").toString()).get().addOnSuccessListener {
            if (it.exists()) {
                val recipenaname = it.child("Name")
                val recipeing = it.child("Ingredients")
                val recipedesc = it.child("Description")
                val recipeimg = it.child("imageurl").value.toString()
                recipe_name.text = recipenaname.value.toString()
                recipe_ing.text = recipeing.value.toString()
                recipe_desc.text = recipedesc.value.toString()
                Glide.with(this).load(recipeimg).into(recipe_image)

            }

        }

    }
}