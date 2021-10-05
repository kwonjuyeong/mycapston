package com.example.myapplication.Main.Board

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class PostAdapter(private val items : ArrayList<String>, private val context: Context?,
                  private val monthClickCallback: ((String) -> Unit)?)
    : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.tag_item, parent, false))
    }
    override fun getItemCount(): Int {
        return items.size
    }
    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val category_tags: TextView = view.findViewById(R.id.tags_value)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.itemView
    }
}


