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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_histroy_recipes)
        val data: String = intent.getStringExtra("Data").toString()
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show()
        history.add(History(data))
        history_rv.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        history_rv.adapter=HistoryAdapter(history)

    }
}