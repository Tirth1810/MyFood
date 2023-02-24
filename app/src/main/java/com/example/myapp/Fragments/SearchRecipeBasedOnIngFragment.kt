package com.example.myapp.Fragments

import androidx.navigation.fragment.findNavController
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.Adapters.DairyAdapter
import com.example.myapp.Adapters.FruitsAdapter
import com.example.myapp.Adapters.IngAdapter
import com.example.myapp.DataClass.Dairy
import com.example.myapp.DataClass.Fruits
import com.example.myapp.DataClass.VegData
import com.example.myapp.R
import com.example.myapp.Activity.RecipeBasedOnIngridientsActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_search.*
import java.util.*
import kotlin.collections.ArrayList


class SearchRecipeBasedOnIngFragment : Fragment(), DairyAdapter.OnSelectedItems,
    FruitsAdapter.OnFruitsSelectedItems, IngAdapter.OnVegDataSelected {
    private lateinit var ingDtabaseReffrence: DatabaseReference
    val ing = ArrayList<VegData>()
    var floatingArrayList = ArrayList<String>()
    val fruits = ArrayList<Fruits>()
    val vegies = ArrayList<VegData>()
    val dairy = ArrayList<Dairy>()
    val filteringFruits = ArrayList<Fruits>()
    val filteringVegies = ArrayList<VegData>()
    val filteringDairy = ArrayList<Dairy>()

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        val back = view.findViewById<ImageView>(R.id.search_back)
        back.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment2_to_deashBoard2)
        }
        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Loading")
        progressDialog.setCancelable(false)
        progressDialog.show()
        val veg_rv = view.findViewById<RecyclerView>(R.id.veg_rv)
        veg_rv?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        ingDtabaseReffrence = FirebaseDatabase.getInstance().getReference("Vegies")
        ingDtabaseReffrence.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                progressDialog.hide()
                if (snapshot.exists()) {
                    for (svegies in snapshot.children) {

                        val vegiesCategories = svegies.getValue(VegData::class.java)
                        vegies.add(vegiesCategories!!)

                        veg_rv?.adapter =
                            IngAdapter(vegies, this@SearchRecipeBasedOnIngFragment)
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        val Fruits_rv = view.findViewById<RecyclerView>(R.id.Fruits_rv)
        Fruits_rv?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        ingDtabaseReffrence = FirebaseDatabase.getInstance().getReference("Fruits")
        ingDtabaseReffrence.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (ffruits in snapshot.children) {

                        val fruitsCategories = ffruits.getValue(Fruits::class.java)
                        fruits.add(fruitsCategories!!)

                        Fruits_rv?.adapter =
                            FruitsAdapter(fruits, this@SearchRecipeBasedOnIngFragment)
                    }

                }
                val floatingactionbtn =
                    view?.findViewById<FloatingActionButton>(R.id.floatingactionbtn)
                floatingactionbtn?.setOnClickListener {
                    if (fruits.isEmpty()) {
                        Toast.makeText(
                            requireContext(),
                            "Select The Ingredients",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val intent =
                            Intent(requireContext(), RecipeBasedOnIngridientsActivity::class.java)
                        intent.putExtra("Ing", fruits.toString())
                        startActivity(intent)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        val dairy_recyclerview = view.findViewById<RecyclerView>(R.id.dairy_rv)
        dairy_recyclerview?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)


        ingDtabaseReffrence = FirebaseDatabase.getInstance().getReference("Dairy")
        ingDtabaseReffrence.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (sdairy in snapshot.children) {

                        val dairyCategories = sdairy.getValue(Dairy::class.java)
                        dairy.add(dairyCategories!!)

                        dairy_recyclerview?.adapter =
                            DairyAdapter(dairy, this@SearchRecipeBasedOnIngFragment)
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        floatingActionClick(floatingArrayList)
        val search = view.findViewById<EditText>(R.id.Search_ing)
        search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isEmpty()) {
                    veg_rv?.adapter =
                        IngAdapter(vegies, this@SearchRecipeBasedOnIngFragment)
                    Fruits_rv?.adapter =
                        FruitsAdapter(fruits, this@SearchRecipeBasedOnIngFragment)
                    dairy_recyclerview?.adapter =
                        DairyAdapter(dairy, this@SearchRecipeBasedOnIngFragment)
                } else {
                    filter(s.toString(), vegies, fruits, dairy)
                }
            }
        })

        return view
    }

    private fun filter(
        text: String,
        vegiesFilter: ArrayList<VegData>,
        fruitsFilter: ArrayList<Fruits>,
        dairyFilter: ArrayList<Dairy>
    ) {
        filteringVegies.clear()
        for (veg in vegiesFilter) {
            if (veg.Name!!.toLowerCase().contains(text.toLowerCase())) {
                filteringVegies.add(veg)
                veg_rv?.adapter =
                    IngAdapter(filteringVegies, this@SearchRecipeBasedOnIngFragment)
            }
        }
        filteringFruits.clear()
        for (fruits in fruitsFilter) {
            if (fruits.Name!!.toLowerCase().contains(text.toLowerCase())) {
                filteringFruits.add(fruits)
                Fruits_rv?.adapter =
                    FruitsAdapter(filteringFruits, this@SearchRecipeBasedOnIngFragment)
            }
        }
        filteringDairy.clear()
        for (dairy in dairyFilter) {
            if (dairy.Name!!.toLowerCase().contains(text.toLowerCase())) {
                filteringDairy.add(dairy)
                val dairy_recyclerview = view?.findViewById<RecyclerView>(R.id.dairy_rv)
                dairy_recyclerview?.adapter =
                    DairyAdapter(filteringDairy, this@SearchRecipeBasedOnIngFragment)
            }
        }
    }

    override fun onFruitsSelect(fruitsText: String, checked: Int) {
        if (checked == 1) {
            floatingArrayList.add(fruitsText)
            floatingActionClick(floatingArrayList)
        }
        if (checked == 0) {
            floatingArrayList.remove(fruitsText)

        }
        floatingActionClick(floatingArrayList)
    }

    override fun OnVegSelecTed(vegText: String, checked: Int) {

        if (checked == 1) {
            floatingArrayList.add(vegText)
            floatingActionClick(floatingArrayList)
        }
        if (checked == 0) {
            floatingArrayList.remove(vegText)

        }
        floatingActionClick(floatingArrayList)
    }

    override fun onSelect(text: String, checked: Int) {
        if (checked == 1) {
            floatingArrayList.add(text)
            floatingActionClick(floatingArrayList)
        }
        if (checked == 0) {
            floatingArrayList.remove(text)

        }
        floatingActionClick(floatingArrayList)
    }

    @SuppressLint("SuspiciousIndentation")
    fun floatingActionClick(list: ArrayList<String>) {
        val floatingactionbtn = view?.findViewById<FloatingActionButton>(R.id.floatingactionbtn)
        floatingactionbtn?.setOnClickListener {
            if (list.isEmpty()) {
                Toast.makeText(requireContext(), "Select The Ingredients", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val intent = Intent(requireContext(), RecipeBasedOnIngridientsActivity::class.java)
                intent.putStringArrayListExtra("Ing", list)
                startActivity(intent)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.action_searchFragment2_to_deashBoard2)
                }
            })
    }
}
