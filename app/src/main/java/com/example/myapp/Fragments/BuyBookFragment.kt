package com.example.myapp.Fragments

import android.annotation.SuppressLint
import android.app.ProgressDialog
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
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.Adapters.BuyBookAdapter
import com.example.myapp.DataClass.BuyBook
import com.example.myapp.R
import com.google.firebase.database.*

class BuyBookFragment : Fragment(), BuyBookAdapter.OnBuyClick {
    private lateinit var firebaseReffrence: DatabaseReference
    val Buy = ArrayList<BuyBook>()
    val filter = ArrayList<BuyBook>()

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_buybook, container, false)
        val back = view.findViewById<CardView>(R.id.bybook_back)
        val cart = view.findViewById<CardView>(R.id.bybook_cart)
        cart.setOnClickListener {
            findNavController().navigate(R.id.action_trendingFragment_to_cartFragment)
        }
        back.setOnClickListener {
            findNavController().navigate(R.id.action_trendingFragment_to_deashBoard)
        }
        val buyBookRv = view.findViewById<RecyclerView>(R.id.trending_recyclerview)
        buyBookRv?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Loading")
        progressDialog.setCancelable(false)
        progressDialog.show()
        filter.clear()
        firebaseReffrence = FirebaseDatabase.getInstance().getReference("Books")
        firebaseReffrence.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                progressDialog.hide()
                if (snapshot.exists()) {
                    for (Books in snapshot.children) {
                        val allBooks = Books.getValue(BuyBook::class.java)
                        Buy.add(allBooks!!)
                        buyBookRv?.adapter = BuyBookAdapter(Buy, this@BuyBookFragment)
                    }
                }
                filter.addAll(Buy)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        val search = view.findViewById<EditText>(R.id.book_search)
        search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isEmpty()) {
                    filter.clear()
                    filter.addAll(Buy)
                    buyBookRv?.adapter = BuyBookAdapter(filter, this@BuyBookFragment)
                } else {
                    Filter(s.toString(), Buy)
                }
            }
        })

        return view
    }

    private fun Filter(Search: String, Books: ArrayList<BuyBook>) {
        filter.clear()
        for (Book in Books) {
            if (Book.BookName!!.toLowerCase()!!.contains(Search.toLowerCase())) {
                filter.add(Book)
                val buyBookRv = view?.findViewById<RecyclerView>(R.id.trending_recyclerview)
                buyBookRv?.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                buyBookRv?.adapter = BuyBookAdapter(filter, this@BuyBookFragment)

            } else {

            }
        }
    }

    override fun OnBookBuy(pos: Int) {
        val bookName = filter[pos]
        val name = bookName.BookName.toString().trim()
        val direction = BuyBookFragmentDirections.actionTrendingFragmentToBookDetailsFragment(
            name
        )
        findNavController().navigate(direction)
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