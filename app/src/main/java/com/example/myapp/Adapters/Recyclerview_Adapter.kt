package com.example.myapp.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapp.R
import com.example.myapp.DataClass.Data
import de.hdodenhof.circleimageview.CircleImageView

class Recyclerview_Adapter(
    private val mList: List<Data>,
    private val listener: onDataItemClicklistener
) : RecyclerView.Adapter<Recyclerview_Adapter.ViewHolder>() {
    private  var aContext:Context?=null
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), OnClickListener {
        val textView: TextView = itemView.findViewById(R.id.rv_card_tv)
        val imageview:CircleImageView=itemView.findViewById(R.id.circleImageView)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = absoluteAdapterPosition
            listener.onitemclick(position)
        }
    }


    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        aContext=parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_rv, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val Data = mList[position]
        holder.textView.text = Data.Name
        Glide.with(aContext!!).load(Data.imageurl).into(holder.imageview)


    }

    override fun getItemCount(): Int {

        return mList.size
    }

    interface onDataItemClicklistener {
        fun onitemclick(position: Int)
    }
}

