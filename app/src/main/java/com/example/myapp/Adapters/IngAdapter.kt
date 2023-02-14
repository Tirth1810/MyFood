package com.example.myapp.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapp.R
import com.example.myapp.DataClass.VegData

class IngAdapter(private val inglist: List<VegData>, private val listener: OnVegDataSelected) :
    RecyclerView.Adapter<ViewHolder>() {
    private var context:Context?=null
    var text = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context=parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.veg_rv_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return inglist.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Glide.with(context!!).load(inglist[position].imageurl).into(holder.image)
        holder.ingtext.text = inglist[position].Name
        holder.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                text = holder.ingtext.text.toString()
                listener.OnVegSelecTed(text, 1)
            }
            if (!isChecked) {
                text = holder.ingtext.text.toString()
                listener.OnVegSelecTed(text, 0)

            }
        }
    }

    interface OnVegDataSelected {
        fun OnVegSelecTed(vegText: String, checked: Int)
    }
}


