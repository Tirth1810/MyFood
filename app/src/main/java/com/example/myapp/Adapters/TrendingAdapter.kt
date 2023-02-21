package com.example.myapp.Adapters

import android.content.Context
import android.graphics.Color
import android.provider.CalendarContract.Colors
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapp.DataClass.TrendingDataClass
import com.example.myapp.R
import de.hdodenhof.circleimageview.CircleImageView

class TrendingAdapter(val list: ArrayList<TrendingDataClass>) :
    RecyclerView.Adapter<TrendingAdapter.ViewHolder>() {
    private var context: Context? = null
    class ViewHolder(val itemview: View) : RecyclerView.ViewHolder(itemview) {
        val Image: CircleImageView = itemView.findViewById(R.id.trending_img)
        val Name:TextView=itemView.findViewById(R.id.trending_name)
        val Category:TextView=itemView.findViewById(R.id.cat)
        val likes:TextView=itemView.findViewById(R.id.likes)
        var bg:ConstraintLayout=itemview.findViewById(R.id.trending_bg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.trending_rv, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context!!).load(list[position].imageurl).into(holder.Image)
        holder.Name.text=list[position].Name
        holder.Category.text=list[position].Category
        holder.likes.text= list[position].Likes.toString()

        if (position % 3 == 0) {
          holder.bg.setBackgroundColor(Color.parseColor("#FF8E8E93"))
        } else if (position % 3 == 1) {
           holder.bg.setBackgroundColor(Color.parseColor("#FF4CD964"))
        } else if (position % 3 == 2) {
           holder.bg.setBackgroundColor(Color.parseColor("#FFFF3B30"))
        }
    }
}