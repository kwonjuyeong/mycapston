package com.example.myapplication.Main.Fragment.BoardFrgment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.DTO.BoardDTO
import com.example.myapplication.R

class BoardListAdapter(private val boarddtos: MutableList<BoardDTO>) : RecyclerView.Adapter<BoardListAdapter.CTViewholder>(){
    class CTViewholder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val profile : ImageView = itemView.findViewById(R.id.boardlist_profile)
        val title : TextView = itemView.findViewById(R.id.boardlist_title)
        val Contents : TextView = itemView.findViewById(R.id.boarlist_content)
        val boarddate : TextView = itemView.findViewById(R.id.boardlist_date)
        val boardimage : ImageView = itemView.findViewById(R.id.boardlist_image)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CTViewholder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.board_list_item, parent, false)
        return CTViewholder(itemView)
    }

    override fun onBindViewHolder(holder: CTViewholder, position: Int) {
        val currentitem = boarddtos[position]
        holder.title.text = currentitem.postTitle
        holder.Contents.text = currentitem.contents
        holder.boarddate.text = currentitem.Writed_date
        Glide.with(holder.itemView.context).load(currentitem.ProfileUrl).into(holder.profile)
        Glide.with(holder.itemView.context).load(currentitem.imageUrlWrite).into(holder.boardimage)
    }

    override fun getItemCount() = boarddtos.size

    fun addItems(items: List<BoardDTO>) {
        this.boarddtos.addAll(items)
        notifyDataSetChanged()
    }

}