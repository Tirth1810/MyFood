package com.example.myapp.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.DataClass.CartDataClass
import com.example.myapp.Fragments.CartFragment
import com.example.myapp.R
import com.google.firebase.database.FirebaseDatabase

class CartAdapter(
    val Name: ArrayList<CartDataClass>,
    val Price: ArrayList<CartDataClass>,
    val Quantity: ArrayList<CartDataClass>,
    val listener: Total
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    val total = 0
    private var aContext: Context? = null

    class ViewHolder(val itemview: View) : RecyclerView.ViewHolder(itemview) {
        val name: TextView = itemView.findViewById(R.id.cart_name)
        val price: TextView = itemview.findViewById(R.id.cart_price)
        val qua: TextView = itemView.findViewById(R.id.cart_qua)
        val remove: TextView = itemview.findViewById(R.id.remove_book)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_recyler, parent, false)
        aContext = parent.context
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return Name.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = Name[position].Name.toString()
        holder.price.text = Price[position].Price.toString()
        holder.qua.text = Quantity[position].quantity.toString()
        holder.remove.setOnClickListener {
            listener.removeclick(position)
        }
        var totalPrice = 0
        for (i in 0 until Price.size) {
            totalPrice += Price.get(i).Price!!.toInt() * Quantity[i].quantity!!.toInt()
            listener.TotalPrice(totalPrice)
        }

    }

    interface Total {
        fun TotalPrice(total: Int)
        fun removeclick(postion:Int)
    }

}