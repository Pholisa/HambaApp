package com.example.hambaapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CommentsAdapter(
    private val context: Context,
    private val businessList: MutableList<User3> = mutableListOf(),
) : RecyclerView.Adapter<CommentsAdapter.MyViewHolder>()  {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById(R.id.tvName1)
        val comment: TextView = itemView.findViewById(R.id.tvcomment)

    }
    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsAdapter.MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.delete_listing_item, parent, false)
        return MyViewHolder(itemView)
    }
    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
    //Grabbing data from inner class to bind to User3 constructor
    override fun onBindViewHolder(holder: CommentsAdapter.MyViewHolder, position: Int) {
        val currentBusiness = businessList[position]

        holder.userName.text = currentBusiness.name
        holder.comment.text = currentBusiness.comment

    }
    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
    override fun getItemCount(): Int {
        return businessList.size
    }
}
//------------------------------------------ooo000EndOfFile000ooo-----------------------------------