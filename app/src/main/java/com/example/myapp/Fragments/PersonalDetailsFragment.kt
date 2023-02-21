package com.example.myapp.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.myapp.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText


class PersonalDetailsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_personal_details, container, false)
        val next = view.findViewById<FloatingActionButton>(R.id.personal_details_next)
        val name = view.findViewById<TextInputEditText>(R.id.addRecipe_PD_Name)
        val email = view.findViewById<TextInputEditText>(R.id.addRecipe_PD_Email)
        val profesion = view.findViewById<TextInputEditText>(R.id.addRecipe_PD_Profesion)
        next.setOnClickListener {
            if (name.text.toString().trim().isEmpty()) {
                name.isFocusable = true
                name.error = "Enter Name"
            } else if (email.text.toString().trim().isEmpty()) {
                email.isFocusable = true
                email.error = "Enter Email"
            } else if (profesion.text.toString().trim().isEmpty()) {
                profesion.isFocusable = true
                profesion.error = "Enter Profesion"
            } else {
                val direction =
                    PersonalDetailsFragmentDirections.actionPersonalDetailsFragment3ToRecipeDetalsFragment(
                        name.text.toString().trim(),
                        email.text.toString().trim(),
                        profesion.text.toString().trim()
                    )
                findNavController().navigate(direction)
            }
        }
        return view
    }

}