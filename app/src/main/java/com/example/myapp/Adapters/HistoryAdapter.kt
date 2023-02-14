package com.example.myapp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.DataClass.History
import com.example.myapp.R
import com.google.firebase.database.core.view.View

class HistoryAdapter(private val list:ArrayList<History>) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    class ViewHolder (private val itemmview:android.view.View):RecyclerView.ViewHolder(itemmview){
        val textview=itemmview.findViewById<TextView>(R.id.history_text)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view=LayoutInflater.from(parent.context).inflate(R.layout.history,parent,false,)
        return  ViewHolder(view)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      holder.textview.text=list[position].name
    }
}