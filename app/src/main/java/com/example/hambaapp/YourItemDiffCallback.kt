package com.example.hambaapp

import androidx.recyclerview.widget.DiffUtil
import com.example.hambaapp.HambaBusiness.Information

class YourItemDiffCallback : DiffUtil.ItemCallback<Information>() {
    override fun areItemsTheSame(oldItem: Information, newItem: Information): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Information, newItem: Information): Boolean {
        return oldItem.companyName == newItem.companyName &&
                oldItem.registerNumber == newItem.registerNumber &&
                oldItem.emailAddress == newItem.emailAddress &&
                oldItem.telephoneNumber == newItem.telephoneNumber &&
                oldItem.businessType == newItem.businessType &&
                oldItem.businessAddress == newItem.businessAddress &&
                oldItem.businessCategory == newItem.businessCategory
    }
}