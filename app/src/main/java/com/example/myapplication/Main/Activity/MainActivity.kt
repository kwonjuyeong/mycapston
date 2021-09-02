package com.example.myapplication.Main.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Main.Activity.DataModel
import com.example.myapplication.Main.Activity.PhotoAdapter
import com.example.myapplication.R
import kotlinx.android.synthetic.main.activity_main.*
import com.kakao.sdk.common.util.Utility


class MainActivity : AppCompatActivity() {
    private lateinit var  photoAdapter: PhotoAdapter
    private var dataList = mutableListOf<DataModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView.layoutManager = GridLayoutManager(applicationContext, 4)
        photoAdapter = PhotoAdapter(applicationContext)
        recyclerView.adapter = photoAdapter


        dataList.add(DataModel("치킨", R.drawable.chiken))
        dataList.add(DataModel("피자", R.drawable.pizza))
        dataList.add(DataModel("중식", R.drawable.jjajang))
        dataList.add(DataModel("한식", R.drawable.rice))
        dataList.add(DataModel("양식", R.drawable.steak))
        dataList.add(DataModel("커피", R.drawable.coffee))
        dataList.add(DataModel("돈까스", R.drawable.gas))
        dataList.add(DataModel("일식", R.drawable.sushi))


        photoAdapter.setDataList(dataList)




    }
}