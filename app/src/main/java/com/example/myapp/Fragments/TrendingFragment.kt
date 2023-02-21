package com.example.myapp.Fragments

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.Adapters.Recyclerview_Adapter
import com.example.myapp.Adapters.TrendingAdapter
import com.example.myapp.DataClass.Data
import com.example.myapp.DataClass.TrendingDataClass
import com.example.myapp.R
import com.google.firebase.database.*

class TrendingFragment : Fragment() {
    private lateinit var firebaseReffrence: DatabaseReference
    val Trending = ArrayList<TrendingDataClass>()

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_trending, container, false)
        val back = view.findViewById<ImageView>(R.id.trending_back)
        back.setOnClickListener {
            findNavController().navigate(R.id.action_trendingFragment_to_deashBoard)
        }
        val trending = view.findViewById<RecyclerView>(R.id.trending_recyclerview)
        trending?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Loading")
        progressDialog.setCancelable(false)
        progressDialog.show()
        firebaseReffrence = FirebaseDatabase.getInstance().getReference("Trending")
        firebaseReffrence.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                progressDialog.hide()
                if (snapshot.exists()) {
                    for (trecipes in snapshot.children) {

                        val recipeCategories = trecipes.getValue(TrendingDataClass::class.java)
                        Trending.add(recipeCategories!!)
                        trending?.adapter =
                            TrendingAdapter(Trending)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.action_trendingFragment_to_deashBoard)
                }
            })
    }
}