package com.example.myapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapp.Adapters.HistoryAdapter
import com.example.myapp.DataClass.History
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_histroy_recipes.*
import java.lang.reflect.Type

class Histroy_Recipes : AppCompatActivity() {
    var Favourites = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_histroy_recipes)
        val sharedPreferences = getSharedPreferences("Text", MODE_PRIVATE)
        val type: Type = object : TypeToken<ArrayList<String?>?>() {}.type
        val gson = Gson()
        val json = sharedPreferences.getString("Fav", null)
        Favourites=gson.fromJson(json,type)
        history_rv.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        history_rv.adapter=HistoryAdapter( Favourites)

    }
}