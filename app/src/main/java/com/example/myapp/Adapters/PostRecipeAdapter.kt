package com.example.myapp.Adapters

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.DataClass.PostRecipe
import com.example.myapp.R

class PostRecipeAdapter(var list: ArrayList<PostRecipe>) :
    RecyclerView.Adapter<PostRecipeAdapter.ViewHolder>() {
    class ViewHolder(private val itemview: View) : RecyclerView.ViewHolder(itemview) {
        val PersonName = itemview.findViewById<TextView>(R.id.chefname_id)
        val RecipeName = itemview.findViewById<TextView>(R.id.postrecipe_name)
        val Time = itemview.findViewById<TextView>(R.id.cooking_time)
        val Email = itemview.findViewById<TextView>(R.id.textView35)
        val ing = itemview.findViewById<TextView>(R.id.ingredients_post_recipe)
        val desc = itemview.findViewById<TextView>(R.id.recipe_dec_postrecipe)
        val  imageView=itemView.findViewById<ImageView>(R.id.circleImageView7)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.users_recipe_rv_bg, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.Email.text = list[position].email
        holder.PersonName.text = list[position].personName
        holder.RecipeName.text = list[position].recipeName
        holder.Time.text = list[position].recipeTime
        holder.desc.text = list[position].recipeDesc
        holder.ing.text = list[position].recipeIng
        val  bytes=android.util.Base64.decode(list[position].recipeimg,android.util.Base64.DEFAULT)
        val bitmap=BitmapFactory.decodeByteArray(bytes,0,bytes.size)
        holder.imageView.setImageBitmap(bitmap)
    }
}