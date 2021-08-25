package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProfileAdapter(val profileList: ArrayList<Profiles> ) : RecyclerView.Adapter<ProfileAdapter.CustomViewHolder>()
{


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileAdapter.CustomViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfileAdapter.CustomViewHolder, position: Int) {
    holder.gender.setImageResource(profileList.get(position).gender)
        holder.names.text = profileList.get(position).names
        holder.age.text = profileList.get(position).age.toString()
        holder.job.text = profileList.get(position).job
    }

    override fun getItemCount(): Int {
    return profileList.size
    }



    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val gender = itemView.findViewById<ImageView>(R.id.iv_profile) //가져올 이미지
        val names = itemView.findViewById<TextView>(R.id.tv_name) //이름
        val age = itemView.findViewById<TextView>(R.id.tv_name) //이름
        val job = itemView.findViewById<TextView>(R.id.tv_name) //이름


    }
}