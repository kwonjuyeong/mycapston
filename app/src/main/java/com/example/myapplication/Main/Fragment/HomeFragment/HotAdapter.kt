package com.example.myapplication.Main.Fragment.HomeFragment

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.DTO.BoardDTO
import com.example.myapplication.R
import com.google.firebase.firestore.FirebaseFirestore

@SuppressLint("NotifyDataSetChanged")
class HotAdapter :RecyclerView.Adapter<HotAdapter.HotViewHolder>(){
    val firestore = FirebaseFirestore.getInstance()
    val Ref = firestore.collection("Board")
    val list = mutableListOf<BoardDTO>()
    val uids = mutableListOf<String>()
    init{
        Ref.addSnapshotListener { value, error ->
            for(snapshot in value!!.documents) {
                val item = snapshot!!.toObject(BoardDTO::class.java)
                list.add(item!!)
                uids.add(snapshot.id)
            }
            notifyDataSetChanged()
        }

    }
    class HotViewHolder(itemview : View) : RecyclerView.ViewHolder(itemview){
        val profile: ImageView = itemView.findViewById(R.id.boardlist_profile)
        val title: TextView = itemView.findViewById(R.id.boardlist_title)
        val Contents: TextView = itemView.findViewById(R.id.boarlist_content)
        val boarddate: TextView = itemView.findViewById(R.id.boardlist_date)
        val boardimage: ImageView = itemView.findViewById(R.id.boardlist_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: HotViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

}