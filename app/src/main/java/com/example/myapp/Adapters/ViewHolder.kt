package com.example.myapp.Adapters

import android.view.View
import android.view.View.OnClickListener
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.R
import de.hdodenhof.circleimageview.CircleImageView


class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview), OnClickListener {
    val image: CircleImageView = itemview.findViewById(R.id.circleImageView3)
    val ingtext: TextView = itemview.findViewById(R.id.ing_text)
    val  checkBox:CheckBox=itemview.findViewById(R.id.item_rv_check)

    init {
        itemview.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        checkBox.isChecked
    }

}
