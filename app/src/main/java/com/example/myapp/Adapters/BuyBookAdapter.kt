package com.example.myapp.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.OnReceiveContentListener
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapp.DataClass.BuyBook
import com.example.myapp.R

class BuyBookAdapter(val list: ArrayList<BuyBook>, var listener: OnBuyClick) :
    RecyclerView.Adapter<BuyBookAdapter.ViewHolder>() {
    private var mcontext: Context? = null

    class ViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image = itemView.findViewById<ImageView>(R.id.book_img)
        val name = itemView.findViewById<TextView>(R.id.book_name)
        val pages = itemView.findViewById<TextView>(R.id.book_pages)
        val ratings = itemView.findViewById<RatingBar>(R.id.book_rating)
        val rupees = itemView.findViewById<TextView>(R.id.book_price)
        val buy = itemView.findViewById<TextView>(R.id.book_buy)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mcontext = parent.context
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.book_recyler_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bookposition = list[position]
        Glide.with(mcontext!!).load(bookposition.imageurl).into(holder.image)
        holder.name.text = bookposition.BookName.toString()
        holder.pages.text = bookposition.BookPages.toString()
        val rating = bookposition.BookRatings
        holder.ratings.rating = rating!!.toFloat()
        holder.rupees.text = bookposition.BookPrice.toString()
        holder.buy.setOnClickListener {
            listener.OnBookBuy(position)
        }
    }

    interface OnBuyClick {
        fun OnBookBuy(pos: Int)

    }
}