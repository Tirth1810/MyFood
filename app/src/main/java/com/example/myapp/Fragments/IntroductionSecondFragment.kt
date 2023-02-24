package com.example.myapp.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import com.example.myapp.R

class IntroductionSecondFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dot = activity?.findViewById<ImageView>(R.id.introdot)
        dot?.visibility = View.VISIBLE
        val view = inflater.inflate(R.layout.fragment_second, container, false)
        val intro_next = activity?.findViewById<TextView>(R.id.intro_next)
        val intro_back = activity?.findViewById<TextView>(R.id.intro_back)
        val getStarted = activity?.findViewById<TextView>(R.id.getstarted_btn)
        intro_back?.visibility = View.VISIBLE
        intro_next?.visibility = View.VISIBLE
        getStarted?.visibility = View.INVISIBLE
        intro_next?.setOnClickListener {
            intro_next?.visibility = View.INVISIBLE
            Navigation.findNavController(view).navigate(R.id.action_second_to_third)
        }
        intro_back?.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_second_to_first)
        }
        return view
    }


}