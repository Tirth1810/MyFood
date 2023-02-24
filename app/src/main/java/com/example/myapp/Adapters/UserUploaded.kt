package com.example.myapp.Adapters

import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.DataClass.PostRecipe
import com.example.myapp.R
import de.hdodenhof.circleimageview.CircleImageView

class UserUploaded(
    private val list: ArrayList<PostRecipe>,
    private val listener: OnUserUploadedClick
) :
    RecyclerView.Adapter<UserUploaded.ViewHolder>() {
    inner class ViewHolder(private val itemview: View) : RecyclerView.ViewHolder(itemview),
        OnClickListener {
        val name: TextView = itemView.findViewById(R.id.trending_name)
        val imageview: CircleImageView = itemView.findViewById(R.id.trending_img)
        val chefname: TextView = itemview.findViewById(R.id.cat)
        var bg: ConstraintLayout =itemview.findViewById(R.id.trending_bg)

        init {
            itemview.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = absoluteAdapterPosition
            listener.onUseClick(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.trending_rv, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = list[position].recipeName
        holder.chefname.text = list[position].personName
        val bytes =
            android.util.Base64.decode(list[position].recipeimg, android.util.Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        holder.imageview.setImageBitmap(bitmap)
        if (position % 3 == 0) {
            holder.bg.setBackgroundColor(Color.parseColor("#FF8E8E93"))
        } else if (position % 3 == 1) {
            holder.bg.setBackgroundColor(Color.parseColor("#FF4CD964"))
        } else if (position % 3 == 2) {
            holder.bg.setBackgroundColor(Color.parseColor("#FFFF3B30"))
        }
    }

    interface OnUserUploadedClick {
        fun onUseClick(position: Int)
    }
}
