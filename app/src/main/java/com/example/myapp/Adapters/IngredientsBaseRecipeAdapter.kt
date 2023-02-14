package com.example.myapp.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapp.DataClass.IngredientsBasedDataClass
import com.example.myapp.R

class IngredientsBaseRecipeAdapter(private  val  list:ArrayList<IngredientsBasedDataClass>,private val listener: RecipeBasedOnIng):
    RecyclerView.Adapter<IngredientsBaseRecipeAdapter.ViewHolder>() {
    private  var rContext: Context?=null
    inner  class ViewHolder(private val itemView: View):RecyclerView.ViewHolder(itemView),OnClickListener {
        val name: TextView = itemView.findViewById(R.id.ite_rv_textn)
        val category: TextView = itemView.findViewById(R.id.ite_rv_textc)
        val image: ImageView =itemView.findViewById(R.id.item_rv_image)
        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val position = absoluteAdapterPosition
            listener.OnRecipeClick(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        rContext=parent.context
        val view=LayoutInflater.from(parent.context).inflate(R.layout.itemrecyclerview_card,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(rContext!!).load(list[position].imageurl).into(holder.image)
        holder.name.text=list[position].Name
        holder.category.text=list[position].Category
    }
    interface RecipeBasedOnIng{
        fun OnRecipeClick(position: Int)
    }
}