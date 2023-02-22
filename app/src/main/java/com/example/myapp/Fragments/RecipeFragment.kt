package com.example.myapp.Fragments

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapp.Adapters.ItemAdapter
import com.example.myapp.Adapters.Recyclerview_Adapter
import com.example.myapp.DataClass.Data
import com.example.myapp.DataClass.Itemas

import com.example.myapp.Activity.Home
import com.example.myapp.R
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_deash_board.*


class RecipeFragment : Fragment(), ItemAdapter.OnItemClickListener,
    Recyclerview_Adapter.onDataItemClicklistener {
    private lateinit var firebaseReffrence: DatabaseReference

    val catagories = ArrayList<Data>()
    val filteredCategories = ArrayList<Data>()
    val Recipes = ArrayList<Itemas>()
    val filterList = ArrayList<Itemas>()
    val catfilter = ArrayList<Itemas>()

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
        item_recyclerview?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        firebaseReffrence = FirebaseDatabase.getInstance().getReference("Recipe")
        firebaseReffrence.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                progressDialog.hide()
                if (snapshot.exists()) {
                    val sharedPreferences =
                        requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

                    var selected = sharedPreferences.getStringSet("selectedItems", null)

                        for (recipes in snapshot.children) {

                            val allRecipeS = recipes.getValue(Itemas::class.java)
                            Recipes.add(allRecipeS!!)
                            if (selected != null) {
                                Recipes.map {
                                    for (i in 0 until selected!!.size) {
                                        if (it.Name!! == selected!!.elementAt(i)) {
                                            it.selected = true
                                        }
                                    }
                                }
                            }


                    }
                    filterList.addAll(Recipes)
                    item_recyclerview?.adapter = ItemAdapter(Recipes, this@RecipeFragment)

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        val search = view.findViewById<EditText>(R.id.searchRecipe)
        search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isEmpty()) {
                    item_recyclerview?.adapter = ItemAdapter(Recipes, this@RecipeFragment)
                } else {
                    Filter(s.toString(), Recipes)
                }
            }
        })




        return view
    }


    private fun Filter(Search: String, Recipes: ArrayList<Itemas>) {
        filterList.clear()
        for (Recipe in Recipes) {
            if (Recipe.Name!!.toLowerCase()!!.contains(Search.toLowerCase())) {
                filterList.add(Recipe)
                val item_recyclerview =
                    view?.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.item_rv)
                item_recyclerview?.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                item_recyclerview?.adapter = ItemAdapter(filterList, this@RecipeFragment)

            }
        }
    }

    override fun onitemclick(position: Int) {
        val select = catagories[position]

        if (select.Name == "Home") {
            val item_recyclerview =
                view?.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.item_rv)
            item_recyclerview?.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            item_recyclerview?.adapter = ItemAdapter(filterList, this@RecipeFragment)

        }
        filterList.clear()
        for (sRecipe in Recipes) {
            if (sRecipe.Category!!.contains(select.Name.toString())) {
                filterList.add(sRecipe)
                val item_recyclerview =
                    view?.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.item_rv)
                item_recyclerview?.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                item_recyclerview?.adapter = ItemAdapter(filterList, this@RecipeFragment)
            }

        }
    }

    override fun OnItemClick(position: Int) {
        val clickedItems = filterList[position]
        val NAME = clickedItems.Name.toString().trim()
        val intent = Intent(requireContext(), Home::class.java)

        intent.putExtra("Name", NAME)
        startActivity(intent)
    }

    override fun Fovourites(text: String, checked: Int, checkbox: CheckBox) {
        if (checked == 1) {
        }
        if (checked == 0) {
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.action_recipeFragment_to_deashBoard2)
                }
            })
    }
}
