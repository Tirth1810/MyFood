package com.example.myapp.Fragments

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.Adapters.UserUploaded
import com.example.myapp.DataClass.PostRecipe
import com.example.myapp.Activity.MainActivity
import com.example.myapp.R
import com.example.myapp.Activity.UserPostedRecipeShowActivity
import com.google.firebase.database.*

class UserUploadedListFragment : Fragment(), UserUploaded.OnUserUploadedClick {
    private lateinit var dref: DatabaseReference
    val Recipes = ArrayList<PostRecipe>()
    val filter = ArrayList<PostRecipe>()

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_uploaded_list, container, false)
        val back = view.findViewById<ImageView>(R.id.userposted_back)
        back.setOnClickListener {
            startActivity(Intent(requireActivity(), MainActivity::class.java))
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
                    filter.addAll(Recipes)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                progressDialog.hide()
            }
        })
        val searchChef = view.findViewById<EditText>(R.id.chefSearch)
        searchChef.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isEmpty()) {
                    filter.clear()
                    filter.addAll(Recipes)
                    recyclerview.adapter = UserUploaded(filter, this@UserUploadedListFragment)
                } else {
                    Filter(s.toString(), Recipes)
                }
            }
        })

        return view
    }

    private fun Filter(Text: String, recipes: ArrayList<PostRecipe>) {
        filter.clear()
        for (FilterRecipe in recipes) {
            if (FilterRecipe.personName!!.toLowerCase()!!.contains(Text.toLowerCase())) {
                filter.add(FilterRecipe)
                val recyclerview = view?.findViewById<RecyclerView>(R.id.userupladedlist)
                recyclerview?.adapter = UserUploaded(filter, this@UserUploadedListFragment)
            }
        }

    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onUseClick(position: Int) {
        val clickedItems = filter[position]
        val NAME = clickedItems.recipeName.toString().trim()
        val itent = Intent(requireContext(), UserPostedRecipeShowActivity::class.java)
        itent.putExtra("Name", NAME)
        startActivity(itent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    startActivity(Intent(requireActivity(), MainActivity::class.java))
                }
            })
    }
}