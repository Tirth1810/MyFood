package com.example.myapp.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myapp.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText


class RecipeDetailsFragment : Fragment() {

    private val args: RecipeDetailsFragmentArgs by navArgs()

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Toast.makeText(

            requireContext(),
            args.name + args.email + args.profesion,
            Toast.LENGTH_SHORT
        ).show()
        val view = inflater.inflate(R.layout.fragment_recipe_detals, container, false)
        val next = view.findViewById<FloatingActionButton>(R.id.recipedetails_next)
        val name = view.findViewById<TextInputEditText>(R.id.addRecipe_RecipeDetails_Name)
        val ingredients = view.findViewById<TextInputEditText>(R.id.addRecipe_Recipedetails_Ing)
        val description = view.findViewById<TextInputEditText>(R.id.addRecipe_Recipedeatails_Desc)
        val time = view.findViewById<TextInputEditText>(R.id.addRecipe_Recipedeatails_Time)

        next.setOnClickListener {
            if (name.text.toString().trim().isEmpty()) {
                name.error = "Enter Name"
                name.isFocusable = true
            } else if (ingredients.text.toString().trim().isEmpty()) {
                ingredients.error = "Enter Ingredients"
                ingredients.isFocusable = true
            } else if (description.text.toString().trim().isEmpty()) {
                description.error = "Enter Ingredients"
                description.isFocusable = true
            } else if (time.text.toString().trim().isEmpty()) {
                time.error = "Enter Ingredients"
                time.isFocusable = true
            } else {
                val personalName = args.name
                val directions =
                    RecipeDetailsFragmentDirections.actionRecipeDetalsFragmentToRecipeImageFragment(
                        name.text.toString().trim(),
                        ingredients.text.toString().trim(),
                        description.text.toString().trim(),
                        time.text.toString().trim(),
                        personalName,
                        args.email.toString(),
                        args.profesion

                    )
                findNavController().navigate(directions)
            }
        }
        val back = view.findViewById<FloatingActionButton>(R.id.recipe_details_back)

        back.setOnClickListener {

        }
        return view
    }

}