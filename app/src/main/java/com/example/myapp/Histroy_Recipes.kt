package com.example.myapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapp.Adapters.HistoryAdapter
import com.example.myapp.Adapters.ItemAdapter
import com.example.myapp.DataClass.History
import kotlinx.android.synthetic.main.activity_histroy_recipes.*

class Histroy_Recipes : AppCompatActivity() ,ItemAdapter.OnItemClickListener{
    val history = ArrayList<History>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_histroy_recipes)



    }

    override fun OnItemClick(position: Int) {

    }

    override fun Fovourites(text: String, checked: Int) {
        if(checked==1){
            history.add(History(text))

        }
        if (checked==0){
            history.remove(History(text))
        }
        history_rv.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        history_rv.adapter=HistoryAdapter(history)

    }
}