package com.example.myapp.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.example.myapp.DataClass.Dairy
import com.example.myapp.R
import de.hdodenhof.circleimageview.CircleImageView

class DairyAdapter(
    private val dairyList: List<Dairy>,
    private val listener: OnSelectedItems
) :
    androidx.recyclerview.widget.RecyclerView.Adapter<DairyAdapter.ViewHolder>() {
    private var context: Context? = null
    var text: String = ""


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.veg_rv_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dairyList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                text = holder.dairyName.text.toString()
                listener.onSelect(text, 1)
            } else {
                text = holder.dairyName.text.toString()
                listener.onSelect(text, 0)
            }

        }
        Glide.with(context!!).load(dairyList[position].imageurl).into(holder.dairyImage)
        holder.dairyName.text = dairyList[position].Name

    }


    inner class ViewHolder(itemView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView), OnClickListener {
        val dairyName: TextView = itemView.findViewById(R.id.ing_text)
        val dairyImage: CircleImageView = itemView.findViewById(R.id.circleImageView3)
        val checkbox: CheckBox = itemView.findViewById(R.id.item_rv_check)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            checkbox.isChecked
        }


    }

    interface OnSelectedItems {
        fun onSelect(text: String, checked: Int)

    }

}
