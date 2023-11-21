package com.example.hambaapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdminAdapter (private  val businessList: ArrayList<BusinessInfoModel>) : RecyclerView.Adapter<AdminAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_new_requests, parent, false)
        return  ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val newRequest = businessList[position]
        holder.tvBusName.text = newRequest.companyName
        holder.tvRegister.text = newRequest.registrationNumber
        holder.tvAddress.text = newRequest.businessAddress
        holder.tvEmail.text = newRequest.emailAddress
        holder.tvTelephone.text = newRequest.telephoneNumber
        holder.tvCategory.text = newRequest.businessCategory
    }

    override fun getItemCount(): Int {
        return businessList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvBusName : TextView = itemView.findViewById(R.id.tv_businessNameDisplay)
        val tvRegister : TextView = itemView.findViewById(R.id.tv_businessRegistrationDisplay)
        val tvAddress : TextView = itemView.findViewById(R.id.tv_businessAddressDisplay)
        val tvEmail : TextView = itemView.findViewById(R.id.tv_businessEmailDisplay)
        val tvTelephone : TextView = itemView.findViewById(R.id.tv_businessTelephoneDisplay)
        val tvCategory : TextView = itemView.findViewById(R.id.tv_businessCategoryDisplay)

    }
}