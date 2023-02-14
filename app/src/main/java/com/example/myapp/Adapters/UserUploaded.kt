package com.example.myapp.Adapters

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
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
        val name: TextView = itemView.findViewById(R.id.ite_rv_textn)
        val imageview: CircleImageView = itemView.findViewById(R.id.item_rv_image)
        val chefname: TextView = itemview.findViewById(R.id.ite_rv_textc)

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
            .inflate(R.layout.itemrecyclerview_card, parent, false)
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

    }

    interface OnUserUploadedClick {
        fun onUseClick(position: Int)
    }
}
