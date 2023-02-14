package com.example.myapp

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_filter_recipe_show.*
import kotlinx.android.synthetic.main.activity_home.*

class FilterRecipeShow : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter_recipe_show)
        window.statusBarColor= Color.BLACK
        val recipeName = intent.getStringExtra("RecipesName")
        database = FirebaseDatabase.getInstance().getReference("Recipes")
        database.child(intent.getStringExtra("RecipesName").toString()).get().addOnSuccessListener {
            if (it.exists()) {
                val recipenaname = it.child("Name")
                val recipeing = it.child("Ingredients")
                val recipedesc = it.child("Description")
                val recipeimg = it.child("imageurl").value.toString()
                filterrecipe_name.text = recipenaname.value.toString()
                filterrecipe_ing.text = recipeing.value.toString()
                filterrecipe_desc.text = recipedesc.value.toString()
                Glide.with(this).load(recipeimg).into(filterrecipe_image)
            }
        }

    }
}