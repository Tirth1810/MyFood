package com.example.myapp.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapp.DataClass.Itemas
import com.example.myapp.R

class ItemAdapter(private val list: List<Itemas>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    private var mcontext: Context? = null

    inner class ViewHolder(private val itemview: View) : RecyclerView.ViewHolder(itemview),
        OnClickListener {
        val name: TextView = itemview.findViewById(R.id.ite_rv_textn)
        val category: TextView = itemview.findViewById(R.id.ite_rv_textc)
        val image:ImageView=itemview.findViewById(R.id.item_rv_image)

        init {
            itemview.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = absoluteAdapterPosition
            listener.OnItemClick(position)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mcontext=parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.itemrecyclerview_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val Items = list[position]
        holder.name.text = Items.Name
        holder.category.text = Items.Category
        Glide.with(mcontext!!).load(Items.imageurl).into(holder.image)

    }


    override fun getItemCount(): Int {
        return list.size

    }

    interface OnItemClickListener {
        fun OnItemClick(position: Int)
    }
}