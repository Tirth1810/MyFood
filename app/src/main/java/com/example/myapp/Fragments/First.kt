package com.example.myapp.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import com.example.myapp.R
import kotlinx.android.synthetic.main.activity_introduction.*


class First : Fragment() {

    private lateinit var intro_next: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dot=activity?.findViewById<ImageView>(R.id.introdot)
        val intro_back = activity?.findViewById<TextView>(R.id.intro_back)
        intro_next = activity?.findViewById<TextView>(R.id.intro_next)!!
        val getStarted = activity?.findViewById<Button>(R.id.getstarted_btn)
        dot?.visibility=View.VISIBLE
        intro_back?.visibility = View.INVISIBLE
        intro_next.visibility = View.VISIBLE
        getStarted?.visibility = View.INVISIBLE
        val view: View = inflater.inflate(R.layout.fragment_first, container, false)
        intro_next.setOnClickListener {
            intro_back?.visibility = View.VISIBLE
            Navigation.findNavController(view).navigate(R.id.action_first_to_second)
        }
        return view
    }


}