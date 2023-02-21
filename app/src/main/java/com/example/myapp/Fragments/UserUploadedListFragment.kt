package com.example.myapp.Fragments

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentContainer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.Adapters.ItemAdapter
import com.example.myapp.Adapters.UserUploaded
import com.example.myapp.DataClass.Itemas
import com.example.myapp.DataClass.PostRecipe
import com.example.myapp.MainActivity
import com.example.myapp.R
import com.example.myapp.UserPostedRecipe
import com.google.firebase.database.*

class UserUploadedListFragment : Fragment(), UserUploaded.OnUserUploadedClick {
    private lateinit var dref: DatabaseReference
    val Recipes = ArrayList<PostRecipe>()

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_uploaded_list, container, false)
        val back = view.findViewById<ImageView>(R.id.userposted_back)
        back.setOnClickListener {
       startActivity(Intent(requireActivity(),MainActivity::class.java))
        }
        val recyclerview = view.findViewById<RecyclerView>(R.id.userupladedlist)
        recyclerview?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Loading")
        progressDialog.setCancelable(false)
        progressDialog.show()
        dref = FirebaseDatabase.getInstance().getReference("Post Recipes")
        dref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                progressDialog.hide()
                if (snapshot.exists()) {
                    for (recipes in snapshot.children) {

                        val allRecipeS = recipes.getValue(PostRecipe::class.java)
                        Recipes.add(allRecipeS!!)
                        recyclerview.adapter = UserUploaded(Recipes, this@UserUploadedListFragment)

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                progressDialog.hide()
            }
        })


        return view
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onUseClick(position: Int) {
        val clickedItems = Recipes[position]
        val NAME = clickedItems.recipeName.toString().trim()
        val itent=Intent(requireContext(),UserPostedRecipe::class.java)
        itent.putExtra("Name",NAME)
        startActivity(itent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    startActivity(Intent(requireActivity(),MainActivity::class.java))
                }
            })
    }
}