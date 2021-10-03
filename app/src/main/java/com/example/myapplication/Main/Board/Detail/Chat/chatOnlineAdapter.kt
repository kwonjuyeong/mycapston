package com.example.myapplication.Main.Board.Detail.Chat

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.DTO.UserinfoDTO
import com.example.myapplication.R
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import de.hdodenhof.circleimageview.CircleImageView

@SuppressLint("NotifyDataSetChanged")
class chatOnlineAdapter(val userList : ArrayList<String>,val context : Context):RecyclerView.Adapter<chatOnlineAdapter.ChatOnlineHolder>() {
    val firestore = FirebaseFirestore.getInstance()
    val Uef = firestore.collection("userid")
    val infoList = mutableListOf<UserinfoDTO>()
    init {
        Log.e("어댑터 유저 리스트", userList.toString() )
        for(i in userList){
            Uef.document(i).addSnapshotListener { value, error ->
                var item = value?.toObject(UserinfoDTO::class.java)
                infoList.add(item!!)
            }
            notifyDataSetChanged()
        }
    }
    class ChatOnlineHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profile :CircleImageView = itemView.findViewById(R.id.status_profile)
        val nickname : TextView = itemView.findViewById(R.id.status_nickname)
        val online : ImageView = itemView.findViewById(R.id.status_sign)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatOnlineHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_chat_list,parent,false)
        return ChatOnlineHolder(itemView)
    }

    override fun onBindViewHolder(holder: ChatOnlineHolder, position: Int) {
        val data = infoList[position]
        if(data.ProfileUrl.toString() != "null")
            Glide.with(holder.itemView.context).load(data.ProfileUrl).into(holder.profile)
        else
            holder.profile.setImageResource(R.drawable.ic_baseline_account_circle_signiture)
        for(j in data.status.keys)
            if(j == "online")
                holder.online.setImageResource(R.drawable.round_online)
            else if (j == "offline")
                holder.online.setImageResource(R.drawable.round_offline)
        holder.nickname.text = data.nickname
    }

    override fun getItemCount() = infoList.size
}
