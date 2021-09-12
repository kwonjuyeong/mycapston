package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_item_main.*


class List_item : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_item_main)

        val profileList = arrayListOf(
            //  Profiles(R.drawable.chat, "피자", 5, "2KM")
            Profiles(R.drawable.chicken, "치킨", 2, "3KM"),
            Profiles(R.drawable.chat, "피자", 5, "5KM"),
            Profiles(R.drawable.chat, "피자", 5, "5KM"),
            Profiles(R.drawable.chat, "피자", 5, "5KM"),


            )

        rv_profile.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_profile.setHasFixedSize(true)

        rv_profile.adapter = ProfileAdapter(profileList)
    }
}