package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

class ViewPagerAdapter2(var context: Context ,var tapList: ArrayList<Int>) : RecyclerView.Adapter<ViewPagerAdapter2.PagerViewHolder>() {
    var item = tapList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PagerViewHolder((parent))

    override fun getItemCount(): Int = Int.MAX_VALUE

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.food2.setImageResource(item[position%3])
    }

    inner class PagerViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder
        (LayoutInflater.from(context).inflate(R.layout.food_list_item2, parent, false)){

        val food2 = itemView.findViewById<ImageView>(R.id.imageView_food2)!!

    }
}