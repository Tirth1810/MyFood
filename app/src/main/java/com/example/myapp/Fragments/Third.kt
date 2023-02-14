package com.example.myapp.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import com.example.myapp.LoginSignup
import com.example.myapp.R

class Third : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_third, container, false)
        val dot = activity?.findViewById<ImageView>(R.id.introdot)
        dot?.visibility = View.INVISIBLE
        val intro_next = activity?.findViewById<TextView>(R.id.intro_next)
        val intro_back = activity?.findViewById<TextView>(R.id.intro_back)
        val getStarted = activity?.findViewById<TextView>(R.id.getstarted_btn)
        intro_back?.visibility=View.VISIBLE
        intro_next?.visibility=View.INVISIBLE
        getStarted?.visibility=View.VISIBLE
        intro_back?.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_third_to_second)
        }
        getStarted?.setOnClickListener {
            val intent= Intent(activity,LoginSignup::class.java)
            startActivity(intent)
        }
        return view
    }

}