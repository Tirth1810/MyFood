package com.example.myapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapp.Adapters.HistoryAdapter
import com.example.myapp.DataClass.History
import kotlinx.android.synthetic.main.activity_histroy_recipes.*

class Histroy_Recipes : AppCompatActivity() {
    val history = ArrayList<History>()
    val Favourites=ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_histroy_recipes)
        val Favourites=intent.getSerializableExtra("Favourites")
        history.add(History(Favourites.toString()))
        history_rv.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        history_rv.adapter=HistoryAdapter(history)

    }
}