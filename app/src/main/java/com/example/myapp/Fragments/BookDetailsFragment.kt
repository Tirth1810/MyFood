package com.example.myapp.Fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.myapp.DataClass.CartDataClass
import com.example.myapp.R
import com.example.myapp.databinding.FragmentBookDetailsBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class BookDetailsFragment : Fragment() {
    private lateinit var database: DatabaseReference
    private lateinit var dref: DatabaseReference
    private val args: BookDetailsFragmentArgs by navArgs()
    private lateinit var binding: FragmentBookDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_book_details, container, false)
        binding = FragmentBookDetailsBinding.bind(view)
        database = FirebaseDatabase.getInstance().getReference("BookDetails")
        database.child(args.name.toString()).get().addOnSuccessListener {
            if (it.exists()) {
                binding.bookDetailsBookName.text = it.child("Name").value.toString()
                binding.bookDetailsBookPages.text = it.child("Pages").value.toString()
                binding.bookDetailsStar.text = it.child("Rating").value.toString()
                binding.bookDetailsPrice.text = it.child("Price").value.toString()
                binding.bookDetailMainDesc.text = it.child("Desc").value.toString()
                Glide.with(this).load(it.child("imageurl").value.toString())
                    .into(binding.bookDetailsBookImg)
            }
        }
        binding.bookDetailsBack.setOnClickListener {
            findNavController().navigate(R.id.action_bookDetailsFragment_to_trendingFragment)
        }
        var count = 0
        binding.counterPlus.setOnClickListener {
            count++
            binding.counterText.text = count.toString()
        }
        binding.counterMinus.setOnClickListener {
            if (count < 0) {
                count = 0
            } else {
                count--
                binding.counterText.text = count.toString()
            }
        }
        binding.addToCartBtn.setOnClickListener {
            if (count == 0) {
                Toast.makeText(requireContext(), "Enter Quantity", Toast.LENGTH_SHORT).show()
            } else {
                val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences(
                    "Text",
                    Context.MODE_PRIVATE
                )
                val email = sharedPreferences.getString("Email", "").toString()
                dref = FirebaseDatabase.getInstance().getReference("Cart")
                val Name = binding.bookDetailsBookName.text.toString().trim()
                val Price = binding.bookDetailsPrice.text.toString().trim()
                val Quantity = binding.counterText.text.toString().trim()
                val cart = CartDataClass(
                    email, Name, Quantity.toString(), Price.toString()
                )
                val userid = dref.key!!
                dref.child(Name).setValue(cart).addOnCompleteListener {
                    Toast.makeText(requireContext(), "Added", Toast.LENGTH_SHORT).show()

                }.addOnCanceledListener {
                    Toast.makeText(requireContext(), "cancel", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.bookDetailsCart.setOnClickListener {
            findNavController().navigate(R.id.action_bookDetailsFragment_to_cartFragment)
        }
        return binding.root
    }
}