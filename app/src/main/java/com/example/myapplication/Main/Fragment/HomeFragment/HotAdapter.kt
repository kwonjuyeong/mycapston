package com.example.myapplication.Main.Fragment.HomeFragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.DTO.BoardDTO
import com.example.myapplication.Main.Board.Detail.Chat.BoardChat
import com.example.myapplication.R
import com.facebook.internal.Mutable
import com.google.firebase.firestore.FirebaseFirestore

@SuppressLint("NotifyDataSetChanged")
class HotAdapter(val context : Context) :RecyclerView.Adapter<HotAdapter.HotViewHolder>(){
    private var hotListDTO = mutableListOf<BoardDTO>()
    private var hotListUid = mutableListOf<String>()
    fun setListUid(data : MutableList<String>){
        hotListUid = data
    }
    fun setHotListDTO(data : MutableList<BoardDTO>){
        hotListDTO = data
    }
    init{
        notifyDataSetChanged()
    }
    class HotViewHolder(itemview : View) : RecyclerView.ViewHolder(itemview){
        val profile: ImageView = itemView.findViewById(R.id.hot_profile)
        val title: TextView = itemView.findViewById(R.id.hot_title)
        val contents: TextView = itemView.findViewById(R.id.hot_content)
        val likeCount: TextView = itemView.findViewById(R.id.hot_like_count)
        val boardImage: ImageView = itemView.findViewById(R.id.hot_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.hot_list_item,parent,false)
        return HotViewHolder(itemView)

    }
    override fun onBindViewHolder(holder: HotViewHolder, position: Int) {
        val data = hotListDTO[position]
        val BUid = hotListUid[position]
        holder.title.text = data.postTitle
        holder.contents.text = data.contents
        holder.boardImage
        holder.likeCount.text = data.likeCount.toString()

        if (data.ProfileUrl.toString() != "null")
            Glide.with(holder.itemView.context).load(data.ProfileUrl).into(holder.profile)
        else
            holder.profile.setImageResource(R.drawable.ic_baseline_account_circle_signiture)
        Glide.with(holder.itemView.context).load(data.imageUrlWrite).into(holder.boardImage)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, BoardChat::class.java)
            intent.putExtra("commentUid", BUid)
            intent.putExtra("owneruid", data.uid)
            ContextCompat.startActivity(holder.itemView.context, intent, null)
        }
    }

    override fun getItemCount() = hotListDTO.size
    fun clear(){
        hotListUid.clear()
        hotListDTO.clear()
    }

}