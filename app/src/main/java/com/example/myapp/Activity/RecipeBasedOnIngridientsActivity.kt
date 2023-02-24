package com.example.myapp.Activity

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.Adapters.IngredientsBaseRecipeAdapter
import com.example.myapp.DataClass.IngredientsBasedDataClass
import com.example.myapp.DataClass.Itemas
import com.example.myapp.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_recipe_based_on_ingridients.*

class RecipeBasedOnIngridientsActivity : AppCompatActivity(),
    IngredientsBaseRecipeAdapter.RecipeBasedOnIng {
    private val ingredientsBasedRecipeList = ArrayList<IngredientsBasedDataClass>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_based_on_ingridients)
        window.statusBarColor = Color.BLACK
        val recipebasedrecyclerview = findViewById<RecyclerView>(R.id.recipe_BasedOn_Ingredients)
        recipebasedrecyclerview?.layoutManager = LinearLayoutManager(this)
        val Ing = intent.getStringArrayListExtra("Ing")
        var list = ArrayList<Itemas>()
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading")
        progressDialog.setCancelable(false)
        progressDialog.show()
        FirebaseDatabase.getInstance().getReference("Ingredients Based Recipe")
            .addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        progressDialog.hide()
                        ingredientsBasedRecipeList.clear()
                        if (snapshot.exists()) {
                            for (data in snapshot.children) {
                                val recipes = data.getValue(IngredientsBasedDataClass::class.java)
                                recipes.let { item ->
                                    for (i in 0 until Ing!!.size) {
                                        if (item?.Ingredients!!.contains(Ing?.get(i)!!)) {
                                            ingredientsBasedRecipeList.add(item!!)
                                            recipe_BasedOn_Ingredients.adapter =
                                                IngredientsBaseRecipeAdapter(
                                                    ingredientsBasedRecipeList,
                                                    this@RecipeBasedOnIngridientsActivity
                                                )
                                        }
                                    }
                                }


                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })


    }


    override fun OnRecipeClick(position: Int) {
        val selecteditem = ingredientsBasedRecipeList[position]
        val Name = selecteditem.Name.toString().trim()
        intent = Intent(this, FilterRecipeShowActivity::class.java)
        intent.putExtra("RecipesName", Name)
        startActivity(intent)
    }
}

