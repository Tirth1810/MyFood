package com.example.myapp.Fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import com.example.myapp.Histroy_Recipes
import com.example.myapp.R
import com.example.myapp.UserPosted

class DeashBoard : Fragment() {


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_deash_board, container, false)
        val search = view.findViewById<CardView>(R.id.Search)
        val recipe = view.findViewById<CardView>(R.id.recipes)
        val profile = view.findViewById<CardView>(R.id.profile)
        val addRecipe = view.findViewById<CardView>(R.id.addRecipe)
        val useruploaded = view.findViewById<CardView>(R.id.userposted)
        val history = view.findViewById<CardView>(R.id.history)
        profile.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_deashBoard_to_profile2)
        }
        search.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_deashBoard_to_searchFragment22)
        }
        useruploaded.setOnClickListener {
            val intent = Intent(requireContext(), UserPosted::class.java)
            startActivity(intent)
        }
        recipe.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_deashBoard_to_recipeFragment)
        }
        addRecipe.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_deashBoard_to_addRecipeFragment2)
        }
       history.setOnClickListener {
           val intent = Intent(requireContext(), Histroy_Recipes::class.java)
           startActivity(intent)
        }
        return view
    }

}