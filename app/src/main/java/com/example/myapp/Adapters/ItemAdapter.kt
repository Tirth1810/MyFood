package com.example.myapp.Adapters

import android.content.Context
import android.os.Bundle
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapp.DataClass.Itemas
import com.example.myapp.Fragments.RecipeFragment
import com.example.myapp.R
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.itemrecyclerview_card.view.*
import java.lang.reflect.Type

class ItemAdapter(private val list: List<Itemas>, private val listener: OnItemClickListener) :

    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    private var Favourites = ArrayList<String>()
    private var mcontext: Context? = null
    var text: String = ""
    val checkBoxStateArray=SparseBooleanArray()
    var selectedItems = mutableSetOf<String>()
    inner class ViewHolder(private val itemview: View) : RecyclerView.ViewHolder(itemview),
        OnClickListener {
        val name: TextView = itemview.findViewById(R.id.ite_rv_textn)
        val category: TextView = itemview.findViewById(R.id.ite_rv_textc)
        val image: ImageView = itemview.findViewById(R.id.item_rv_image)
        val checkbox: MaterialCheckBox = itemview.findViewById(R.id.usr_fav)
        init {
            itemview.setOnClickListener(this)

        }

        override fun onClick(v: View?) {

            val position = absoluteAdapterPosition
            listener.OnItemClick(position)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mcontext = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.itemrecyclerview_card, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val Items = list[position]
        if(Items.selected==true){
            holder.checkbox.isChecked=true
        }
        holder.name.text = Items.Name
        holder.category.text = Items.Category
        Glide.with(mcontext!!).load(Items.imageurl).into(holder.image)
        val sharedPreferences = holder.itemView.context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        sharedPreferences.getStringSet("selectedItems",null)?.let {
            for(i in 0 until it.size) {
                selectedItems.add(it.elementAt(i))
            }
        }
        holder.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                selectedItems!!.add(holder.name.text.toString())
                val sharedPreferences = holder.itemView.context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putStringSet("selectedItems",selectedItems)
                editor.apply()
                text = holder.name.text.toString()
                listener.Fovourites(text, 1, holder.checkbox)
            } else {
                text = holder.name.text.toString()
                listener.Fovourites(text, 0, holder.checkbox)
                selectedItems!!.remove(holder.name.text.toString())
                val sharedPreferences = holder.itemView.context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putStringSet("selectedItems",selectedItems)
                editor.apply()
            }

        }



    }
    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)

    }

    override fun getItemCount(): Int {
        return list.size

    }

    interface OnItemClickListener {
        fun OnItemClick(position: Int)
        fun Fovourites(text: String, checked: Int, checkbox: CheckBox)
    }


}