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
import android.widget.ImageView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapp.Adapters.ItemAdapter
import com.example.myapp.Adapters.Recyclerview_Adapter
import com.example.myapp.DataClass.Data
import com.example.myapp.DataClass.Itemas
import com.example.myapp.Histroy_Recipes

import com.example.myapp.Home
import com.example.myapp.R
import com.google.firebase.database.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_recycler_view.*


class RecipeFragment : Fragment(), ItemAdapter.OnItemClickListener,
    Recyclerview_Adapter.onDataItemClicklistener {
    private lateinit var firebaseReffrence: DatabaseReference

    val catagories = ArrayList<Data>()
    val Recipes = ArrayList<Itemas>()
    val Favourites=ArrayList<String>()

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_recipe, container, false)
        val ingBack = view?.findViewById<ImageView>(R.id.appCompatImageView)
        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Loading")
        progressDialog.setCancelable(false)
        progressDialog.show()
        ingBack?.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_recipeFragment_to_deashBoard)
        }
        val recyclerviewdata =
            view?.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.categoriesrecyclerview)
        recyclerviewdata?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        firebaseReffrence = FirebaseDatabase.getInstance().getReference("Categories")
        firebaseReffrence.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                progressDialog.hide()
                if (snapshot.exists()) {
                    for (categories in snapshot.children) {

                        val recipeCategories = categories.getValue(Data::class.java)
                        catagories.add(recipeCategories!!)
                        recyclerviewdata?.adapter =
                            Recyclerview_Adapter(catagories, this@RecipeFragment)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        val item_recyclerview =
            view?.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.item_rv)
        item_recyclerview?.layoutManager = LinearLayoutManager(requireContext())
        firebaseReffrence = FirebaseDatabase.getInstance().getReference("Recipe")
        firebaseReffrence.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                progressDialog.hide()
                if (snapshot.exists()) {
                    for (recipes in snapshot.children) {

                        val allRecipeS = recipes.getValue(Itemas::class.java)
                        Recipes.add(allRecipeS!!)

                        item_recyclerview?.adapter = ItemAdapter(Recipes, this@RecipeFragment)

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })




        return view
    }


    override fun OnItemClick(position: Int) {
        val clickedItems = Recipes[position]
        val NAME = clickedItems.Name.toString().trim()
        val intent = Intent(requireContext(), Home::class.java)

       intent.putExtra("Name", NAME)
        startActivity(intent)
    }

    override fun Fovourites(text: String, checked: Int) {
        if(checked==1){
            Favourites.add(text)
            val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("Text",Context.MODE_PRIVATE)
            val editor:SharedPreferences.Editor =  sharedPreferences.edit()
            val gson = Gson()
            val json:String=gson.toJson(Favourites)
            editor.putString("Fav",json)
            editor.commit()
        }
        if (checked==0){
            Favourites.remove(text)
            val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("Text",Context.MODE_PRIVATE)
            val editor:SharedPreferences.Editor =  sharedPreferences.edit()
            val gson = Gson()
            val json:String=gson.toJson(Favourites)
            editor.putString("Fav",json)
            editor.commit()
        }
    }

    override fun onitemclick(position: Int) {
    }

}