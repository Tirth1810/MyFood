package com.example.myapp.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.myapp.DataClass.Fruits
import com.example.myapp.R
import de.hdodenhof.circleimageview.CircleImageView

class FruitsAdapter(
    private val fruitsList: List<Fruits>,
    private val listener: OnFruitsSelectedItems
) :
    androidx.recyclerview.widget.RecyclerView.Adapter<FruitsAdapter.ViewHolder>() {
    private  var mcontext:Context?=null

    var text: String = ""
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mcontext=parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.veg_rv_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return fruitsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                text = holder.fruitName.text.toString()
                listener.onFruitsSelect(text,1)
            }
            if (!isChecked)
            {
                text=holder.fruitName.text.toString()
                listener.onFruitsSelect(text,0)
            }
        }




        Glide.with(mcontext!!).load(fruitsList[position].imageurl).into(holder.fruitImage)
        holder.fruitName.text = fruitsList[position].Name

    }


    inner class ViewHolder(itemView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView), OnClickListener {
        val fruitName: TextView = itemView.findViewById(R.id.ing_text)
        val fruitImage: CircleImageView = itemView.findViewById(R.id.circleImageView3)
        val checkbox: CheckBox = itemView.findViewById(R.id.item_rv_check)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {

            checkbox.isChecked
        }

    }

    interface OnFruitsSelectedItems {
        fun onFruitsSelect(fruitsText: String,checked:Int)

    }
}