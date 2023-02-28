package com.example.myapp.Fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapp.Adapters.CartAdapter
import com.example.myapp.DataClass.CartDataClass
import com.example.myapp.R
import com.example.myapp.databinding.FragmentCartBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging

class CartFragment : Fragment(), CartAdapter.Total {
    private lateinit var binding: FragmentCartBinding
    val name = ArrayList<CartDataClass>()
    val price = ArrayList<CartDataClass>()
    val quantity = ArrayList<CartDataClass>()
    var total = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        binding = FragmentCartBinding.bind(view)
        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences(
            "Text", Context.MODE_PRIVATE
        )
        val email = sharedPreferences.getString("Email", "").toString()
        val rootRef = FirebaseDatabase.getInstance().reference
        val userRef = rootRef.child("Cart")
        val eventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    val userEmail = ds.child("email").getValue(String::class.java)
                    if (userEmail!!.contains(email)) {
                        val namevalue = ds.child("name").value.toString()
                        name.add(CartDataClass(Name = namevalue))
                        price.add(CartDataClass(Price = ds.child("price").value.toString()))
                        quantity.add(CartDataClass(quantity = ds.child("quantity").value.toString()))
                    }
                }
                if (name == null) {
                    binding.cartTotal.visibility = View.GONE
                } else {
                    binding.cartRv.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                    binding.cartRv.adapter = CartAdapter(name, price, quantity, this@CartFragment)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        userRef.addListenerForSingleValueEvent(eventListener)
        binding.placeOrder.setOnClickListener {

        }
        binding.placeOrder.setOnClickListener {
            val dwlwtweventListener: ValueEventListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (ds in snapshot.children) {
                        val userEmail = ds.child("email").getValue(String::class.java)
                        if (userEmail!!.contains(email)) {
                            val dref = FirebaseDatabase.getInstance().getReference("Cart")
                            dref.child(
                                ds.child("name").getValue().toString()
                            ).removeValue().addOnSuccessListener {
                                Toast.makeText(requireContext(), "Order Placed", Toast.LENGTH_SHORT)
                                    .show()
                                findNavController().navigate(R.id.action_cartFragment_to_buybookfragment)
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            }
            userRef.addListenerForSingleValueEvent(dwlwtweventListener)

            FirebaseMessaging.getInstance().token.addOnCompleteListener {
                if (!it.isSuccessful) {

                }
                val token: String = it.result
                Log.d("Token", token)

            }
        }
        return binding.root

    }

    override fun TotalPrice(total: Int) {
        binding.cartTotal.text = total.toString()
    }

    override fun removeclick(postion: Int) {
        val removetext = name[postion].Name.toString()

        val dref = FirebaseDatabase.getInstance().getReference("Cart")
        dref.child(removetext).removeValue().addOnSuccessListener {
            Toast.makeText(requireContext(), "Done", Toast.LENGTH_SHORT).show()
            val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences(
                "Text", Context.MODE_PRIVATE
            )
            val email = sharedPreferences.getString("Email", "").toString()
            val rootRef = FirebaseDatabase.getInstance().reference
            val userRef = rootRef.child("Cart")
            val eventListener: ValueEventListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    name.clear()
                    price.clear()
                    quantity.clear()
                    for (ds in snapshot.children) {
                        val userEmail = ds.child("email").getValue(String::class.java)
                        if (userEmail!!.contains(email)) {
                            val namevalue = ds.child("name").value.toString()
                            name.add(CartDataClass(Name = namevalue))
                            price.add(CartDataClass(Price = ds.child("price").value.toString()))
                            quantity.add(CartDataClass(quantity = ds.child("quantity").value.toString()))
                        }
                    }
                    if (name == null) {
                        binding.cartTotal.visibility = View.GONE
                    } else {
                        binding.cartRv.layoutManager = LinearLayoutManager(
                            requireContext(), LinearLayoutManager.VERTICAL, false
                        )
                        binding.cartRv.adapter =
                            CartAdapter(name, price, quantity, this@CartFragment)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            }
            userRef.addListenerForSingleValueEvent(eventListener)

        }

    }
}